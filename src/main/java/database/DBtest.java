package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class DBtest {
    public static MongoCollection<Document> DBInit() {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "nekohub.me" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("xiao", "admin",
                "pass".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("admin");
        System.out.println("Credentials ::"+ credential);
        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("user");
        System.out.println("Collection sampleCollection selected successfully");
        return collection;
    }
}
