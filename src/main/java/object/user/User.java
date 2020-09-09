package object.user;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.Filters;
import logger.Logger;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import java.util.UUID;
import database.DBtest;
import org.bson.conversions.Bson;

public class User {

    String firstName;
    String lastName;
    String nickName;

    String userName;
    String eMail;
    String uuid;

    public User(String firstName, String lastName, String nickName, String userName, String eMail, String uuid) {
        System.out.println("Strat to creat user");
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.userName = userName;
        this.eMail = eMail;
        this.uuid = uuid;

    }

    public  String getFirstName() {
        return this.firstName= firstName;
    }


    public String getLastName() {
       return this.lastName=lastName;
    }

    public String getNickName() {
       return this.nickName=nickName;
    }

    public String getUserName() {
      return this.userName=userName;
    }

    public String geteMail() {
       return this.eMail=eMail;
    }

    public String getUuid() {
        return uuid;
    }

    public static void setFirstName(String firstName, String name) {
        MongoCollection<Document> collection=DBtest.DBInit();
        BasicDBObject query= new BasicDBObject();
        query.put("userName",name);
        BasicDBObject newDocument= new BasicDBObject();
        newDocument.put("firstName",firstName);
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set",newDocument);
        collection.updateOne(query,updateObject);
        String update=firstName;
        //this.firstName = firstName;
    }

    public void setLastName(String lastName, String name) {
        MongoCollection<Document> collection=DBtest.DBInit();
        BasicDBObject query= new BasicDBObject();
        query.put("userName",name);
        BasicDBObject newDocument= new BasicDBObject();
        newDocument.put("lastName",lastName);
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set",newDocument);
        collection.updateOne(query,updateObject);
        String update=firstName;
        this.lastName = lastName;
    }

    public void setNickName(String nickName,String name) {
        MongoCollection<Document> collection=DBtest.DBInit();
        BasicDBObject query= new BasicDBObject();
        query.put("userName",name);
        BasicDBObject newDocument= new BasicDBObject();
        newDocument.put("lastName",lastName);
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set",newDocument);
        collection.updateOne(query,updateObject);
        String update=firstName;
        this.nickName = nickName;
    }

    public String toJson(){
        return "";
    }
}
