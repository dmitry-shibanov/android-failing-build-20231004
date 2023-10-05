package nl.fd.data.entity;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return LocalDate.fromDateFields(df.parse(json.getAsString()));
        } catch (ParseException e) {
            throw new JsonParseException("Expected date format 'yyyy-MM-dd'");
        }
    }
}
