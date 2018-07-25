package main.java.beans;

public class User {
    private int userID;
    private byte[] encryptedPassword;
    private byte[] salt;
    private String userName;
    private int clinic;

    public User(){
        this.clinic = -1;
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

    public String getClinic() {
        return String.valueOf(clinic);
    }
    public int getClinicNumber() {
        return clinic;
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

    public void setClinic(int clinic) { this.clinic = clinic; }

    @Override
    public String toString() {
        return  "userId: " + userID + "\n"
                + "userName: " + userName + "\n"
                + "encryptedPassword: " + encryptedPassword + "\n"
                + "salt: " + salt + "\n";
    }
}
