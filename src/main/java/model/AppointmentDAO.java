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
import java.util.Calendar;
import java.util.Date;

public class AppointmentDAO {

    public static boolean addAppointment(Appointment app){
        String query = "INSERT INTO Appointment (patientId, date, paidCost, finished, image, comment, confirmed_paid) VALUES ( "
                + app.getPatientID() + " , " + "'" + app.getDateString() + "'" + " , "
                + app.getPaidCost() + " , " + (app.isFinished() ? 1 : 0) + " , "
                + "'" + app.getImageBytes() + "'" + " , " + "'" + app.getComment() + "'" + " , "
                + (app.isConfirmedPaid() ? 1 : 0) + " );" ;
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean deleteAppointmentByID(int id){
        String query = "DELETE FROM Appointment WHERE id = " + id + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean deleteAllAppointmentByPatientID(int patientID){
        String query = "DELETE FROM Appointment WHERE patientId = " + patientID + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

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
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
        String formattedDate = dt.format(date);
        String query = "SELECT * FROM Appointment WHERE CAST(Appointment.date as date) = '" + formattedDate + "'" + " ORDER BY date ;";
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

        int index = 0;
        ArrayList<Appointment> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 12, 0);
        for(int i = 0; i <= 24; i++){
            Appointment app = new Appointment();
            app.setDate(calendar.getTime());
            calendar.add(Calendar.MINUTE, 30);
            if(index < matched.size() && matched.get(index).getDateString() == app.getDateString()){
                result.add(matched.get(index));
                index++;
            }
            else{
                result.add(app);
            }
        }
        return result;
    }

    // id can not be changed by user (read only ==> fix it in gui)
    public static boolean updateAppointmentByID(int id, Appointment newAppointment){
        boolean delete = deleteAppointmentByID(id);
        newAppointment.setAppointmentID(id);
        boolean add = addAppointment(newAppointment);
        return delete && add;
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

            app.setConfirmedPaid(rs.getBoolean("confirmed_paid"));
            return app;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
