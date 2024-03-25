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
import java.util.*;

public final class CardsManager {

    // Static
    private static final Random RANDOMIZER = new Random();

    // Attributes
    @NotNull private final ArrayList<@NotNull WhiteCard> WHITE_CARDS = new ArrayList<>();
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
                WHITE_CARDS.add(new WhiteCard(result.getString("content"), result.getInt("is_blank") != 0));
            }
            ps.close();
            query = "SELECT * FROM black_cards;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("Prepared statement is null");
            result = ps.executeQuery();
            while (result.next()) {
                AVAILABLE_BLACK_CARDS.add(new BlackCard(result.getString("content"), result.getInt("empty_fields")));
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
        if (amount <= 0 || amount > WHITE_CARDS.size()) return null;
        if (amount == WHITE_CARDS.size()) {
            ArrayList<WhiteCard> clone = new ArrayList<>();
            Collections.copy(clone, new ArrayList<>(WHITE_CARDS));
            return clone;
        }
        ArrayList<Integer> cardsPool = new ArrayList<>();
        ArrayList<WhiteCard> cards = new ArrayList<>();
        for (int i=0; i<amount; i++) {
            int number;
            //noinspection StatementWithEmptyBody
            while (cardsPool.contains(number = randomBetween(WHITE_CARDS.size())));
            cards.add(WHITE_CARDS.get(number));
            cardsPool.add(number);
        }
        return cards;
    }
    @NotNull
    public BlackCard getRandomBlackCard() {
        int cardNumber = randomBetween(AVAILABLE_BLACK_CARDS.size());
        BlackCard card = AVAILABLE_BLACK_CARDS.get(cardNumber);
        NOT_AVAILABLE_BLACK_CARDS.add(card);
        AVAILABLE_BLACK_CARDS.remove(cardNumber);
        return card;
    }
    public void restoreAvailableBlackCards() {
        AVAILABLE_BLACK_CARDS.addAll(NOT_AVAILABLE_BLACK_CARDS);
        NOT_AVAILABLE_BLACK_CARDS.clear();
    }
    private int randomBetween(final int max) { // 0 Included to Max excluded
        return RANDOMIZER.nextInt(max);
    }

    // Instance
    private static CardsManager INSTANCE = null;
    public static void reloadInstance() {
        INSTANCE = new CardsManager();
    }
    @NotNull
    public static CardsManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardsManager();
        }
        return INSTANCE;
    }
}
