package main.java.beans;

import java.awt.*;

public class Patient {
    int patientID;
    String patientName;
    Image image;
    String address;
    String birthdate;
    int remainingCost;

    public Patient(){

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

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getRemainingCost() {
        return remainingCost;
    }


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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setRemainingCost(int remainingCost) {
        this.remainingCost = remainingCost;
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
