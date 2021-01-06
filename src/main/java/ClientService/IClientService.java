package ClientService;

import java.util.HashMap;

public interface IClientService<T> {
    void GET(HashMap<String, String> parameters);
    void POST(T object);
    void PUT(String ID, T object);
    void DELETE(String ID);
}
