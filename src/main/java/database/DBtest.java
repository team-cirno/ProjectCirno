package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DBtest {
    public static void main(String[] args) {


        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://neko:<password>@cluster0.8fyld.mongodb.net/<dbname>?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("test");
    }
}