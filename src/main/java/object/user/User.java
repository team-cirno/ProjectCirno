package object.user;

import java.util.UUID;

public class User {

    String firstName;
    String lastName;
    String nickName;

    String userName;
    String eMail;
    UUID uuid;

    public User(String firstName, String lastName, String nickName, String userName, String eMail, UUID uuid) {
        System.out.println("Strat to creat user");
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.userName = userName;
        this.eMail = eMail;
        this.uuid = uuid;
    }

    public static User createUser(String firstName, String lastName, String nickName, String userName, String eMail){
        return new User(firstName, lastName, nickName, userName, eMail, UUID.randomUUID());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserName() {
        return userName;
    }

    public String geteMail() {
        return eMail;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String toJson(){
        return "";
    }
}
