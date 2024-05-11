package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public final class JsonFactory<T> {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Type type = new TypeToken<T>() {}.getType();
    private final Type typeList = new TypeToken<List<T>>() {}.getType();

    public T load(String fileName) throws Exception {
        JsonReader reader = new JsonReader(new FileReader(fileName));

        return gson.fromJson(reader, type);
    }

    public List<T> loadList(String fileName) throws Exception {
        JsonReader reader = new JsonReader(new FileReader(fileName));

        return gson.fromJson(reader, typeList);
    }

    public void save(String fileName, T object) throws Exception {
        JsonWriter writer = new JsonWriter(new FileWriter(fileName));

        gson.toJson(object, type, writer);
        writer.close();
    }

    public void saveList(String fileName, List<T> objects) throws Exception {
        JsonWriter writer = new JsonWriter(new FileWriter(fileName));

        gson.toJson(objects, typeList, writer);
        writer.close();
    }
}
