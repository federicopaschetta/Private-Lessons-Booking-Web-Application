package DAO;

import java.util.ArrayList;

public interface DAOInterface {

    public boolean insertCourse(Course c);
    public boolean removeCourse(Course c);
    public ArrayList<Course> getCourses();
    public ArrayList<Course> getCourses(Teacher t);
    public Course getCourse(int ID);
    public String getCourseTitle(int ID);
    public int getCourseID(String title);
    public ArrayList<Teacher> getTeachersforCourse(Course c);

    public boolean insertTeacher(Teacher t);
    public boolean removeTeacher(Teacher t);
    public ArrayList<Teacher> getTeachers();
    public ArrayList<Teacher> getTeachers(Course c);
    public Teacher getTeacher(int ID);
    public Teacher getTeacher(String Nome, String Cognome);
    public int getTeacherID(String Nome, String Cognome);
    public ArrayList<Course> getCoursesforTeacher(Teacher t);

    public boolean insertUser(User u);
    public boolean removeUser(User u);
    public ArrayList<User> getUsers();
    public User getUser(String Username);
    public String getUserRole(String username, String password);
    public int getUserID(String username);

    public boolean insertTeaching(Teaching t);
    public boolean removeTeaching(Teaching t);
    public ArrayList<Teaching> getTeachings(Course c);
    public ArrayList<Teaching> getTeachings(Teacher t);
    public ArrayList<Teaching> getTeachings();
    public Teaching getTeaching(int Docente, int Corso);

    public boolean insertBooking(Booking b);
    public boolean removeBooking(Booking l);
    public ArrayList<Booking> getBookings();
    public ArrayList<Booking> getUserBookings(User u);
    public ArrayList<Booking> getUserBookings(String username);

    public Booking getBooking(int ID);
    public Booking getBooking(int course, int teacher, int user, String day, String time);
    public boolean updateBooking(Booking b, String state);
    public boolean insertCalendar(Calendar c);
    public ArrayList<Calendar> freeSlots(Teaching t);
}
