package main.java.beans;

public class User {
    private int userID;
    private byte[] encryptedPassword;
    private byte[] salt;
    private String userName;

    public User(){

    }

    // GETTERS
    public int getUserID() {
        return userID;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getUserName() {
        return userName;
    }


    // SETTERS
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEncryptedPassword(byte[] encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return  "userId: " + userID + "\n"
                + "userName: " + userName + "\n"
                + "encryptedPassword: " + encryptedPassword + "\n"
                + "salt: " + salt + "\n";
    }
}
