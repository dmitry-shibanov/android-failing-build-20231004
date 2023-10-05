package nl.fd.data.entity.card;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import nl.fd.data.entity.card.teaser.Teaser;

@Slf4j
public class CardDeserializer implements JsonDeserializer<Card> {

    private static final String AD_UNIT = "AdUnit";

    private static final String TEASER = "Teaser";

    private static final String STOCK_TICKER = "StockTicker";

    private static final String NEWSLETTER_TEASER = "SubscribableNewsletter";

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Card result = null;
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement type = jsonObject.get("type");
        String typeString = Optional.ofNullable(type).map(JsonElement::getAsString).orElse(TEASER);
        switch (typeString) {
            case TEASER:
            default:
                result = context.deserialize(jsonObject, Teaser.class);
        }
        return result;
    }
}
