package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {
    private String _id;

    @NotNull
    @Size(min = 5, max = 100, message = "Size must be between 5 and 100")
    private String type;

    @NotNull
    @Size(min = 10,max = 1000, message = "Size must be between 10 and 1000")
    private String content;

    public Template(){}

    public Template(String type, String content){
        this.type = type;
        this.content = content;
    }

    public Template(String ID, String type, String content){
        this._id = ID;
        this.type = type;
        this.content = content;
    }

    public void setID(String ID){
        this._id = ID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getID(){
        return _id;
    }

    public String getType(){
        return type;
    }

    public String getContent(){
        return content;
    }

    @JsonIgnore
    public Set<String> getParameters(){
        Set<String> parameters = new HashSet<>();

        Pattern pattern = Pattern.compile("\\%\\%(.*?)\\%\\%");
        Matcher matcher = pattern.matcher(this.content);
        while(matcher.find()){
            parameters.add(matcher.group(1));
        }
        return parameters;
    }

    @Override
    public String toString() {
        return "_id = " + _id + "\nType = " + type + "\nContent = " + content + "\n";
    }
}
