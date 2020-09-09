package database;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import object.user.User;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class DBtest {
    public static MongoCollection<Document> DBInit() {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient( new MongoClientURI("mongodb://nekohub.me:27017"));

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("xiao", "admin",
                "pass".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("ProjectCirno");
        System.out.println("Credentials ::"+ credential);
        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("UserTable");
        System.out.println("Collection sampleCollection selected successfully");
        return collection;
    }

    public static void main(String[] args) {
        User tmp = DataBaseHandler.createUser("xiao", "admin",
                "pass", "admin",
                "pass");

        System.out.print(DataBaseHandler.getUser("admin"));
    }
}
