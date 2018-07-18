package main.java.model;

import main.java.beans.Appointment;
import main.java.beans.Patient;
import main.java.beans.UserSignedInData;

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
        String query = "INSERT INTO Appointment (patientId, date, paidCost, finished, image, comment, confirmed_paid, clinic_number) VALUES ( "
                + app.getPatientID() + " , " + "'" + app.getDateString() + "'" + " , "
                + app.getPaidCost() + " , " + (app.isFinished() ? 1 : 0) + " , "
                + "'" + app.getImageBytes() + "'" + " , " + "'" + app.getComment() + "'" + " , "
                + (app.isConfirmedPaid() ? 1 : 0) + " , " + UserSignedInData.user.getClinic() + " );" ;
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean deleteAppointmentByID(int id){
        String query = "DELETE FROM Appointment WHERE id = " + id + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean deleteAllAppointmentByPatientID(int patientID){
        String query = "DELETE FROM Appointment WHERE patientId = " + patientID + " AND clinic_number = " + UserSignedInData.user.getClinic() + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static ArrayList<Appointment> findByPatientID(int patientID){
        String query = "SELECT * FROM Appointment WHERE patientId = " + patientID + " AND clinic_number = " + UserSignedInData.user.getClinic() + " ;";
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
        //TODO:: changed mm to MM as mm is the minutes and MM is the months.
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
        String formattedDate = dt.format(date);
        String query = "SELECT * FROM Appointment WHERE CAST(Appointment.date as date) = '" + formattedDate + "'"
                        + " AND clinic_number = " + UserSignedInData.user.getClinic() + " ORDER BY date ;";
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

    //TODO:: id can not be changed by user (read only ==> fix it in gui)
    public static boolean updateAppointmentByID(int id, Appointment newAppointment){
        boolean delete = deleteAppointmentByID(id);
        newAppointment.setAppointmentID(id);
        boolean add = addAppointment(newAppointment);
        return delete && add;
    }

    public static boolean editAppointmentList(String patientFileIDBefore, String patientFileIDAfter, Date appDate, int paidCost){
        if(patientFileIDBefore.isEmpty() && patientFileIDAfter.isEmpty()){
            return true;
        }

        if(patientFileIDBefore.isEmpty()){
            //insert
            Patient patient = PatientDAO.findByFileNumber(patientFileIDAfter);
            Appointment app = new Appointment();
            app.setPatientID(patient.getPatientID());
            app.setDate(appDate);
            app.setPaidCost(paidCost);
            //TODO add clinic numebr
            return AppointmentDAO.addAppointment(app);
        }
        else if(patientFileIDAfter.isEmpty()){
            //delete
            Patient patient = PatientDAO.findByFileNumber(patientFileIDBefore);
            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
            String formattedDate = dt.format(appDate);
            String query = "SELECT * FROM Appointment WHERE patientId = " + patient.getPatientID() + " AND date = '" + formattedDate
                            + "' AND clinic_number = " + UserSignedInData.user.getClinic() + " ;";
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
            return deleteAppointmentByID(matched.get(0).getAppointmentID());
        }
        else{
            //update
            Patient patient1 = PatientDAO.findByFileNumber(patientFileIDBefore);
            Patient patient2 = PatientDAO.findByFileNumber(patientFileIDAfter);
            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
            String formattedDate = dt.format(appDate);
            String query = "SELECT * FROM Appointment WHERE patientId = " + patient1.getPatientID() + " AND date = '" + formattedDate
                            + "' AND clinic_number = " + UserSignedInData.user.getClinic() + " ;";
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
            Appointment app = new Appointment();
            app.setPatientID(patient2.getPatientID());
            app.setDate(appDate);
            app.setPaidCost(paidCost);
            return updateAppointmentByID(matched.get(0).getAppointmentID(), app);
        }
    }

    public static boolean confirmPaidCost(String patientFileID, Date appDate){
        Patient patient = PatientDAO.findByFileNumber(patientFileID);
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        String formattedDate = dt.format(appDate);
        String query = "SELECT * FROM Appointment WHERE patientId = " + patient.getPatientID() + " AND date = '" + formattedDate
                + "' AND clinic_number = " + UserSignedInData.user.getClinic() + " ;";
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
        if(matched.isEmpty())return false;
        if(!matched.get(0).isConfirmedPaid()){
            matched.get(0).setConfirmedPaid(true);
            patient.setRemainingCost(Math.max(patient.getRemainingCost() - matched.get(0).getPaidCost(), 0));
            AppointmentDAO.updateAppointmentByID(matched.get(0).getAppointmentID(), matched.get(0));
            PatientDAO.updatePatient(patient.getFile_number(), patient);
        }
        return true;
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
            app.setClinicNumber(rs.getInt("clinic_number"));
            return app;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}