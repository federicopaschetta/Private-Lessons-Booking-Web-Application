package DAO;

public class Lesson {
    private int ID;
    private String course;
    private String teacherName;
    private String teacherSurname;
    private String day;
    private String time;
    private String state;

    public Lesson(String course, String teacherName, String teacherSurname, String day, String time) {
        this.course = course;
        this.teacherName = teacherName;
        this.teacherSurname = teacherSurname;
        this.day = day;
        this.time = time;
    }

    public Lesson(int ID, String course, String teacherName, String teacherSurname, String day, String time, String state) {
        this.ID = ID;
        this.course = course;
        this.teacherName = teacherName;
        this.teacherSurname = teacherSurname;
        this.day = day;
        this.time = time;
        this.state = state;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSurname() {
        return teacherSurname;
    }

    public void setTeacherSurname(String teacherSurname) {
        this.teacherSurname = teacherSurname;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}


