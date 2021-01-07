package ClientService;

import java.util.HashMap;
import java.util.List;

public interface IClientService<T> {
    List<T> GET(HashMap<String, String> parameters);
    void POST(T object);
    void PUT(String ID, T object);
    void DELETE(String ID);
}
