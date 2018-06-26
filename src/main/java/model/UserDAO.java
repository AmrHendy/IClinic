package main.java.model;

import main.java.beans.User;
import main.java.services.PasswordEncryptionService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean login(String userName, String password) {
        boolean status = false;
        PasswordEncryptionService pw = new PasswordEncryptionService();
        ResultSet rs = null;
        try {
            String query = " SELECT password, salt from User where userName = " + "'" + userName + "'" + ";";
            rs = ModelManager.getInstance().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            while(rs.next()) {
                try {
                    if(pw.authenticate(password, rs.getBytes("password"), rs.getBytes("salt"))) {
                        System.out.println("CORRECT PASSWORD");
                        status = true;
                    } else {
                        System.out.println("WRONG PASSWORD: ");
                        status = false;
                    }
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException e) {
                    e.printStackTrace();
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }


    public static boolean register(User user) {
        boolean status = false;

        // check if the email is already registered.
        ResultSet rs = null;
        try {
            rs = ModelManager.getInstance().executeQuery(
                    " SELECT * FROM User WHERE userName = " + "'" + user.getUserName() + "'" + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            if (!rs.isBeforeFirst()) {
                String query = "INSERT INTO User (userName , password , salt) VALUES ( "
                                + "'" + user.getUserName() + "'"
                                + "'" + user.getEncryptedPassword() + "'"
                                + "'" + user.getSalt() + "'"
                                + " );";


                if (ModelManager.getInstance().executeUpdateQuery(query)) {
                    status = true;
                }
            } else {
                // this userName is taken before
                status = false;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static User getUser(String userName) {
        User user = new User();

        String query = "SELECT * FROM User WHERE userName = '" + userName + "';";
        ResultSet rs = null;
        try {
            rs = ModelManager.getInstance().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if (rs.next()) {
                user.setUserID(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setEncryptedPassword(rs.getBytes("password"));
                user.setSalt(rs.getBytes("salt"));
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
