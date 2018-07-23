package main.java.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.beans.Appointment;
import main.java.beans.Patient;
import main.java.beans.User;

import java.util.ArrayList;

public class UiUtil {

    private UiUtil(){


    }

    public static ObservableList<Patient> getPatientObservable(ArrayList<Patient> list){
        ObservableList<Patient> obs = FXCollections.observableArrayList();
        if(list == null || list.size() == 0){
            return obs;
        }
        obs.addAll(list);
        return obs;
    }

    public static ObservableList<User> getUserObservable(ArrayList<User> list){
        ObservableList<User> obs = FXCollections.observableArrayList();
        if(list == null || list.size() == 0){
            return obs;
        }
        obs.addAll(list);
        return obs;
    }

    public static ObservableList<Appointment> getAppointmentObservable(ArrayList<Appointment> list){
        ObservableList<Appointment> obs = FXCollections.observableArrayList();
        if(list == null || list.size() == 0){
            return obs;
        }
        obs.addAll(list);
        return obs;
    }

}
