package server.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonFactory<T> {
    private static final Gson gson = new Gson();

    private final Type type;
    private final Type typeList;

    protected JsonFactory() {
        Type superclass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        ListType listType = new ListType(type);

        this.type = TypeToken.get(type).getType();
        this.typeList = TypeToken.get(listType).getType();
    }

    private JsonFactory(Type type) {
        ListType tempList = new ListType(type);

        this.type = TypeToken.get(type).getType();
        this.typeList = TypeToken.get(tempList).getType();
    }

    public static JsonFactory<?> get(Type type) {
        return new JsonFactory<>(type);
    }

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
        writer.setIndent("    ");

        gson.toJson(object, type, writer);
        writer.close();
    }

    public void saveList(String fileName, List<T> objects) throws Exception {
        JsonWriter writer = new JsonWriter(new FileWriter(fileName));
        writer.setIndent("    ");

        gson.toJson(objects, typeList, writer);
        writer.close();
    }

    private static final class ListType implements ParameterizedType {
        private final Type type;

        public ListType(Type _type) {
            type = _type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
