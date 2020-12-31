package org.Software;

import DataBase.IDatabase;
import DataBase.MongoDB;
import DataBase.MongoNotificationHandlerI;
import DataValidation.BaseValidator;
import DataValidation.IValidator;
import Model.Notification;
import Model.Template;
import Service.NotificationOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {
    private IDatabase mongo = new MongoDB();
    private MongoNotificationHandlerI mongoNotificationHandlerI;
    private NotificationOperation notificationOperation;

    public NotificationResource(){
        mongo.connectToDB();
        mongoNotificationHandlerI = new MongoNotificationHandlerI(mongo);
        notificationOperation = new NotificationOperation(mongoNotificationHandlerI);
    }

    @GET
    @Path("/get")
    public Object getNotification(@QueryParam("templateID") String ID) {
        Object object = notificationOperation.readNotification(ID);
        if(object == null)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}";
        return object;
    }

    @GET
    @Path("/getAll")
    public List<Object> getAllNotifications() {
        return notificationOperation.readAllNotifications();
    }

    @POST
    @Path("/create")
    public Object createNotification(Notification notification){
        String errorMessage;
        IValidator<Notification> notificationValidator = new BaseValidator<>();
        boolean exist = mongo.exists(notification.getTemplateID());

        if(!exist)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}";

        errorMessage = notificationValidator.isValid(notificationValidator.validate(notification));
        if(errorMessage == null)
            return notificationOperation.createNotification(notification);
        return errorMessage;
    }

    @PUT
    @Path("/{notificationID}")
    public Object updateNotification(@PathParam("notificationID") String ID, Notification notification){
        notification.setID(ID);
        String errorMessage;
        IValidator<Notification> notificationIValidator = new BaseValidator<>();
        boolean exist = mongo.exists(notification.getTemplateID());

        if(!exist)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}";

        errorMessage = notificationIValidator.isValid(notificationIValidator.validate(notification));
        if(errorMessage == null)
            return notificationOperation.updateNotification(notification);
        return errorMessage;
    }

    @DELETE
    @Path("/delete")
    public void deleteNotification(@QueryParam("notificationID") String ID){
        notificationOperation.deleteNotification(ID);
    }
}
