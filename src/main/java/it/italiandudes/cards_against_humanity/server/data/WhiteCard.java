package it.italiandudes.cards_against_humanity.server.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public final class WhiteCard {

    // Attributes
    private final int id;
    @NotNull private final String content;
    private final boolean isBlank;

    // Constructors
    public WhiteCard(final int id, @NotNull final String content, final boolean isBlank) {
        super();
        this.id = id;
        this.content = content;
        this.isBlank = isBlank;
    }
    public WhiteCard(@NotNull final JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        this.content = json.getString("content");
        this.isBlank = json.getBoolean("is_blank");
    }

    // Methods
    public int getId() {
        return id;
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
        if (!(o instanceof WhiteCard)) return false;

        WhiteCard whiteCard = (WhiteCard) o;

        if (getId() != whiteCard.getId()) return false;
        if (isBlank() != whiteCard.isBlank()) return false;
        return getContent().equals(whiteCard.getContent());
    }
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getContent().hashCode();
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
        object.put("id", id);
        object.put("content", content);
        object.put("is_blank", isBlank);
        return object;
    }
}
