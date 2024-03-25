package it.italiandudes.cards_against_humanity.server.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public final class BlackCard {

    // Attributes
    @NotNull private final String content;
    private final int emptyFields;

    // Constructors
    public BlackCard(@NotNull final String content, final int emptyFields) {
        this.content = content;
        this.emptyFields = emptyFields;
    }

    // Methods
    @NotNull
    public String getContent() {
        return content;
    }
    public int getEmptyFields() {
        return emptyFields;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackCard)) return false;

        BlackCard card = (BlackCard) o;

        if (getEmptyFields() != card.getEmptyFields()) return false;
        return getContent().equals(card.getContent());
    }
    @Override
    public int hashCode() {
        int result = getContent().hashCode();
        result = 31 * result + getEmptyFields();
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
        object.put("empty_fields", emptyFields);
        return object;
    }
}
