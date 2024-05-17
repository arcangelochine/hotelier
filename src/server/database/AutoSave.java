package server.database;

public class AutoSave implements Runnable {
    private static final Database database = Database.getInstance();

    @Override
    public void run() {
        database.commit();
    }
}
