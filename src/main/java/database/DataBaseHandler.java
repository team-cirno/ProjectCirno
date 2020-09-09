package database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import object.user.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBaseHandler {

    public static User createUser(String firstName, String lastName, String nickName, String userName, String eMail){
        Document document1 = new Document("firstName",firstName)
                .append("lastName", lastName)
                .append("nickName",nickName)
                .append("userName", userName)
                .append("uuid", UUID.randomUUID().toString())
                .append("eMail", eMail);
        List<Document> list = new ArrayList<Document>();
        list.add(document1);
        MongoCollection<Document> collection=DBtest.DBInit();
        collection.insertMany(list);
        return new User(firstName, lastName, nickName, userName, eMail, UUID.randomUUID().toString());
    }

    public static User getUser(String name){
        MongoCollection<Document> collection=DBtest.DBInit();
        BasicDBObject Query=new BasicDBObject();
        Query.put("userName",name);
        FindIterable<Document> findIterable = collection.find(Query);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        Document studentDocument = mongoCursor.tryNext();
        System.out.println(studentDocument);
        return new User(studentDocument.getString("firstName"),
                studentDocument.getString("firstName"),
                studentDocument.getString("firstName"),
                studentDocument.getString("userName"),
                studentDocument.getString("eMail"),
                studentDocument.getString("uuid"));
    }

}
