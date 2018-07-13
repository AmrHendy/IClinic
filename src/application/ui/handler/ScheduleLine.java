package application.ui.handler;

public class ScheduleLine {
    private String time;
    private String patientName;
    private String patientNumber;
    private String phoneNumber;

    public ScheduleLine(String time, String patientName, String patientNumber, String phoneNumber){
        this.time = time;
        this.patientName = patientName;
        this.patientNumber = patientNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
