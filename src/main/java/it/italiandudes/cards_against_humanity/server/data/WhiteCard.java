package it.italiandudes.cards_against_humanity.server.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public final class WhiteCard {

    // Attributes
    @NotNull private final String content;
    private final boolean isBlank;

    // Constructors
    public WhiteCard(@NotNull final String content, final boolean isBlank) {
        super();
        this.content = content;
        this.isBlank = isBlank;
    }
    public WhiteCard(@NotNull final String content) {
        this(content, false);
    }

    // Methods
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
        if (!(o instanceof WhiteCard)) return false;

        WhiteCard whiteCard = (WhiteCard) o;

        if (isBlank() != whiteCard.isBlank()) return false;
        return getContent().equals(whiteCard.getContent());
    }
    @Override
    public int hashCode() {
        int result = getContent().hashCode();
        result = 31 * result + (isBlank() ? 1 : 0);
        return result;
    }
    @Override @NotNull
    public String toString() {
        return content;
    }
    @NotNull
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object;
    }
}
