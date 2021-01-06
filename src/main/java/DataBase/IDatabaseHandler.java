package DataBase;

import Model.Template;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface IDatabaseHandler<T> {
    List<T> GET(Map<String, String> parameters);
    T POST(T object);
    T PUT(T object);
    void DELETE(ObjectId ID);
}
