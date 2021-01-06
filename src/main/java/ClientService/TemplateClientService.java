package ClientService;

import Model.Template;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateClientService implements IClientService<Template> {
    private WebTarget baseTarget;

    private WebTarget prepareURI(HashMap<String, String> parameters) {
        WebTarget requestedTarget = baseTarget.path("get");

        for (Map.Entry<String, String> entry : parameters.entrySet())
            requestedTarget = requestedTarget.queryParam(entry.getKey(), entry.getValue());

        return requestedTarget;
    }

    public TemplateClientService() {
        Client client = ClientBuilder.newClient();
        baseTarget = client.target("http://localhost:8080/notifications_war/webapi/template");
    }

    public void GET(HashMap<String, String> parameters) {
        ObjectMapper mapper = new ObjectMapper();
        WebTarget getTemplate = prepareURI(parameters);
        Response response = getTemplate.request(MediaType.APPLICATION_JSON).get();

        List jsonObjects = response.readEntity(List.class);
        List<Template> templates = mapper.convertValue(jsonObjects, new TypeReference<List<Template>>(){});

        if(!templates.isEmpty()){
            for(Template template : templates)
                System.out.println(template);
        } else
            System.out.println("Couldn't find the template");
    }

    public void POST(Template template) {
        Response response = baseTarget.request().post(Entity.json(template));

        Template createdTemplate = response.readEntity(Template.class);
        System.out.println(createdTemplate);
    }

    public void PUT(String ID, Template template) {
        Response response = baseTarget.path(ID).request().put(Entity.json(template));

        Template updatedTemplate = response.readEntity(Template.class);
        System.out.println(updatedTemplate);
    }

    public void DELETE(String ID) {
        WebTarget deleteTemplate = baseTarget.queryParam("templateID", ID);
        Response response = deleteTemplate.request().delete();
        if(response.getStatus() == 204)
            System.out.println("Notification deleted successfully");
        else
            System.out.println("Something bad happened");
    }
}
