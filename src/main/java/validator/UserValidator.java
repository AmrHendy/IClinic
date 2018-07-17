package main.java.validator;

import main.java.beans.User;

import java.util.ArrayList;

public class UserValidator {

    private UserValidator(){

    }

    public static ArrayList<String> insertingValidator(User user){
        ArrayList<String> msgs = new ArrayList<String>();
        if(user.getUserName() != null){
            msgs.add("لا يوجد قيمة لاسم المستخدم");
        }
        if(user.getClinic() != -1){
            msgs.add("لا توجد قيمة لرقم العيادة");
        }
        return msgs;
    }


}
