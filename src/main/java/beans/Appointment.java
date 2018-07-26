package main.java.beans;

import main.java.model.PatientDAO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Appointment {
    //TODO:: add patient name to appointment object
    private int appointmentID;
    private int patientID;
    private Date date;
    private Boolean finished;
    private Image image;
    private int paidCost;
    private String comment;
    private boolean confirmed_paid;

    private String patientName;
    private String patientFileID;
    private int clinicNumber;
    private Date dateOnly;
    private Date timeOnly;
    private String phoneNumber;

    public Appointment(){
        finished = false;
        image = null;
        paidCost = 0;
        comment = "";
        confirmed_paid = false;
        patientID = -1;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public Date getDate() {
        return date;
    }


    public String getDateString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = dt.format(date);
        return formattedDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date formattedDate = null;
        try {
            formattedDate = dt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = formattedDate;
    }


    public boolean isConfirmedPaid() {
        return confirmed_paid;
    }

    public void setConfirmedPaid(boolean confirmed_paid) {
        this.confirmed_paid = confirmed_paid;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Image getImage() {
        return image;
    }

    public byte[] getImageBytes() {
        if(image == null)return null;
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write((BufferedImage)image, "jpg", baos);
            baos.flush();
            bytes = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImage(byte[] bytes) {
        InputStream in = new ByteArrayInputStream(bytes);
        try {
            this.image = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPaidCost() {
        return String.valueOf(paidCost);
    }

    public void setPaidCost(int paidCost) {
        this.paidCost = paidCost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateOnly(){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dt.format(date);
        return formattedDate;
    }

    public String getTimeOnly(){
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm");
        String formattedDate = dt.format(date);
        return formattedDate;
    }

    public Patient getPatient(){
        ArrayList<Patient> arr = PatientDAO.findByID(patientID);
        if(arr.isEmpty()){
            return null;
        }
        return arr.get(0);
    }

    public String getPatientName(){
        if(patientID == -1) return "";
        return getPatient().getPatientName();
    }

    public String getPatientFileID() {
        if(patientID == -1) return "";
        return getPatient().getFile_number();
    }

    public String getPhoneNumber(){
        if(patientID == -1) return "";
        return getPatient().getPhoneNumber();
    }

    public String getClinicNumber() {
        if(patientID == -1) return "";
        return String.valueOf(getPatient().getClinic_number());
    }

    public void setClinicNumber(int clinicNumber){
        this.clinicNumber = clinicNumber;
    }
}
