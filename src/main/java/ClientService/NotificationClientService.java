package ClientService;

import Model.Notification;
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

public class NotificationClientService implements IClientService<Notification> {
    private WebTarget baseTarget;

    private WebTarget prepareURI(HashMap<String, String> parameters) {
        WebTarget requestedTarget = baseTarget.path("get");

        for (Map.Entry<String, String> entry : parameters.entrySet())
            requestedTarget = requestedTarget.queryParam(entry.getKey(), entry.getValue());

        return requestedTarget;
    }

    public NotificationClientService() {
        Client client = ClientBuilder.newClient();
        baseTarget = client.target("http://localhost:8080/notifications_war/webapi/notifications");
    }

    public List<Notification> GET(HashMap<String, String> parameters) {
        ObjectMapper mapper = new ObjectMapper();
        WebTarget getAllNotifications = prepareURI(parameters);
        Response response = getAllNotifications.request(MediaType.APPLICATION_JSON).get();

        List jsonObjects = response.readEntity(List.class);
        List<Notification> notifications = mapper.convertValue(jsonObjects, new TypeReference<List<Notification>>(){});

        if(notifications.isEmpty()) {
            System.out.println("Couldn't find the notification");
        }
        return notifications;
    }

    public void POST(Notification notification) {
        WebTarget createNotification = baseTarget.path("create");
        Response response = createNotification.request().post(Entity.json(notification));

        Notification createdNotification = response.readEntity(Notification.class);
        System.out.println(createdNotification);
    }

    public void PUT(String ID, Notification notification) {
        Response response = baseTarget.path(ID).request().put(Entity.json(notification));

        Notification updatedNotification = response.readEntity(Notification.class);
        System.out.println(updatedNotification);
    }

    public void DELETE(String ID) {
        WebTarget deleteNotification = baseTarget.path("delete").queryParam("notificationID", ID);
        Response response = deleteNotification.request().delete();
        if(response.getStatus() == 204)
            System.out.println("Notification deleted successfully");
        else
            System.out.println("Something bad happened");
    }
}
