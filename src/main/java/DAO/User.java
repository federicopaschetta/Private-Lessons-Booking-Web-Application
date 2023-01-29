package DAO;

public class User {
    private int ID;
    private String Name;
    private String Surname;
    private String Email;
    private String Username;
    private String Password;
    private String Role;

    public User(int ID, String name, String surname, String email, String username, String password, String role) {
        this.ID = ID;
        Name = name;
        Surname = surname;
        Email = email;
        Username = username;
        Password = password;
        Role = role;
    }

    public User(String name, String surname, String email, String username, String password, String role) {
        Name = name;
        Surname = surname;
        Email = email;
        Username = username;
        Password = password;
        Role = role;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {return Username;}

    public void setUsername(String username) {
        Username = username;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
