package nl.fd.data.entity

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.reflect.Type

class DateTimeDeserializer : JsonDeserializer<DateTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DateTime {
        if (json.isJsonPrimitive) {
            val asEpoch = json.asLong
            return DateTime(asEpoch, DateTimeZone.forID("Europe/Amsterdam"))
        }
        throw JsonParseException("Expected integer to represent a DateTime")
    }
}