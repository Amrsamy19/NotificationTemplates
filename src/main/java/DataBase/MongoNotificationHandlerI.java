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

    public List<Object> GET(){
        List<Object> notifications = new ArrayList<>();
        List<Document> documents = mongo.getDatabase().getCollection("Notification").find().into(new ArrayList<>());
        for(Document document : documents){
            notifications.add(new Notification(
                    document.get("_id").toString(),
                    (String) document.get("templateID"),
                    (String) document.get("target"),
                    (String) document.get("type"),
                    (Map<String, String>) document.get("parameters"))
                    );
        }
        return notifications;
    }

    public Object GET(String ID){
        if(ObjectId.isValid(ID)) {
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("_id", new ObjectId(ID));
            if (mongo.getDatabase().getCollection("Notification").countDocuments(mongoQuery) == 1) {
                Document document = mongo.getDatabase().getCollection("Notification").find(mongoQuery).into(new ArrayList<>()).get(0);
                return new Notification(
                        document.get("_id").toString(),
                        (String) document.get("templateID"),
                        (String) document.get("target"),
                        (String) document.get("type"),
                        (Map<String, String>) document.get("parameters")
                );
            } else return null;
        } else return null;
    }

    public Object POST(Notification notification){
        Document newDocument = new Document();
        newDocument.append("_id", new ObjectId())
                .append("templateID", notification.getTemplateID())
                .append("target", notification.getTarget())
                .append("type", notification.getType())
                .append("parameters", notification.getParameters());
        mongo.getDatabase().getCollection("Notification").insertOne(newDocument);
        notification.setID(newDocument.get("_id").toString());
        return notification;
    }

    public Object PUT(Notification notification) {
        if (ObjectId.isValid(notification.getID())) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.append("_id", new ObjectId(notification.getID()));
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("$set", new BasicDBObject()
                    .append("templateID", notification.getTemplateID())
                    .append("target", notification.getTarget())
                    .append("type", notification.getType())
                    .append("parameters", notification.getParameters()));

            mongo.getDatabase().getCollection("Notification").updateOne(searchQuery, mongoQuery);
            return GET(notification.getID());
        }
        return "{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Wrong ID\"}";
    }

    public void DELETE(ObjectId ID){
        mongo.getDatabase().getCollection("Notification").deleteOne(new Document("_id", ID));
    }
}
