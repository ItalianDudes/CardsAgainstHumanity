package it.italiandudes.cards_against_humanity.server.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public final class BlackCard {

    // Attributes
    private final int id;
    @NotNull private final String content;
    private final int emptyFields;

    // Constructors
    public BlackCard(final int id, @NotNull final String content, final int emptyFields) {
        this.id = id;
        this.content = content;
        this.emptyFields = emptyFields;
    }
    public BlackCard(@NotNull final JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        this.content = json.getString("content");
        this.emptyFields = json.getInt("empty_fields");
    }

    // Methods
    public int getId() {
        return id;
    }
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

        BlackCard blackCard = (BlackCard) o;

        if (getId() != blackCard.getId()) return false;
        if (getEmptyFields() != blackCard.getEmptyFields()) return false;
        return getContent().equals(blackCard.getContent());
    }
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getContent().hashCode();
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
        object.put("id", id);
        object.put("content", content);
        object.put("empty_fields", emptyFields);
        return object;
    }
}
