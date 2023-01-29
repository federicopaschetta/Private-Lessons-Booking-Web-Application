package DAO;

public class Course {
    private int ID;
    private String Title;
    private final int State;
    public Course(int ID, String title) {
        this.ID=ID;
        this.Title=title;
        this.State = 1;
    }

    public Course(String title) {
        Title = title;
        State = 1;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
