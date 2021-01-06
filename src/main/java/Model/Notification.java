package Model;

import DataBase.MongoDB;
import DataValidation.TargetConstraint;
import com.mongodb.BasicDBObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@TargetConstraint
public class Notification {
    private String ID;

    @NotBlank
    private String templateID;

    @NotBlank
    private String target;

    @NotBlank
    @Pattern(regexp = "^(SMS|MAIL)$", message = "Notification type must be either SMS or MAIL")
    private String type;

    private String status;
    private Map<@NotBlank String, @NotBlank String> parameters;

    public Notification(){
        parameters = new HashMap<>();
    }

    public Notification(@NotBlank String templateID, String target, String type, String status, Map<String, String> parameters) {
        this.templateID = templateID;
        this.target = target;
        this.type = type;
        this.status = status;
        this.parameters = parameters;
    }

    public Notification(String ID, @NotBlank String templateID, String target, String type, String status, Map<String, String> parameters) {
        this.ID = ID;
        this.templateID = templateID;
        this.target = target;
        this.type = type;
        this.status = status;
        this.parameters = parameters;
    }

    public String getID() {
        return ID;
    }

    public String getTemplateID() {
        return templateID;
    }

    public String getTarget() {
        return target;
    }

    public String getType(){
        return type;
    }

    public String getStatus(){
        return status;
    }

    public Map<String, String> getParameters(){
        return parameters;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setParameters(Map<String, String> parameters){
        this.parameters = parameters;
    }

    public boolean matchParameter(String templateID) {
        MongoDB mongoDB = new MongoDB();
        mongoDB.connectToDB();

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(templateID));
        Document document = mongoDB.getDatabase().getCollection("Templates").find(query).into(new ArrayList<>()).get(0);

        Template template = new Template((String) document.get("Type"), (String) document.get("Contents"));
        Set<String> templateParameters = template.getParameters();
        return templateParameters.equals(this.parameters.keySet());
    }
}
