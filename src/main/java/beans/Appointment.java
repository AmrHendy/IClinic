package main.java.beans;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private int appointmentID;
    private int patientID;
    private Date date;
    private Boolean finished;
    private Image image;
    private int paidCost;
    private String comment;
    private boolean confirmed_paid;

    public Appointment(){
        finished = false;
        image = null;
        paidCost = 0;
        comment = "";
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
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        String formattedDate = dt.format(date);
        return formattedDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
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

    public int getPaidCost() {
        return paidCost;
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
}
