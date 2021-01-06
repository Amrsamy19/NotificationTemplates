package DataBase;

public interface IDatabase<T> {
    void connectToDB();
    T getDatabase();
    Boolean exists(String ID);
}
