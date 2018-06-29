package main.java.model;

import java.sql.*;

public class ModelManager {
    private static String url = "jdbc:mysql://127.0.0.1:3306/iclinic?autoReconnect=true&useSSL=false";
    private static String user = "root";
    private static String pass = "root";
    private static ModelManager model;
    private Connection connection;

    private ModelManager() {
        startConnection();
    }

    public static synchronized ModelManager getInstance()
    {
        if (model == null) {
            model = new ModelManager();
        }
        return model;
    }

    public void startConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
        return statement.getResultSet();
    }

    public boolean executeUpdateQuery(String query){
       try {
            PreparedStatement pst = ModelManager.getInstance().getConnection().prepareStatement(query);
            if (pst.executeUpdate() == 1) {
                return true;
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}