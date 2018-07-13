package application.ui.handler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeGenerator {
    private final int dayPeriods = 24;


    public ObservableList<ScheduleLine> getDayTimeEmpty(){
        ObservableList<ScheduleLine> list = FXCollections.observableArrayList();
        String time = "11:30";
        for(int i = 0 ;i < dayPeriods; i++){
            time = getNextPeriod(30, time);
            ScheduleLine scheduleLine = new ScheduleLine(time, "", "", "");
            list.add(scheduleLine);
        }
        return list;
    }


    public String getNextPeriod(int interval, String startTime){
        String newTime = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date date = df.parse(startTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, interval);
            newTime = df.format(cal.getTime());
        }catch (Exception e){
            //TODO:: ADD log4j jar here.
            e.printStackTrace();
        }
        return  newTime;
    }
}
