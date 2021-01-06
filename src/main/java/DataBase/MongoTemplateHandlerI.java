package DataBase;

import Model.Template;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoTemplateHandlerI implements IDatabaseHandler<Template> {
    private MongoDB mongo;

    public MongoTemplateHandlerI(IDatabase mongo){
        this.mongo = (MongoDB) mongo;
    }

    public List<Template> GET(Map<String, String> parameters) {
        BasicDBObject mongoQuery = new BasicDBObject(parameters);
        List<Template> templates = new ArrayList<>();
        List<Document> documents;

        if(mongoQuery.containsKey("templateID")) {
            String id = parameters.get("templateID");
            mongoQuery.remove("templateID");
            mongoQuery.put("_id",  new ObjectId(id));
        }

        if(mongoQuery.isEmpty())
            documents = mongo.getDatabase().getCollection("Templates").find().into(new ArrayList<>());
        else
            documents = mongo.getDatabase().getCollection("Templates").find(mongoQuery).into(new ArrayList<>());

        for (Document document : documents)
            templates.add(new Template(
                    document.get("_id").toString(),
                    (String) document.get("Type"),
                    (String) document.get("Contents")));

        return templates;
    }

    public Template POST(Template template) {
        MongoDatabase mongoDatabase = mongo.getDatabase();
        Document newDocument = new Document();
        newDocument.append("_id", new ObjectId()).append("Type", template.getType()).append("Contents", template.getContent());
        mongoDatabase.getCollection("Templates").insertOne(newDocument);
        template.setID(newDocument.get("_id").toString());
        return template;
    }

    public Template PUT(Template template) {
        if (ObjectId.isValid(template.getID())) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.append("_id", new ObjectId(template.getID()));
            BasicDBObject mongoQuery = new BasicDBObject();
            mongoQuery.put("$set", new BasicDBObject().append("Type", template.getType())
                    .append("Contents", template.getContent()));

            mongo.getDatabase().getCollection("Templates").updateOne(searchQuery, mongoQuery);
            return template;
        }
        return null;
    }

    public void DELETE(ObjectId ID) {
        mongo.getDatabase().getCollection("Templates").deleteOne(new Document("_id", ID));
    }
}
