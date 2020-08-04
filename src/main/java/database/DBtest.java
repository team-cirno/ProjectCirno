package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class DBtest {
    public static void main(String[] args) {

        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "nekohub.me" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("neko", "test",
                "Nekohub!".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("test");
        System.out.println("Credentials ::"+ credential);

    }
}
