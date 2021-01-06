package Service;

import DataBase.MongoNotificationHandlerI;
import Model.Notification;
import Model.Template;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public class NotificationOperation {
    private MongoNotificationHandlerI mongo;

    public NotificationOperation(MongoNotificationHandlerI mongo){
        this.mongo =  mongo;
    }

    public List<Notification> readNotification(Map<String, String> parameters) {
        return mongo.GET(parameters);
    }

    public Notification createNotification(Notification notification){
        return mongo.POST(notification);
    }

    public Notification updateNotification(Notification notification){
        return mongo.PUT(notification);
    }

    public void deleteNotification(String ID){
        if(ObjectId.isValid(ID))
            mongo.DELETE(new ObjectId(ID));
    }
}
