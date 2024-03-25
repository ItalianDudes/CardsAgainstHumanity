package it.italiandudes.cards_against_humanity.client.javafx.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

public class WhiteCardFX extends Group {

    // Attributes
    @NotNull private final Rectangle card;
    @NotNull private final String content;
    private final boolean isBlank;

    // Constructors
    public WhiteCardFX(@NotNull final String content, final boolean isBlank) {
        super();
        this.content = content;
        this.isBlank = isBlank;
        card = new Rectangle();
        card.setStyle("-fx-border-radius: 2px;");
        card.setFill(Color.WHITE);
        card.setStroke(Color.BLACK);
    }
    public WhiteCardFX(@NotNull final String content) {
        this(content, false);
    }

    // Methods
    @NotNull
    public Rectangle getCard() {
        return card;
    }
    @NotNull
    public String getContent() {
        return content;
    }
    public boolean isBlank() {
        return isBlank;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteCardFX)) return false;

        WhiteCardFX whiteCard = (WhiteCardFX) o;

        if (isBlank() != whiteCard.isBlank()) return false;
        if (!getCard().equals(whiteCard.getCard())) return false;
        return getContent().equals(whiteCard.getContent());
    }
    @Override
    public int hashCode() {
        int result = getCard().hashCode();
        result = 31 * result + getContent().hashCode();
        result = 31 * result + (isBlank() ? 1 : 0);
        return result;
    }
    @Override @NotNull
    public String toString() {
        return content;
    }
}
