package server.database;

import server.util.JsonFactory;
import server.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class JsonTable<T> {
    private static final Logger logger = Logger.getInstance();

    private final String fileName;
    private final JsonFactory<T> json = new JsonFactory<>();

    public JsonTable(String _fileName) {
        fileName = _fileName;
    }

    public List<T> load() {
        try {
            List<T> data = json.loadList(fileName);
            logger.out("Loaded " + data.size() + " items from " + fileName);
            return data;
        } catch (Exception ignored) {
            logger.err("Failed to load items from " + fileName);
            return new ArrayList<>();
        }
    }

    public void save(List<T> data) {
        try {
            json.saveList(fileName, data);
            logger.out("Saved " + data.size() + " items to " + fileName);
        } catch (Exception ignored) {
            logger.err("Failed to save items to " + fileName);
        }
    }
}
