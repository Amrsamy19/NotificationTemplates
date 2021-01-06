package org.Software;

import DataBase.IDatabase;
import DataBase.MongoDB;
import DataBase.MongoNotificationHandlerI;
import DataValidation.BaseValidator;
import DataValidation.IValidator;
import Model.Notification;
import Service.NotificationOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
    public Object getNotification(@QueryParam("notificationID") String ID) {
        Object object = notificationOperation.readNotification(ID);
        if(object == null)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Notification is not found\"}";
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
        IValidator<Notification> notificationValidator = new BaseValidator<>();
        String errorMessage = notificationValidator.isValid(notificationValidator.validate(notification));
        if(errorMessage != null)
            return errorMessage;

        boolean exist = mongo.exists(notification.getTemplateID());
        if(!exist)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}";

//        boolean parametersMatch = notification.matchParameter(notification.getTemplateID());
//        if(!parametersMatch)
//            return "{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Parameters in the notification and template doesn't match\"}";

        return notificationOperation.createNotification(notification);
    }

    @PUT
    @Path("/{notificationID}")
    public Object updateNotification(@PathParam("notificationID") String ID, Notification notification){
        notification.setID(ID);
        IValidator<Notification> notificationValidator = new BaseValidator<>();
        String errorMessage = notificationValidator.isValid(notificationValidator.validate(notification));
        if(errorMessage != null)
            return errorMessage;

        boolean exist = mongo.exists(notification.getTemplateID());
        if(!exist)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}";

//        boolean parametersMatch = notification.matchParameter(notification.getTemplateID());
//        if(!parametersMatch)
//            return "{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Parameters in the notification and template doesn't match\"}";

        return notificationOperation.updateNotification(notification);
    }

    @DELETE
    @Path("/delete")
    public void deleteNotification(@QueryParam("notificationID") String ID){
        notificationOperation.deleteNotification(ID);
    }
}
