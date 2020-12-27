package Service;

import DataBase.MongoNotificationHandlerI;
import Model.Notification;
import Model.Template;
import org.bson.types.ObjectId;

import java.util.List;

public class NotificationOperation {
    private MongoNotificationHandlerI mongo;

    public NotificationOperation(MongoNotificationHandlerI mongo){
        this.mongo =  mongo;
    }

    public List<Object> readAllNotifications(){
        return mongo.GET();
    }

    public Object readNotification(String ID) {
        return mongo.GET(ID);
    }

    public Object createNotification(Notification notification){
        return mongo.POST(notification);
    }

    public Object updateNotification(Notification notification){
        return mongo.PUT(notification);
    }

    public void deleteNotification(String ID){
        if(ObjectId.isValid(ID))
            mongo.DELETE(new ObjectId(ID));
    }
}
