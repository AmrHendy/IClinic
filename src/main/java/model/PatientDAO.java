package main.java.model;

import main.java.beans.Appointment;
import main.java.beans.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientDAO {

    public static boolean addPatient(Patient newPatient){
        String query = "INSERT INTO Patient (id, name, image, address, birthdate, remainingCost, mobile_number) VALUES ( "
                + newPatient.getPatientID() + " , " + "'" + newPatient.getPatientName() + "'" + " , "
                + "'" + newPatient.getImageBytes() + "'" + " , " + "'" + newPatient.getAddress() + "'" + " , "
                + "'" + newPatient.getDateString() + "'" + " , " + newPatient.getRemainingCost()
                + "'" + newPatient.getMobileNumber() + "'" + ");";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static boolean updatePatient(int id, Patient newPatient){
        String query = "UPDATE Patient SET "
                        + "name = '" + newPatient.getPatientName() + "' , "
                        + "image = '" + newPatient.getImageBytes() + "' , "
                        + "address = '" + newPatient.getAddress() + "' , "
                        + "birthdate = '" + newPatient.getDateString() + "' , "
                        + "remainingCost = " + newPatient.getRemainingCost() + " , "
                        + "mobile_number = '" + newPatient.getMobileNumber() + "'"
                        + " WHERE id = " + id + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static ArrayList<Patient> findByID(int id){
        String query = "SELECT * FROM Patient WHERE id = " + id + " ;";
        ArrayList<Patient> matched = new ArrayList<>();
        try{
            ResultSet resultSet = ModelManager.getInstance().executeQuery(query);
            while (resultSet.next()) {
                matched.add(buildPatient(resultSet));
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return matched;
    }

    public static ArrayList<Patient> findByName(String name){
       return findByName(name, -1);
    }

    public static ArrayList<Patient> findByName(String name, int limit){
        String query = "SELECT * FROM Patient WHERE name = " + "'" + name + "'";
        if(limit != -1){
            query += " LIMIT " + limit;
        }
        query += ";";

        ArrayList<Patient> matched = new ArrayList<>();
        try{
            ResultSet resultSet = ModelManager.getInstance().executeQuery(query);
            while (resultSet.next()) {
                matched.add(buildPatient(resultSet));
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return matched;
    }

    public static boolean deletePatient(int id){
        AppointmentDAO.deleteAllAppointmentByPatientID(id);
        String query = "DELETE FROM Patient WHERE id = " + id + " ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    private static Patient buildPatient(ResultSet rs){
        Patient patient = new Patient();
        try {
            patient.setPatientID(rs.getInt("id"));
            patient.setPatientName(rs.getString("name"));
            patient.setImage(rs.getBytes("image"));
            patient.setAddress(rs.getString("address"));
            patient.setBirthdate(rs.getString("birthdate"));
            patient.setRemainingCost(rs.getInt("remainingCost"));
            patient.setMobileNumber(rs.getString("mobile_number"));
            return patient;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
