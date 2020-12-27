package Service;

import DataBase.MongoTemplateHandlerI;
import Model.Template;
import org.bson.types.ObjectId;
import java.util.List;

public class TemplateOperation {
    private MongoTemplateHandlerI mongo;

    public TemplateOperation(MongoTemplateHandlerI mongo){
        this.mongo =  mongo;
    }

    public List<Object> readAllTemplates(){
        return mongo.GET();
    }

    public Object readTemplate(String ID) {
        return mongo.GET(ID);
    }

    public Object createTemplate(Template template){
        return mongo.POST(template);
    }

    public Object updateTemplate(Template template){
        return mongo.PUT(template);
    }

    public void deleteTemplate(String ID){
        if(ObjectId.isValid(ID))
            mongo.DELETE(new ObjectId(ID));
    }
}
