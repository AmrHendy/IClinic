package main.java.beans;

public class User {
    private int userID;
    private byte[] encryptedPassword;
    private byte[] salt;
    private String userName;
    private String clinic;

    public User(){
        this.clinic = "";
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
        return clinic;
    }

    public String getClinicNumber() {
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

    public void setClinic(String clinic) { this.clinic = clinic; }

    @Override
    public String toString() {
        return  "userId: " + userID + "\n"
                + "userName: " + userName + "\n"
                + "encryptedPassword: " + encryptedPassword + "\n"
                + "salt: " + salt + "\n";
    }

    public User clone() {
        User user = new User();
        user.setEncryptedPassword(getEncryptedPassword());
        user.setClinic(getClinicNumber());
        user.setUserName(getUserName());
        user.setSalt(getSalt());
        user.setUserID(getUserID());
        return user;
    }
}
