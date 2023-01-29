package DAO;

public class Teaching {
    private int teacher;
    private int course;

    private int State;
    public Teaching(int teacher, int course) {
        this.teacher = teacher;
        this.course = course;
        this.State = 1;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}


