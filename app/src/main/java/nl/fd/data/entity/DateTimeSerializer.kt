package nl.fd.data.entity

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.joda.time.DateTime
import java.lang.reflect.Type

class DateTimeSerializer : JsonSerializer<DateTime> {
    @Throws(JsonParseException::class)
    override fun serialize(
        dateTime: DateTime,
        typeOfT: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(dateTime.millis, Long::class.java)
    }
}