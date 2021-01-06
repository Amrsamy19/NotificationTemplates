package DataBase;

import Model.Notification;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoNotificationHandlerI implements IDatabaseHandler<Notification> {
    private MongoDB mongo;

    public MongoNotificationHandlerI(IDatabase mongo){
        this.mongo = (MongoDB) mongo;
    }

    public List<Notification> GET(Map<String, String> parameters){
        BasicDBObject mongoQuery = new BasicDBObject(parameters);
        List<Notification> notifications = new ArrayList<>();
        List<Document> documents;

        if(mongoQuery.containsKey("notificationID")){
            String id = parameters.get("notificationID");
            mongoQuery.remove("notificationID");
            mongoQuery.put("_id", new ObjectId(id));
        }

        if(mongoQuery.isEmpty())
            documents = mongo.getDatabase().getCollection("Notification").find().into(new ArrayList<>());
        else
            documents = mongo.getDatabase().getCollection("Notification").find(mongoQuery).into(new ArrayList<>());

        for (Document document : documents)
            notifications.add(new Notification(
                    document.get("_id").toString(),
                    (String) document.get("templateID"),
                    (String) document.get("target"),
                    (String) document.get("type"),
                    (String) document.get("status"),
                    (Map<String, String>) document.get("parameters"))
            );
        return notifications;
    }

    public Notification POST(Notification notification){
        Document newDocument = new Document();
        newDocument.append("_id", new ObjectId())
                .append("templateID", notification.getTemplateID())
                .append("target", notification.getTarget())
                .append("type", notification.getType())
                .append("status", "PENDING")
                .append("parameters", notification.getParameters());
        mongo.getDatabase().getCollection("Notification").insertOne(newDocument);
        notification.setID(newDocument.get("_id").toString());
        notification.setStatus("PENDING");
        return notification;
    }

    public Notification PUT(Notification notification) {
        if (ObjectId.isValid(notification.getID())) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.append("_id", new ObjectId(notification.getID()));
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("$set", new BasicDBObject()
                    .append("templateID", notification.getTemplateID())
                    .append("target", notification.getTarget())
                    .append("type", notification.getType())
                    .append("status", notification.getStatus())
                    .append("parameters", notification.getParameters()));

            mongo.getDatabase().getCollection("Notification").updateOne(searchQuery, mongoQuery);
            return notification;
        }
        return null;
    }

    public void DELETE(ObjectId ID){
        mongo.getDatabase().getCollection("Notification").deleteOne(new Document("_id", ID));
    }
}
