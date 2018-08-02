package main.java.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.beans.User;
import main.java.beans.UserSignedInData;
import main.java.services.PasswordEncryptionService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDAO {

    public static boolean login(String userName, String password) {
        boolean status = false;
        PasswordEncryptionService pw = new PasswordEncryptionService();
        ResultSet rs = null;
            try {
            rs = ModelManager.getInstance().executeQuery("SELECT password, salt from User where userName = " + "'" + userName + "'" + ";");
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
                        UserSignedInData.user = getUser(userName);
                } else {
                    System.out.println("WRONG PASSWORD");
                    status = false;
                    UserSignedInData.user = null;
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
                PreparedStatement pst = ModelManager.getInstance().getConnection().prepareStatement("INSERT INTO User (userName, password, salt, clinic) VALUES ( " + "? , ? , ? , ?)");
                pst.setString(1, user.getUserName());
                pst.setBytes(2, user.getEncryptedPassword());
                pst.setBytes(3, user.getSalt());
                pst.setInt(4, user.getClinicNumber());
                System.out.println("Salt1 = " + Arrays.toString(user.getSalt()));
                System.out.println("Encr1 = " + Arrays.toString(user.getEncryptedPassword()));
                if (pst.executeUpdate() == 1) {
                    status = true;
                }
            } else {
                status = false;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean updateUser(int id, User newUser) {
        try {
            PreparedStatement pst = ModelManager.getInstance().getConnection().prepareStatement(
                    "UPDATE User SET userName = ?, password = ?, clinic = ? WHERE id = ?;");
            pst.setString(1, newUser.getUserName());
            pst.setBytes(2, newUser.getEncryptedPassword());
            pst.setInt(3, newUser.getClinicNumber());
            pst.setInt(4, id);

            if (pst.executeUpdate() == 1) {
                return true;
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static User getUser(String userName) {
        User user = null;
        System.out.println("name = " + userName);
        String query = "SELECT * FROM User WHERE userName LIKE '%" + userName + "%';";
        ResultSet rs = null;
        try {
            rs = ModelManager.getInstance().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setEncryptedPassword(rs.getBytes("password"));
                user.setSalt(rs.getBytes("salt"));
                user.setClinic(rs.getInt("clinic"));
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<User> findByName(String userName) {
        ArrayList<User> matched = new ArrayList<>();
        String query = "SELECT * FROM User WHERE userName LIKE '%" + userName + "%';";
        try{
            ResultSet rs = ModelManager.getInstance().executeQuery(query);
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setEncryptedPassword(rs.getBytes("password"));
                user.setSalt(rs.getBytes("salt"));
                user.setClinic(rs.getInt("clinic"));
                matched.add(user);
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return matched;
    }

    public static boolean deleteUser(String userName){
        String query = "DELETE FROM User WHERE userName = '" + userName + "' ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static ObservableList<String> getClinics(){
         String query = "SELECT DISTINCT clinic FROM User";
         ObservableList<String> result = FXCollections.observableArrayList();
         try{
            ResultSet resultSet = ModelManager.getInstance().executeQuery(query);
            while (resultSet.next()) {
                result.add(resultSet.getString("clinic"));
            }
                resultSet.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            return result;
    }

    public static boolean registerAdmin(){
        if(getUser("admin") != null)return false;
        User user = new User();
        PasswordEncryptionService pw = new PasswordEncryptionService();
        try {
            byte[] salt = pw.generateSalt();
            user.setSalt(salt);
            user.setUserName("admin");
            user.setClinic(0);
            user.setEncryptedPassword(pw.getEncryptedPassword("admin", salt));
            return register(user);
        } catch(Exception e){
            return false;
        }
    }
}
