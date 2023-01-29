package DAO;

public class Booking {

    private int ID;
    private int course;
    private int teacher;
    private int user;
    private String day;
    private String time;
    private final String state;

    public Booking(int course, int teacher, int user, String day, String time) {
        this.course = course;
        this.teacher = teacher;
        this.user = user;
        this.day = day;
        this.time = time;
        this.state = "attiva";
    }

    public Booking(int ID, int course, int teacher, int user, String day, String time, String state) {
        this.ID = ID;
        this.course = course;
        this.teacher = teacher;
        this.user = user;
        this.day = day;
        this.time = time;
        this.state = state;
    }

    public int getID() {
        return this.ID;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() { return state; }
}
