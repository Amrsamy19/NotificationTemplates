package DataBase;

import Model.Template;
import org.bson.types.ObjectId;

import java.util.List;

public interface IDatabaseHandler<T> {
    List<Object> GET();
    Object GET(String ID);
    Object POST(T object);
    Object PUT(T object);
    void DELETE(ObjectId ID);
}
