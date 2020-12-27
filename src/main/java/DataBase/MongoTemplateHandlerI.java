package DataBase;

import Model.Template;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class MongoDBHandlerI implements IDatabaseHandler {
    private MongoDB mongo;

    public MongoDBHandlerI(IDatabase mongo){
        this.mongo = (MongoDB) mongo;
    }

    public List<Object> GET(){
        List<Object> templates = new ArrayList<>();
        List<Document> documents = mongo.getDatabase().getCollection("Templates").find().into(new ArrayList<Document>());
        for(Document document : documents){
            templates.add(new Template(document.get("_id").toString(),
                    (String) document.get("Type"),
                    (String) document.get("Contents")));
        }
        return templates;
    }

    public Object GET(String ID){
        if(ObjectId.isValid(ID)) {
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("_id", new ObjectId(ID));
            if (mongo.getCollection().countDocuments(mongoQuery) == 1) {
                Document document = mongo.getCollection().find(mongoQuery).into(new ArrayList<Document>()).get(0);
                return new Template(document.get("_id").toString(), (String) document.get("Type"), (String) document.get("Contents"));
            } else return null;
        } else return null;
    }

    public Object POST(Template template){
        MongoDatabase mongoDatabase = mongo.getDatabase();
        Document newDocument = new Document();
        newDocument.append("_id", new ObjectId()).append("Type", template.getType()).append("Contents", template.getContent());
        mongoDatabase.getCollection("Templates").insertOne(newDocument);
        template.setID(newDocument.get("_id").toString());
        return template;
    }

    public Object PUT(Template template) {
        if (ObjectId.isValid(template.getID())) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.append("_id", new ObjectId(template.getID()));
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("$set", new BasicDBObject().append("Type", template.getType())
                    .append("Contents", template.getContent()));

            mongo.getCollection().updateOne(searchQuery, mongoQuery);
            return GET(template.getID());
        }
        return "{\"ErrorCode\":\"400\",\"ErrorMessage\":\"Wrong ID\"}";
    }

    public void DELETE(ObjectId ID){
        mongo.getCollection().deleteOne(new Document("_id", ID));
    }
}
