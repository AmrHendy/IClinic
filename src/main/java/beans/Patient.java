package main.java.beans;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Patient {
    private int patientID;
    private String patientName;
    private String address;
    private Date birthdate;
    private int remainingCost;
    private String phoneNumber;
    private String file_number;
    private int clinic_number;

    public Patient(){
        remainingCost = 0;
    }

    public String getFile_number() {
        return file_number;
    }

    public void setFile_number(String file_number) {
        this.file_number = file_number;
    }

    public int getClinic_number() {
        return clinic_number;
    }

    public void setClinic_number(int clinic_number) {
        this.clinic_number = clinic_number;
    }


    // Getters
    public int getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return getDateString();
    }

    public String getDateString() {
        if(birthdate == null)return "";
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dt.format(birthdate);
        return formattedDate;
    }

    public Date getDate(){
        return birthdate;
    }

    public String getRemainingCost() {
        return String.valueOf(remainingCost);
    }

    public String getPhoneNumber() { return phoneNumber; }

    // Setters
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setBirthdate(String date) {
        //TODO:: check this
        if(date == null || date.isEmpty()){
            this.birthdate = null;
            return;
        }
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date formattedDate = null;
        try {
            formattedDate = dt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.birthdate = formattedDate;
    }

    public void setRemainingCost(int remainingCost) {
        this.remainingCost = remainingCost;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected Patient clone() throws CloneNotSupportedException {
        Patient patient = new Patient();
        patient.setFile_number(this.getFile_number());
        patient.setRemainingCost(Integer.parseInt(this.getRemainingCost()));
        patient.setClinic_number(this.getClinic_number());
        patient.setBirthdate(this.getBirthdate());
        patient.setAddress(this.getAddress());
        patient.setPatientID(this.getPatientID());
        patient.setPatientName(this.getPatientName());
        patient.setPhoneNumber(this.getPhoneNumber());
        return patient;
    }
}
