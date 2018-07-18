package main.java.validator;

import main.java.beans.Patient;
import main.java.beans.User;

import java.util.ArrayList;

public class PatientValidator {

    public static ArrayList<String> insertingValidator(Patient patient){
        ArrayList<String> msgs = new ArrayList<>();
        if(patient.getPatientName() == null){
            msgs.add("لا توجد قيمة لاسم المريض");
        }
        if(patient.getClinic_number() == -1){
            msgs.add("لا توجد قيمة لرقم العيادة");
        }
        //TODO:: add phoneNumber in patient
        if(patient.getPhoneNumber() == null){
            msgs.add("لا توجد قيمة صحيحة لرقم الهاتف");
        }

        return msgs;
    }
}
