package DAO;

import java.sql.*;
import java.util.ArrayList;

public class DAO implements DAOInterface {

    private final String url1;
    private final String user;
    private final String password;

    public DAO(String url1, String user, String password) {
        this.url1 = url1;
        this.user = user;
        this.password = password;
        registerDriver();
    }


    public void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private Connection openConnection() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn1;
    }

    private void closeConnection(Connection conn1) {
        try {
            if(conn1 != null) {
                conn1.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * COURSES
     */

    public boolean insertCourse(Course c) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("INSERT INTO corso VALUES (null, '" + c.getTitle() + "', 1)");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean removeCourse(Course c) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("UPDATE corso SET Stato=0 WHERE Titolo='"+c.getTitle()+"'");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public ArrayList<Course> getCourses() {
        Connection conn1 = null;
        ArrayList<Course> courseList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                courseList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM corso WHERE Stato=1");
                while(rs.next()) {
                    courseList.add(new Course(rs.getInt("ID"), rs.getString("Titolo")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return courseList;
    }

    public ArrayList<Course> getCourses(Teacher t) {
        Connection conn1 = null;
        ArrayList<Course> courseList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                courseList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT Corso FROM insegnamento WHERE Docente='"+t.getID()+"'");
                while(rs.next()) {
                    courseList.add(new Course(rs.getString("Corso")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return courseList;
    }

    public Course getCourse(int ID) {
        Connection conn1 = null;
        Course course = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM corso WHERE ID='"+ID+"' AND Stato=1");
                rs.next();
                course = new Course(rs.getInt("ID"), rs.getString("Titolo"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return course;
    }


    public String getCourseTitle(int ID) {
        Connection conn1 = null;
        String courseTitle = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT Titolo FROM corso WHERE ID='"+ID+"' AND Stato=1");
                rs.next();
                courseTitle = rs.getString("Titolo");
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return courseTitle;
    }

    public int getCourseID(String title) {
        Connection conn1 = null;
        int courseID = -1;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT ID FROM corso WHERE Titolo='"+title+"' AND Stato=1");
                rs.next();
                courseID = rs.getInt("ID");
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return courseID;
    }


    public ArrayList<Teacher> getTeachersforCourse(Course c) {
        Connection conn = null;
        ArrayList<Teacher> courseTeachersList = null;
        try {
            conn = openConnection();
            if(conn != null) {
                courseTeachersList = new ArrayList<>();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT d.ID, d.Nome, d.Cognome FROM insegnamento i JOIN docente d ON (i.Docente=d.ID) WHERE " +
                        "i.Corso='"+c.getID()+"' AND d.Stato=1");
                while (rs.next()) {
                    courseTeachersList.add(new Teacher(rs.getString("Nome"), rs.getString("Cognome"), rs.getInt("ID")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return courseTeachersList;
    }


    /*
     * TEACHER
     */

    public boolean insertTeacher(Teacher t) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("INSERT INTO docente VALUES ('" + t.getName() + "', '"+
                        t.getSurname() + "', '"+ t.getID()+"', '"+t.getState()+"')");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean removeTeacher(Teacher t) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;

        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("UPDATE docente SET Stato=0 WHERE ID='"+t.getID()+"'");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public ArrayList<Teacher> getTeachers() {
        Connection conn1 = null;
        ArrayList<Teacher> teacherList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                teacherList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM docente WHERE Stato=1");
                while(rs.next()) {
                    teacherList.add(new Teacher(rs.getString("Nome"),
                            rs.getString("Cognome"), rs.getInt("ID")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teacherList;
    }

    public ArrayList<Teacher> getTeachers(Course c) {
        Connection conn1 = null;
        ArrayList<Teacher> teacherList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                teacherList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT d.Nome, d.Cognome, d.ID FROM docente d " +
                        "JOIN insegnamento i WHERE d.Stato=1 AND i.Corso='"+c.getTitle()+"'");
                while(rs.next()) {
                    teacherList.add(new Teacher(rs.getString("Nome"),
                            rs.getString("Cognome"), rs.getInt("ID")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teacherList;
    }

    public Teacher getTeacher(int ID) {
        Connection conn1 = null;
        Teacher teacher = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM docente WHERE ID='"+ID+"'");
                rs.next();
                teacher = new Teacher(rs.getString("Nome"), rs.getString("Cognome"), rs.getInt("ID"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teacher;
    }

    public Teacher getTeacher(String Nome, String Cognome) {
        Connection conn1 = null;
        Teacher teacher = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM docente WHERE Nome='"+Nome+"' AND Cognome='"+Cognome+"'");
                rs.next();
                teacher = new Teacher(Nome, Cognome, rs.getInt("ID"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teacher;
    }

    public int getTeacherID(String Nome, String Cognome) {
        Connection conn1 = null;
        int teacherID = -1;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT ID FROM docente WHERE Nome='"+Nome+"' AND Cognome='"+Cognome+"'");
                rs.next();
                teacherID = rs.getInt("ID");
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teacherID;
    }

    public ArrayList<Course> getCoursesforTeacher(Teacher t) {
        Connection conn = null;
        ArrayList<Course> teacherCoursesList = null;
        try {
            conn = openConnection();
            if(conn != null) {
                teacherCoursesList = new ArrayList<>();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT c.ID, c.Titolo FROM insegnamento i JOIN corso c ON (i.Corso=c.ID) WHERE" +
                        "i.Docente='"+t.getID()+"' AND c.Stato=1");
                while (rs.next()) {
                    teacherCoursesList.add(new Course(rs.getInt("ID"), rs.getString("Titolo")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return teacherCoursesList;
    }

    /*
    USER
     */

    public boolean insertUser(User u) {
        /* Connection to database */
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                    result = st.executeUpdate("INSERT INTO utente VALUES ('" + u.getID() + "', '"+
                            u.getName() + "', '"+ u.getSurname()+"', '"+u.getEmail()+"', '"+u.getUsername()+"', '"+ u.getPassword()+
                            "', '"+ u.getRole()+"')");
                    st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean removeUser(User u) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("DELETE FROM utente WHERE ID='"+u.getID()+"'");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public ArrayList<User> getUsers() {
        Connection conn1 = null;
        ArrayList<User> userList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                userList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM utente");
                while(rs.next()) {
                    userList.add(new User(rs.getInt("ID"),
                            rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Email"),
                            rs.getString("Username"), rs.getString("Password"), rs.getString("Ruolo")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return userList;
    }

    public User getUser(String Username) {
        Connection conn1 = null;
        User user = null;
        try {
            conn1 = openConnection();
            if(conn1!=null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Utente WHERE Username='"+Username+"'");
                rs.next();
                user = new User(rs.getInt("ID"), rs.getString("Nome"), rs.getString("Cognome"),
                        rs.getString("Email"), rs.getString("Username"), rs.getString("Password"), rs.getString("Ruolo"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return user;
    }

    public String getUserRole(String username, String password) {
        Connection conn = null;
        String userRole = null;
        try {
            conn = openConnection();
            Statement st1 = conn.createStatement();
            ResultSet rs = st1.executeQuery("SELECT * FROM Utente WHERE (Username='"+username+"' AND Password='"+password+"')");
            if(rs.next()) {
                userRole =  rs.getString("Ruolo");
            } else {
                userRole = "ospite";
            }
            rs.close();
            st1.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return userRole;
    }

    public int getUserID(String Username) {
        Connection conn1 = null;
        int userID = -1;
        try {
            conn1 = openConnection();
            if(conn1!=null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT ID FROM Utente WHERE Username='"+Username+"'");
                rs.next();
                userID = rs.getInt("ID");
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return userID;
    }

    /*
     TEACHING
     */

    public boolean insertTeaching(Teaching t) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("INSERT INTO insegnamento VALUES (NULL, "+t.getTeacher()+
                        ", "+ t.getCourse()+", 1)");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean removeTeaching(Teaching t) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("UPDATE insegnamento SET Stato=0 WHERE (Docente='"+t.getTeacher()+"'" +
                        " AND Corso='"+t.getCourse()+"')");
                st.close();
            }
            boolRes = result>0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public ArrayList<Teaching> getTeachings(Course c) {
        Connection conn1 = null;
        ArrayList<Teaching> teachingList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                teachingList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT d.Nome, d.Cognome " +
                        "FROM insegnamento i JOIN docente d ON (i.Docente=d.ID) " +
                        "WHERE i.Corso = '"+c.getTitle()+"' WHERE d.Stato=1 AND i.Stato=1");
                while (rs.next()) {
                    teachingList.add(new Teaching(rs.getInt("ID"), rs.getInt("Corso")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teachingList;
    }

    public ArrayList<Teaching> getTeachings(Teacher t) {
        Connection conn1 = null;
        ArrayList<Teaching> teachingList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                teachingList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT d.ID, i.Titolo" +
                        "FROM insegnamento i JOIN docente d ON (i.Docente=d.ID) " +
                        "WHERE i.Stato=1 AND d.ID = '"+t.getID()+"'");
                while (rs.next()) {
                    teachingList.add(new Teaching(rs.getInt("d.ID"), rs.getInt("i.ID")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teachingList;
    }

    public ArrayList<Teaching> getTeachings() {
        Connection conn1 = null;
        ArrayList<Teaching> teachingList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                teachingList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM insegnamento i " +
                        "JOIN docente d on (i.Docente=d.ID) " +
                        "WHERE d.Stato=1 AND i.Stato=1");
                while(rs.next()) {
                    teachingList.add(new Teaching(rs.getInt("ID"), rs.getInt("Corso")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teachingList;
    }

    public Teaching getTeaching(int Docente, int Corso) {
        Connection conn1 = null;
        Teaching teaching = null;
        try {
            conn1 = openConnection();
            if (conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM insegnamento WHERE (Docente="+Docente+
                        " AND Corso="+Corso+" AND Stato=1)");
                rs.next();
                teaching = new Teaching(rs.getInt("Docente"), rs.getInt("Corso"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return teaching;
    }


    /*
    BOOKING
     */

    public boolean insertBooking(Booking b) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                Teaching teaching = new Teaching(b.getTeacher(), b.getCourse());
                if(existsTeaching(teaching) && !(existsBooking(b))) {
                    result = st.executeUpdate("INSERT INTO prenotazione VALUES (null"+", '"+b.getCourse()+
                            "', '"+ b.getTeacher()+"', '"+b.getUser()+"', '"+b.getDay()+
                            "', '"+b.getTime()+"', '"+b.getState()+"')");
                    st.close();
                } else {
                    throw new SQLException("Error in insertBooking");
                }
                boolRes = result>0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean removeBooking(Booking b) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("DELETE FROM prenotazione WHERE (Corso='" + b.getCourse()+
                        "' AND Docente= '"+ b.getTeacher()+"' AND Utente= '"+b.getUser()+"')");
                st.close();
                boolRes = result>0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public ArrayList<Booking> getBookings() {
        Connection conn1 = null;
        ArrayList<Booking> bookingList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione p");
                while(rs.next()) {
                    bookingList.add(new Booking(rs.getInt("ID"), rs.getInt("Corso"),
                            rs.getInt("Docente"), rs.getInt("Utente"), rs.getString("Giorno"),
                            rs.getString("Ora"), rs.getString("Stato")));
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingList;
    }

    public Booking getBooking(int ID) {
        Connection conn1 = null;
        Booking booking = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE ID='"+ID+"'");
                rs.next();
                booking = new Booking(rs.getInt("ID"), rs.getInt("Corso"),
                        rs.getInt("Docente"), rs.getInt("Utente"),
                        rs.getString("Giorno"), rs.getString("Ora"), rs.getString("Stato"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return booking;
    }

    public Booking getBooking(int course, int teacher, int user, String day, String time) {
        Connection conn1 = null;
        Booking booking = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE (Corso="+course+" AND " +
                        "Docente="+teacher+" AND Utente="+user+" AND Giorno='"+day+"' AND Ora='"+time+"')");
                rs.next();
                booking = new Booking(rs.getInt("ID"), rs.getInt("Corso"),
                        rs.getInt("Docente"), rs.getInt("Utente"),
                        rs.getString("Giorno"), rs.getString("Ora"), rs.getString("Stato"));
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return booking;
    }

    public ArrayList<Booking> getUserBookings(String username) {
        User u = getUser(username);
        return getUserBookings(u);
    }

    public ArrayList<Booking> getUserBookings(User u) {
        Connection conn1 = null;
        ArrayList<Booking> bookingsList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingsList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE Utente='"+u.getID()+"'");
                addBookingsToArrayList(bookingsList, rs);
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingsList;

    }

    public ArrayList<Booking> getPendingUserBookings(User u) {
        Connection conn1 = null;
        ArrayList<Booking> bookingsList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingsList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE Utente='"+u.getID()+"' AND Stato='attiva'");
                addBookingsToArrayList(bookingsList, rs);
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingsList;
    }

    public ArrayList<Booking> getPendingBookings() {
        Connection conn1 = null;
        ArrayList<Booking> bookingsList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingsList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE Stato='attiva'");
                addBookingsToArrayList(bookingsList, rs);
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingsList;
    }

    public ArrayList<Booking> getFinishedUserBookings(User u) {
        Connection conn1 = null;
        ArrayList<Booking> bookingsList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingsList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE Utente='"+u.getID()+"' AND (Stato='disdetta' OR Stato='effettuata')");
                addBookingsToArrayList(bookingsList, rs);
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingsList;
    }

    public ArrayList<Booking> getFinishedBookings() {
        Connection conn1 = null;
        ArrayList<Booking> bookingsList = null;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                bookingsList = new ArrayList<>();
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE (Stato='disdetta' OR Stato='effettuata')");
                addBookingsToArrayList(bookingsList, rs);
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return bookingsList;
    }


    private void addBookingsToArrayList(ArrayList<Booking> bookingsList, ResultSet rs) throws SQLException {
        while(rs.next()) {
            bookingsList.add(new Booking(rs.getInt("ID"), rs.getInt("Corso"),
                    rs.getInt("Docente"), rs.getInt("Utente"),
                    rs.getString("Giorno"), rs.getString("Ora"), rs.getString("Stato")));
        }
        rs.close();
    }

    public boolean updateBooking(Booking b, String state) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("UPDATE prenotazione SET Stato='"+state+"' WHERE ID="+b.getID());
                st.close();
                boolRes = result>0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    public boolean insertCalendar(Calendar c) {
        Connection conn1 = null;
        int result = -1;
        boolean boolRes = false;
        try {
            conn1 = openConnection();
            if(conn1 != null) {
                Statement st = conn1.createStatement();
                result = st.executeUpdate("INSERT INTO calendario VALUES ('" + c.getDay()+
                        "', '"+ c.getDay()+"', '"+c.getTime()+"')");
                st.close();
                boolRes = result>0;
            } else {
                throw new SQLException("Error in insertCalendar");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn1);
        }
        return boolRes;
    }

    private boolean existsTeaching(Teaching t) {
        Connection conn = null;
        boolean queryResult = false;
        try {
            conn = openConnection();
            if (conn != null) {
                Statement st1 = conn.createStatement();
                ResultSet rs = st1.executeQuery("SELECT * FROM insegnamento WHERE (Corso='"
                        + t.getCourse()+ "' AND Docente='" + t.getTeacher()+ "' AND Stato=1)");
                queryResult = (rs.next());
                rs.close();
                st1.close();
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return queryResult;
    }

    private boolean existsBooking(Booking b) {
        Connection conn = null;
        boolean queryResult = false;
        try {
            conn = openConnection();
            if (conn != null) {
                Statement st1 = conn.createStatement();
                ResultSet rs = st1.executeQuery("SELECT * FROM prenotazione WHERE (Giorno='"
                        + b.getDay() + "' AND Ora='" + b.getTime() + "' AND Docente='" + b.getTeacher()+ "')");
                queryResult = (rs.next());
                rs.close();
                st1.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return queryResult;
    }

    public ArrayList<Calendar> freeSlots(Teaching t) {
        Connection conn = null;
        ArrayList<Calendar> teachingFreeSlots = null;
        try {
            conn = openConnection();
            if (conn != null) {
                teachingFreeSlots = new ArrayList<>();
                Statement st1 = conn.createStatement();
                ResultSet rs = st1.executeQuery(
                        "(SELECT c.Giorno, c.Ora " +
                                "FROM calendario c)" +
                                "EXCEPT" +
                                "(SELECT c1.Giorno, c1.Ora FROM" +
                                " calendario c1 JOIN prenotazione p1 ON (c1.Giorno=p1.Giorno AND c1.Ora=p1.Ora) " +
                                "WHERE p1.Docente = '"+t.getTeacher()+"')");

                while(rs.next()) {
                    teachingFreeSlots.add(new Calendar(rs.getString("Giorno"),
                            rs.getString("Ora")));
                }
                rs.close();
                st1.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return teachingFreeSlots;
    }

    public ArrayList<Lesson> getLessons() {
        Connection conn = null;
        ArrayList<Lesson> lessonsAvailable = null;
        try {
            conn = openConnection();
            if(conn != null) {
                lessonsAvailable = new ArrayList<>();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("(SELECT co.Titolo, d.Nome, d.Cognome, ca.Giorno, ca.Ora " +
                        "FROM insegnamento i JOIN docente d ON (i.Docente=d.ID) JOIN corso co ON (i.Corso=co.ID) " +
                        "JOIN calendario ca WHERE d.Stato=1 AND co.Stato=1) " +
                        "EXCEPT (SELECT co1.Titolo, d1.Nome, d1.Cognome, ca1.Giorno, ca1.Ora " +
                        "FROM insegnamento i1 JOIN docente d1 ON (i1.Docente=d1.ID) JOIN corso co1 " +
                        "ON (i1.Corso=co1.ID) JOIN prenotazione p1 ON (p1.Corso=co1.ID AND p1.Docente=d1.ID) " +
                        "JOIN calendario ca1 ON (ca1.Giorno=p1.Giorno AND ca1.Ora=p1.Ora)) " +
                        "ORDER BY `Giorno`='Venerdi', `Giorno`='Giovedi', `Giorno`='Mercoledi', " +
                        "`Giorno`='Martedi', `Giorno`='Lunedi';");
                while(rs.next()) {
                    lessonsAvailable.add(new Lesson(rs.getString("Titolo"), rs.getString("Nome"),
                            rs.getString("Cognome"), rs.getString("Giorno"), rs.getString("Ora")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return lessonsAvailable;
    }

    public ArrayList<Lesson> getLessonsAvailableForTeacherandDay(Course c, Teacher t, String day) {
        Connection conn = null;
        ArrayList<Lesson> lessonsAvailable = null;
        try {
            conn = openConnection();
            if(conn != null) {
                lessonsAvailable = new ArrayList<>();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("(SELECT c1.Giorno, c1.Ora FROM calendario c1 WHERE c1.Giorno='"+day+"')" +
                        "EXCEPT (SELECT p1.Giorno, p1.Ora FROM prenotazione p1 WHERE " +
                        "p1.Docente='"+t.getID()+"')");
                while(rs.next()) {
                    lessonsAvailable.add(new Lesson(c.getTitle(), t.getName(), t.getSurname(),
                            rs.getString("Giorno"), rs.getString("Ora")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return lessonsAvailable;
    }

    public Lesson getLessonFromBooking(Booking b) {
        Teacher t = getTeacher(b.getTeacher());
        return new Lesson(b.getID(), getCourseTitle(b.getCourse()), t.getName(),
                t.getSurname(), b.getDay(), b.getTime(), b.getState());
    }

    public ArrayList<Lesson> getLessonListFromBookingList (ArrayList<Booking> bookingsList) {
        ArrayList<Lesson> lessonsList = new ArrayList<>();
        for (Booking booking : bookingsList) {
            lessonsList.add(getLessonFromBooking(booking));
        }
        return lessonsList;
    }

    public boolean uniqueUserEmail(String emailAddress) {
        Connection conn = null;
        boolean result = false;
        try {
            conn = openConnection();
            if(conn != null) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM utente WHERE Email='"+emailAddress+"'");
                result = (!rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return result;
    }

    public boolean uniqueUserUsername(String Username) {
        Connection conn = null;
        boolean result = false;
        try {
            conn = openConnection();
            if(conn != null) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM utente WHERE Username='"+Username+"'");
                result = (!rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return result;
    }

    public ArrayList<Course> getCoursesAvailable(int teacherID) {
        Connection conn = null;
        ArrayList<Course> coursesAvailable = null;
        try {
            conn = openConnection();
            if(conn != null) {
                coursesAvailable = new ArrayList<>();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("(SELECT c.Titolo FROM corso c )" +
                        "EXCEPT (SELECT c.Titolo FROM insegnamento i JOIN corso c WHERE " +
                        "c.ID=i.Corso AND i.Docente='"+teacherID+"')");
                while(rs.next()) {
                    coursesAvailable.add(new Course(rs.getString("Titolo")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection(conn);
        }
        return coursesAvailable;
    }


}

