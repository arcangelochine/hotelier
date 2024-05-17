package client.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("date");
        jsonWriter.value(localDateTime.format(FORMATTER));
        jsonWriter.endObject();
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        LocalDateTime localDateTime = null;

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("date")) {
                String dateStr = jsonReader.nextString();
                localDateTime = LocalDateTime.parse(dateStr, FORMATTER);
            } else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();
        return localDateTime;
    }
}
