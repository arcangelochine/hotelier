package server.database;

import server.util.JsonFactory;
import server.util.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonTable<T> {
    private static final Logger logger = Logger.getInstance();

    private final String fileName;
    private final JsonFactory<T> json;

    @SuppressWarnings("unchecked")
    public JsonTable(String _fileName) {
        fileName = _fileName;

        Type superclass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

        json = (JsonFactory<T>) JsonFactory.get(type);
    }

    public List<T> load() {
        try {
            List<T> data = json.loadList(fileName);
            logger.out("Loaded " + data.size() + " items from " + fileName);
            return data;
        } catch (Exception e) {
            logger.err("Failed to load items from " + fileName);
            return new ArrayList<>();
        }
    }

    public void save(List<T> data) {
        try {
            json.saveList(fileName, data);
            logger.out("Saved " + data.size() + " items to " + fileName);
        } catch (Exception e) {
            logger.err("Failed to save items to " + fileName);
        }
    }

    public String toJson(T obj) {
        return json.toJson(obj);
    }

    public T fromJson(String j) {
        return json.fromJson(j);
    }
}
