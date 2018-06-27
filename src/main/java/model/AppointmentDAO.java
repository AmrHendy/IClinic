package main.java.model;

import main.java.beans.Appointment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentDAO {

    public static ArrayList<Appointment> findByPatientID(int patientID){
        String query = "SELECT * FROM Appointment WHERE patientId = " + patientID + " ;";
        ArrayList<Appointment> matched = new ArrayList<>();
        try{
            ResultSet resultSet = ModelManager.getInstance().executeQuery(query);
            while (resultSet.next()) {
                matched.add(buildAppointment(resultSet));
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return matched;
    }


    public static ArrayList<Appointment> findByDate(Date date){
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        String formattedDate = dt.format(date);
        String query = "SELECT * FROM Appointment WHERE date = '" + formattedDate + "' ;";
        ArrayList<Appointment> matched = new ArrayList<>();
        try{
            ResultSet resultSet = ModelManager.getInstance().executeQuery(query);
            while (resultSet.next()) {
                matched.add(buildAppointment(resultSet));
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return matched;
    }

    public static boolean deleteAllAppointmentByPatientID(int patientID){
        String query = "DELETE FROM Appointment WHERE patientId = " + patientID + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean addAppointment(Appointment app){
        String query = "INSERT INTO Appointment (patientId, date, paidCost, finished, image, comment) VALUES ( "
                        + app.getPatientID() + " , " + "'" + app.getDateString() + "'" + " , "
                        + app.getPaidCost() + " , " + (app.isFinished() ? 1 : 0) + " , "
                        + "'" + app.getImageBytes() + "'" + " , " + "'" + app.getComment() + "'" + " );" ;
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    private static Appointment buildAppointment(ResultSet rs){
        Appointment app = new Appointment();
        try {
            app.setAppointmentID(rs.getInt("id"));
            app.setPatientID(rs.getInt("patientId"));

            app.setDate(rs.getString("date"));

            app.setPaidCost(rs.getInt("paidCost"));
            app.setFinished(rs.getBoolean("finished"));

            app.setImage(rs.getBytes("image"));

            app.setComment(rs.getString("comment"));
            return app;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
