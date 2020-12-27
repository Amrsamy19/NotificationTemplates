package Model;

import DataValidation.TargetConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@TargetConstraint
public class Notification {
    private String ID;
    @NotBlank
    private String templateID;
    @NotBlank
    private String target;
    @NotBlank
    private String type;
    private Map<@NotBlank String, @NotBlank String> parameters;

    public Notification(){
        parameters = new HashMap<>();
    }

    public Notification(String ID, @NotBlank String templateID, String target, String type, Map<String, String> parameters) {
        this.ID = ID;
        this.templateID = templateID;
        this.target = target;
        this.type = type;
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

    public void setParameters(Map<String, String> parameters){
        this.parameters = parameters;
    }
}