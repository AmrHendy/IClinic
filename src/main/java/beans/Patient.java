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
    private Image image;
    private String address;
    private Date birthdate;
    private int remainingCost;
    private String mobileNumber;

    public Patient(){
        remainingCost = 0;
        image = null;
    }

    // Getters
    public int getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
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

    public String getAddress() {
        return address;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getDateString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        String formattedDate = dt.format(birthdate);
        return formattedDate;
    }

    public int getRemainingCost() {
        return remainingCost;
    }

    public String getMobileNumber() { return mobileNumber; }

    // Setters
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setBirthdate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
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

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", patientName='" + patientName + '\'' +
                ", image=" + image +
                ", address='" + address + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", remainingCost=" + remainingCost +
                '}';
    }
}
