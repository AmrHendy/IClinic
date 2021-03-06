package main.java.model;

import main.java.beans.Patient;
import main.java.beans.UserSignedInData;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientDAO {

    public static boolean addPatient(Patient newPatient) {
        try {
            PreparedStatement ps = ModelManager.getInstance().getConnection().prepareStatement(
                    "INSERT INTO Patient (Patient.name, address, birthdate, remainingCost, mobile_number, file_number, clinic_number) VALUES ( ?, ?, ?, ?, ?, ?, ? );");
            ps.setString(1, newPatient.getPatientName());
            ps.setString(2, newPatient.getAddress());
            if(newPatient.getDateString().isEmpty()){
                ps.setDate(3, null);
            }
            else{
                ps.setString(3, newPatient.getDateString());
            }
            ps.setInt(4, Integer.parseInt(newPatient.getRemainingCost()));
            ps.setString(5, newPatient.getPhoneNumber());
            ps.setString(6, newPatient.getFile_number());
            ps.setString(7, newPatient.getClinic_number());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updatePatient(String oldFileNumber, Patient newPatient){
        String query = "UPDATE Patient SET "
                        + "name = '" + newPatient.getPatientName() + "' , "
                        + "address = '" + newPatient.getAddress() + "' , "
                        + (newPatient.getDateString().isEmpty() ? "" : "birthdate = '" + newPatient.getDateString() + "' , ")
                        + "remainingCost = " + Integer.parseInt(newPatient.getRemainingCost()) + " , "
                        + "mobile_number = '" + newPatient.getPhoneNumber() + "' , "
                        + "file_number = '" + newPatient.getFile_number() + "' , "
                        + "clinic_number = '" + newPatient.getClinic_number() + "'"
                        + " WHERE file_number = '" + oldFileNumber + "' AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "' ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    public static ArrayList<Patient> findByID(int id){
        if(id == -1)return new ArrayList<Patient>();
        String query = "SELECT * FROM Patient WHERE id = " + id +";";
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

    public static ArrayList<Patient> findByFileNumberLike(String file_number){
        String query = "SELECT * FROM Patient WHERE file_number LIKE '" + file_number + "%' AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "' ;";
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

    public static ArrayList<Patient> findByNameLike(String patientName){
        String query = "SELECT * FROM Patient WHERE name LIKE '%" + patientName + "%' AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "' ;";
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

    public static Patient findByFileNumber(String file_number){
        // we will use the current logged in user to get the clinic number and use it in the where condition
        String query = "SELECT * FROM Patient WHERE file_number = '" + file_number + "' AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "' ;";
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
        return matched.isEmpty() ? null : matched.get(0);
    }


    public static ArrayList<Patient> findByName(String name){
       return findByName(name, -1);
    }

    // use this for lazy loading if you want
    public static ArrayList<Patient> findByName(String name, int limit){
        String query = "SELECT * FROM Patient WHERE name = " + "'" + name + "' AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "'";
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
        String query = "DELETE FROM Patient WHERE id = " + id + " AND clinic_number = '" + UserSignedInData.user.getClinicNumber() + "' ;";
        return ModelManager.getInstance().executeUpdateQuery(query);
    }

    private static Patient buildPatient(ResultSet rs){
        Patient patient = new Patient();
        try {
            patient.setPatientID(rs.getInt("id"));
            patient.setPatientName(rs.getString("name"));
            patient.setAddress(rs.getString("address"));
            patient.setBirthdate(rs.getString("birthdate"));
            patient.setRemainingCost(rs.getInt("remainingCost"));
            patient.setPhoneNumber(rs.getString("mobile_number"));
            patient.setFile_number(rs.getString("file_number"));
            patient.setClinic_number(rs.getString("clinic_number"));
            return patient;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}