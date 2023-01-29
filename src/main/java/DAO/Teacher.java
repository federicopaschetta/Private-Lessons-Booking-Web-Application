package DAO;

public class Teacher {
    private String Name;
    private String Surname;
    private int ID;

    private int State;

    public Teacher(String name, String surname) {
        this.Name = name;
        this.Surname = surname;
        this.State = 1;
    }

    public Teacher(String name, String surname, int id) {
        this.Name = name;
        this.Surname = surname;
        this.ID = id;
        this.State = 1;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
