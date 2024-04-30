package it.italiandudes.cards_against_humanity.server.game;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import it.italiandudes.cards_against_humanity.protocol.MessageExchanger;
import it.italiandudes.cards_against_humanity.protocol.client.MessageClientWinningChoice;
import it.italiandudes.cards_against_humanity.protocol.server.MessageServerCardsUpdate;
import it.italiandudes.cards_against_humanity.protocol.server.MessageServerSendUserChoicesToMaster;
import it.italiandudes.cards_against_humanity.protocol.server.MessageServerWinningChoice;
import it.italiandudes.cards_against_humanity.server.Server;
import it.italiandudes.cards_against_humanity.server.connection.ConnectionManager;
import it.italiandudes.cards_against_humanity.server.data.BlackCard;
import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import it.italiandudes.cards_against_humanity.server.data.UserData;
import it.italiandudes.cards_against_humanity.server.data.WhiteCard;
import it.italiandudes.cards_against_humanity.server.exceptions.*;
import it.italiandudes.cards_against_humanity.server.utils.ServerSettings;
import it.italiandudes.cards_against_humanity.utils.Randomizer;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class GameManager {

    // Attributes
    @NotNull private final ArrayList<@NotNull UserData> USER_DATA = new ArrayList<>();
    @NotNull private final HashMap<@NotNull String, @NotNull ArrayList<@NotNull WhiteCard>> USER_CHOICES = new HashMap<>();
    private UserConnection master = null;
    private UserConnection previousWinner = null;

    // Public Methods
    public void startNewGame() throws GameAlreadyRunningException, MasterAlreadyElectedException {
        if (GAME_STATE != GameState.LOBBY) throw new GameAlreadyRunningException("A game is already started");
        GAME_STATE = GameState.RUNNING;
        resetUserData(); // Reset all user wins and eventually remove not connected players
        fetchUserData(); // Load new players stats into the game if they exists
        startNextRound(); // Start game rolling
    }
    public void startNextRound() {
        new Thread(() -> {
            if (GAME_STATE != GameState.WAIT_FOR_NEXT_ROUND) {
                Logger.log("The game needs to be on waiting for next round to use this method", new InfoFlags(true, false));
                return;
            }

            try {
                electNewMaster();
            } catch (GameNotRunningException | MasterAlreadyElectedException e) {
                Logger.log("This should not happen", new InfoFlags(true, true));
                Server.showErrorAndShutdownServer(e);
                return;
            }

            try {
                broadcastCards();
            } catch (UserDisconnectedException | IOException e) {
                Logger.log("Someone has probably disconnected", new InfoFlags(true, false));
                Logger.log(e);
                abortGame();
                return;
            } catch (GameNotRunningException e) {
                Logger.log("This should not happen", new InfoFlags(true ,true));
                Server.showErrorAndShutdownServer(e);
                return;
            }

            //noinspection StatementWithEmptyBody
            while (USER_CHOICES.size() < USER_DATA.size() && GAME_STATE == GameState.RUNNING);
            if (GAME_STATE != GameState.RUNNING) {
                Logger.log("Game State changed to " + GAME_STATE.name() + " during set user choices", new InfoFlags(true, true));
                abortGame();
                return;
            }

            try {
                sendUserChoicesToMaster();
            } catch (GameNotRunningException e) {
                Logger.log("This should not happen", new InfoFlags(true, true));
                Server.showErrorAndShutdownServer(e);
                return;
            } catch (IOException e) {
                Logger.log("An error has occurred during the transmission of the choices to the master", new InfoFlags(true, false));
                Logger.log(e);
                abortGame();
                return;
            }

            @NotNull ArrayList<@NotNull WhiteCard> winningChoice;
            try {
                winningChoice = receiveWinningChoice();
            } catch (GameNotRunningException e) {
                Logger.log("This should not happen", new InfoFlags(true, true));
                Server.showErrorAndShutdownServer(e);
                return;
            } catch (ProtocolException | IOException e) {
                Logger.log("An error has occurred during winning choice receive", new InfoFlags(true, false));
                Logger.log(e);
                abortGame();
                return;
            }

            try {
                broadcastWinningChoice(winningChoice);
            } catch (GameNotRunningException e) {
                Logger.log("This should not happen", new InfoFlags(true, true));
                Server.showErrorAndShutdownServer(e);
                return;
            } catch (WinnerNotFoundException e) {
                Logger.log("The round winner was not found, probably the player has disconnected", new InfoFlags(true, false));
                Logger.log(e);
                abortGame();
                return;
            } catch (IOException e) {
                Logger.log("An error has occurred during winning choice receive", new InfoFlags(true, false));
                Logger.log(e);
                abortGame();
                return;
            }

            // TODO: finish to implement end game with infinite mode ecc.

        }).start();
    }
    public void setUserChoice(@NotNull final String username, @NotNull final ArrayList<@NotNull Integer> choiceIDs) {
        if (USER_CHOICES.containsKey(username)) return;
        List<UserData> users = USER_DATA.stream().filter(e -> e.getUsername().equals(username)).collect(Collectors.toList());
        if (users.isEmpty()) return;
        UserData user = users.get(0);
        ArrayList<WhiteCard> cards = new ArrayList<>();
        for (@NotNull Integer id : choiceIDs) {
            List<WhiteCard> matchWhiteCards = user.getWhiteCards().stream().filter(e -> e.getId() == id).collect(Collectors.toList());
            if (matchWhiteCards.isEmpty()) {
                GAME_STATE = GameState.CHEAT_DETECTED;
                return;
            } else {
                cards.add(matchWhiteCards.get(0));
            }
        }
        USER_CHOICES.put(username, cards);
    }
    public void electNewMaster() throws GameNotRunningException, MasterAlreadyElectedException {
        if (GAME_STATE != GameState.RUNNING) throw new GameNotRunningException("The game needs to be started");
        if (master != null) throw new MasterAlreadyElectedException("The master is already elected");
        if (previousWinner == null || ServerSettings.getSettings().getBoolean(ServerSettings.SettingsKeys.ALWAYS_RANDOMIZE_MASTER)) {
            master = ConnectionManager.getInstance().getEstablishedConnections().get(Randomizer.randomFromZeroTo(ConnectionManager.getInstance().getEstablishedConnections().size()));
        } else {
            master = previousWinner;
        }
    }

    // Private Methods
    private void broadcastWinningChoice(@NotNull final ArrayList<@NotNull WhiteCard> winningChoice) throws GameNotRunningException, WinnerNotFoundException, IOException {
        if (GAME_STATE != GameState.RUNNING) throw new GameNotRunningException("The game needs to be started");
        UserConnection winner = null;
        for (String username : USER_CHOICES.keySet()) {
            if (USER_CHOICES.get(username).equals(winningChoice)) {
                winner = ConnectionManager.getInstance().getEstablishedConnectionByUsername(username);
                break;
            }
        }
        if (winner == null) throw new WinnerNotFoundException("The winner wasn't found in the connected users");
        MessageServerWinningChoice winningChoiceMessage = new MessageServerWinningChoice(winner.getUsername(), winningChoice);
        for (UserConnection connection : ConnectionManager.getInstance().getEstablishedConnections()) {
            MessageExchanger.sendServerMessage(connection, winningChoiceMessage);
        }
    }
    @NotNull
    private ArrayList<@NotNull WhiteCard> receiveWinningChoice() throws GameNotRunningException, IOException, ProtocolException {
        if (GAME_STATE != GameState.RUNNING) throw new GameNotRunningException("The game needs to be started");
        JSONObject winningChoiceJSON = MessageExchanger.receiveClientMessage(master);
        MessageClientWinningChoice winningChoiceMessage = new MessageClientWinningChoice(winningChoiceJSON);
        return winningChoiceMessage.getWinningChoice();
    }
    private void sendUserChoicesToMaster() throws GameNotRunningException, IOException {
        if (GAME_STATE != GameState.RUNNING) throw new GameNotRunningException("The game needs to be started");
        MessageExchanger.sendServerMessage(master, new MessageServerSendUserChoicesToMaster(USER_CHOICES));
        GAME_STATE = GameState.WAITING_FOR_MASTER;
    }
    private void abortGame() {
        resetPreviousWinner();
        resetUserData();
        resetMaster();
        GAME_STATE = GameState.LOBBY;
    }
    private void broadcastCards() throws GameNotRunningException, UserDisconnectedException, IOException {
        if (GAME_STATE != GameState.RUNNING) throw new GameNotRunningException("The game needs to be started");
        BlackCard blackCard = CardsManager.getInstance().getRandomBlackCard();
        for (UserData userData : USER_DATA) {
            UserConnection connection = ConnectionManager.getInstance().getEstablishedConnectionByUsername(userData.getUsername());
            if (connection == null) throw new UserDisconnectedException(userData.getUsername() + " has disconnected during card broadcasting");
            ArrayList<@NotNull WhiteCard> newWhiteCards = CardsManager.getInstance().getRandomWhiteCards(userData.getMissingWhiteCardsAmount());
            if (newWhiteCards == null) newWhiteCards = new ArrayList<>();
            userData.getWhiteCards().addAll(newWhiteCards);
            MessageExchanger.sendServerMessage(connection, new MessageServerCardsUpdate(blackCard, newWhiteCards));
        }
    }
    private void resetUserData() {
        ArrayList<@NotNull UserConnection> connections = ConnectionManager.getInstance().getEstablishedConnections();
        for (UserData userData : USER_DATA) {
            if (connections.stream().map(UserConnection::getUsername).noneMatch(Predicate.isEqual(userData.getUsername()))) {
                USER_DATA.remove(userData);
            } else {
                userData.resetWins();
            }
        }
        USER_CHOICES.clear();
    }
    private void fetchUserData() {
        for (UserConnection connection : ConnectionManager.getInstance().getEstablishedConnections()) {
            if (USER_DATA.stream().map(UserData::getUsername).noneMatch(Predicate.isEqual(connection.getUsername()))) {
                USER_DATA.add(new UserData(connection));
            }
        }
    }
    private void resetMaster() {
        master = null;
    }
    private void resetPreviousWinner() {
        previousWinner = null;
    }

    // Instance
    private static GameManager INSTANCE = null;
    private static GameState GAME_STATE = GameState.LOBBY;
    @NotNull
    private static GameState getGameState() {
        return GAME_STATE;
    }
    @NotNull
    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }
}
