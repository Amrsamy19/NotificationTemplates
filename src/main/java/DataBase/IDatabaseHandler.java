package DataBase;

import Model.Template;
import org.bson.types.ObjectId;

import java.util.List;

public interface IDatabaseHandler {
    List<Object> GET();
    Object GET(String ID);
    Object POST(Template template);
    Object PUT(Template template);
    void DELETE(ObjectId ID);
}
