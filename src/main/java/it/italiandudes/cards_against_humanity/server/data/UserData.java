package it.italiandudes.cards_against_humanity.server.data;

import it.italiandudes.cards_against_humanity.server.utils.ServerSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class UserData {

    // Attributes
    @NotNull private final String username;
    @NotNull private final ArrayList<@NotNull WhiteCard> whiteCards = new ArrayList<>();
    private int wins;

    // Constructors
    public UserData(@NotNull final UserConnection userConnection) {
        this.username = userConnection.getUsername();
        this.wins = 0;
    }

    // Methods
    @NotNull
    public ArrayList<@NotNull WhiteCard> getWhiteCards() {
        return whiteCards;
    }
    public int getMissingWhiteCardsAmount() {
        return ServerSettings.getSettings().getInt(ServerSettings.SettingsKeys.WHITE_CARDS) - whiteCards.size();
    }
    @NotNull
    public String getUsername() {
        return username;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public void addWin() {
        wins++;
    }
    public void resetWins() {
        wins = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;

        UserData userData = (UserData) o;

        if (getWins() != userData.getWins()) return false;
        if (!getUsername().equals(userData.getUsername())) return false;
        return getWhiteCards().equals(userData.getWhiteCards());
    }
    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getWhiteCards().hashCode();
        result = 31 * result + getWins();
        return result;
    }
    @Override @NotNull
    public String toString() {
        return username;
    }
}
