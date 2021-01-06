package Service;

import DataBase.MongoTemplateHandlerI;
import Model.Template;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Map;

public class TemplateOperation {
    private MongoTemplateHandlerI mongo;

    public TemplateOperation(MongoTemplateHandlerI mongo){
        this.mongo =  mongo;
    }

    public List<Template> readTemplate(Map<String, String> parameters) {
        return mongo.GET(parameters);
    }

    public Template createTemplate(Template template){
        return mongo.POST(template);
    }

    public Template updateTemplate(Template template){
        return mongo.PUT(template);
    }

    public void deleteTemplate(String ID){
        if(ObjectId.isValid(ID))
            mongo.DELETE(new ObjectId(ID));
    }
}
