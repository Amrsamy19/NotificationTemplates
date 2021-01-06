package org.Software;

import DataBase.IDatabase;
import DataBase.MongoDB;
import DataBase.MongoNotificationHandlerI;
import DataValidation.BaseValidator;
import DataValidation.IValidator;
import Model.Notification;
import Service.NotificationOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {
    private IDatabase mongo = new MongoDB();
    private NotificationOperation notificationOperation;
    private IValidator<Notification> notificationValidator;

    public NotificationResource(){
        mongo.connectToDB();
        MongoNotificationHandlerI mongoNotificationHandlerI = new MongoNotificationHandlerI(mongo);
        notificationOperation = new NotificationOperation(mongoNotificationHandlerI);
        notificationValidator = new BaseValidator<>();
    }

    @GET
    @Path("/get")
    public Response getNotification(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        Map<String, String> parameters = new HashMap<>();

        for(String string : queryParameters.keySet())
            parameters.put(string, queryParameters.getFirst(string));

        return Response
                .status(Response.Status.OK)
                .entity(notificationOperation.readNotification(parameters))
                .build();
    }

    @POST
    public Response createNotification(Notification notification) throws URISyntaxException {
        String errorMessage = notificationValidator.isValid(notificationValidator.validate(notification));
        if(errorMessage != null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();

        boolean exist = mongo.exists(notification.getTemplateID());
        if(!exist)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}")
                    .build();

        boolean parametersMatch = notification.matchParameter(notification.getTemplateID());
        if(!parametersMatch)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Parameters in the notification and template doesn't match\"}")
                    .build();

        return Response
                .status(Response.Status.OK)
                .entity(notificationOperation.createNotification(notification))
                .build();
    }

    @PUT
    @Path("/{notificationID}")
    public Response updateNotification(@PathParam("notificationID") String ID, Notification notification) throws URISyntaxException {
        notification.setID(ID);
        String errorMessage = notificationValidator.isValid(notificationValidator.validate(notification));
        if(errorMessage != null)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();

        boolean exist = mongo.exists(notification.getTemplateID());
        if(!exist)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"ErrorCode\":\"404\",\"ErrorMessage\":\"Template is not found\"}")
                    .build();

        boolean parametersMatch = notification.matchParameter(notification.getTemplateID());
        if(!parametersMatch)
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Parameters in the notification and template doesn't match\"}")
                    .build();

        return Response
                .status(Response.Status.OK)
                .entity(notificationOperation.updateNotification(notification))
                .build();
    }

    @DELETE
    public void deleteNotification(@QueryParam("notificationID") String ID){
        notificationOperation.deleteNotification(ID);
    }
}
