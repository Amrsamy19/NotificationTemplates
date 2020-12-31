package DataBase;

public interface IDatabase<T> {
    void connectToDB();
    T getCollection();
    T getDatabase();
    Boolean exists(String ID);
}
