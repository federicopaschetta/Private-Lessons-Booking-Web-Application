package DAO;

public class Calendar {
    private String Day;
    private String Time;

    public Calendar(String day, String time) {
        Day = day;
        Time = time;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
