package it.italiandudes.cards_against_humanity.server.utils;

import it.italiandudes.cards_against_humanity.server.data.BlackCard;
import it.italiandudes.cards_against_humanity.server.data.WhiteCard;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public final class CardsManager {

    // Static
    private static final Random RANDOMIZER = new Random();

    // Attributes
    @NotNull private final ArrayList<@NotNull WhiteCard> AVAILABLE_WHITE_CARDS = new ArrayList<>();
    @NotNull private final ArrayList<@NotNull WhiteCard> NOT_AVAILABLE_WHITE_CARDS = new ArrayList<>();
    @NotNull private final ArrayList<@NotNull BlackCard> AVAILABLE_BLACK_CARDS = new ArrayList<>();
    @NotNull private final ArrayList<@NotNull BlackCard> NOT_AVAILABLE_BLACK_CARDS = new ArrayList<>();

    // Constructors
    private CardsManager() {
        try {
            String query = "SELECT * FROM white_cards;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("Prepared statement is null");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                AVAILABLE_WHITE_CARDS.add(new WhiteCard(result.getInt("id"), result.getString("content"), result.getInt("is_blank") != 0));
            }
            ps.close();
            query = "SELECT * FROM black_cards;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("Prepared statement is null");
            result = ps.executeQuery();
            while (result.next()) {
                AVAILABLE_BLACK_CARDS.add(new BlackCard(result.getInt("id"), result.getString("content"), result.getInt("empty_fields")));
            }
            ps.close();
        } catch (SQLException e) {
            Logger.log(e);
            Logger.log("An SQLException has occurred during cards reading", new InfoFlags(true, true));
            System.exit(0);
        }
    }

    // Methods
    @Nullable
    public ArrayList<WhiteCard> getRandomWhiteCards(final int amount) {
        if (amount <= 0) return null;
        if (AVAILABLE_WHITE_CARDS.isEmpty()) restoreAvailableWhiteCards();
        ArrayList<Integer> cardsPool = new ArrayList<>();
        ArrayList<WhiteCard> cards = new ArrayList<>();
        for (int i=0; i<amount; i++) {
            int number;
            //noinspection StatementWithEmptyBody
            while (cardsPool.contains(number = randomBetween(AVAILABLE_WHITE_CARDS.size())));
            WhiteCard whiteCard = AVAILABLE_WHITE_CARDS.get(number);
            cards.add(whiteCard);
            if (ServerSettings.getSettings().getBoolean(ServerSettings.SettingsKeys.ALWAYS_RANDOMIZE_WHITE_CARDS) || ServerSettings.getSettings().getBoolean(ServerSettings.SettingsKeys.INFINITE_MODE)) {
                AVAILABLE_WHITE_CARDS.remove(whiteCard);
                NOT_AVAILABLE_WHITE_CARDS.add(whiteCard);
            }
            cardsPool.add(number);
        }
        return cards;
    }
    @NotNull
    public BlackCard getRandomBlackCard() {
        if (AVAILABLE_BLACK_CARDS.isEmpty()) restoreAvailableBlackCards();
        int cardNumber = randomBetween(AVAILABLE_BLACK_CARDS.size());
        BlackCard card = AVAILABLE_BLACK_CARDS.get(cardNumber);
        if (ServerSettings.getSettings().getBoolean(ServerSettings.SettingsKeys.ALWAYS_RANDOMIZE_BLACK_CARDS) || ServerSettings.getSettings().getBoolean(ServerSettings.SettingsKeys.INFINITE_MODE)) {
            NOT_AVAILABLE_BLACK_CARDS.add(card);
            AVAILABLE_BLACK_CARDS.remove(cardNumber);
        }
        return card;
    }
    private void restoreAvailableCards() {
        restoreAvailableWhiteCards();
        restoreAvailableBlackCards();
    }
    private void restoreAvailableWhiteCards() {
        AVAILABLE_WHITE_CARDS.addAll(NOT_AVAILABLE_WHITE_CARDS);
        NOT_AVAILABLE_WHITE_CARDS.clear();
    }
    private void restoreAvailableBlackCards() {
        AVAILABLE_BLACK_CARDS.addAll(NOT_AVAILABLE_BLACK_CARDS);
        NOT_AVAILABLE_BLACK_CARDS.clear();
    }
    private int randomBetween(final int max) { // 0 Included to Max excluded
        return RANDOMIZER.nextInt(max);
    }

    // Instance
    private static CardsManager INSTANCE = null;
    public static void reloadInstance() {
        if (INSTANCE == null) INSTANCE = new CardsManager();
        else getInstance().restoreAvailableCards();
    }
    @NotNull
    public static CardsManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardsManager();
        }
        return INSTANCE;
    }
}
