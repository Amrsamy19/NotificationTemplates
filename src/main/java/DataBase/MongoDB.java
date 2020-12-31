package DataBase;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDB implements IDatabase {
    private static MongoDatabase mongoDatabase;
    private static MongoCollection<Document> mongoCollection;

    public void connectToDB(){
        String databaseURI = "mongodb+srv://root:root@notificationtemplates.0zhql.mongodb.net/Notifications?retryWrites=true&w=majority";
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

        MongoClient mongoClient = MongoClients.create(databaseURI);
        mongoDatabase = mongoClient.getDatabase("Notifications");
        mongoCollection = mongoDatabase.getCollection("Templates");
    }

    public MongoDatabase getDatabase(){
        return mongoDatabase;
    }

    public MongoCollection<Document> getCollection(){
        return mongoCollection;
    }

    public Boolean exists(String templateID) {
        if (ObjectId.isValid(templateID)) {
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("_id", new ObjectId(templateID));

            return this.getCollection().countDocuments(mongoQuery) == 1;
        } else return false;
    }
}
