package com.example.servlettest;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Servlet che gestisce le operazioni dell'admin.
 */

@WebServlet(name = "ServletAdmin", value = "/ServletAdmin")
public class ServletAdmin extends HttpServlet {

    private DAO dao = null;
    
    /** 
     * @param config configurazione della Servlet
     * @throws ServletException
     * Inizializza il DAO con l'istanza salvata nel ServletContext
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        dao = (DAO)ctx.getAttribute("DAO");
    }
    
    /** 
     * @param request richiesta della Get
     * @param response risposta della Get
     * @throws ServletException
     * @throws IOException
     * Servlet che permette all'admin di visualizzare i dati della piattaforma
     * Se il ruolo salvato nella sessione utente e' admin, in base all'azione, parametro della get,
     * richiama il metodo corretto nel DAO e restituisce le informazioni richieste, convertite in JSON.
     * Questo metodo gestisce le visualizzazioni prima di un'eventuale eliminazione di prenotazioni, docenti, corsi e insegnamenti.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String username = s.getAttribute("Username").toString();
        String userRole = s.getAttribute("UserRole").toString();
        String returningJSON = null;
        if (username != null && userRole.equals("admin")) {
            Gson gson = new Gson();
            String action = request.getParameter("Action");
            switch (action) {
                case "ViewAllBookings":
                    ArrayList<Booking> bookingsList = dao.getBookings();
                    returningJSON = gson.toJson(bookingsList);
                    break;
                case "GetTeachersToDelete":
                    ArrayList<Teacher> teacherList = dao.getTeachers();
                    returningJSON = gson.toJson(teacherList);
                    break;
                case "GetCoursesToDelete":
                    ArrayList<Course> coursesList = dao.getCourses();
                    returningJSON = gson.toJson(coursesList);
                    break;
                case "GetTeachingsToDelete":
                    ArrayList<Teaching> teachingList = dao.getTeachings();
                    returningJSON = gson.toJson(teachingList);
                    break;
            }
        } else {
            returningJSON = "Non puoi accedere a questa funzione";
        }
        out.print(returningJSON);
        out.flush();
        out.close();

    }

    
    /** 
     * @param request richiesta della Post
     * @param response risposta della Post
     * @throws ServletException
     * @throws IOException
     * Servlet che esegue le operazioni dell'admin.
     * Se il ruolo salvato nella sessione utente e' admin, in base all'azione richiesta, parametro della post,
     * richiama il metodo corretto nel DAO e restituisce true se riesce nell'esecuzione, false altrimenti, convertiti in JSON.
     * Questo metodo gestisce l'aggiunta e l'eliminazione di docenti, corsi, insegnamenti e prenotazioni.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String username = s.getAttribute("Username").toString();
        String userRole = s.getAttribute("UserRole").toString();
        Gson gson = new Gson();
        String returningJSON, teacherName, teacherSurname, course;
        Teacher selectedTeacher;
        boolean result = false;
        if (username != null && userRole.equals("admin")) {
            String action = request.getParameter("Action");
            switch (action) {
                case "InsertTeacher":
                    teacherName = request.getParameter("TeacherName");
                    teacherSurname = request.getParameter("TeacherSurname");
                    selectedTeacher = new Teacher(teacherName, teacherSurname);
                    result = dao.insertTeacher(selectedTeacher);
                    break;
                case "RemoveTeacher": {
                    String[] selectedTeacherStrings = request.getParameter("Teacher").split(",");
                    teacherName = selectedTeacherStrings[1].trim();
                    teacherSurname = selectedTeacherStrings[0];
                    selectedTeacher = dao.getTeacher(teacherName, teacherSurname);
                    result = dao.removeTeacher(selectedTeacher);
                    break;
                }
                case "InsertCourse":
                    course = request.getParameter("Course");
                    result = dao.insertCourse(new Course(course));
                    break;
                case "RemoveCourse":
                    course = request.getParameter("Course");
                    Course removingCourse = dao.getCourse(dao.getCourseID(course));
                    result = dao.removeCourse(removingCourse);
                    break;
                case "InsertTeaching": {
                    course = request.getParameter("Course");
                    int courseID = dao.getCourseID(course);
                    String[] selectedTeacherStrings = request.getParameter("Teacher").split(",");
                    teacherName = selectedTeacherStrings[1].trim();
                    teacherSurname = selectedTeacherStrings[0];
                    int teacherID = dao.getTeacherID(teacherName, teacherSurname);
                    Teaching selectedTeaching = new Teaching(teacherID, courseID);
                    result = dao.insertTeaching(selectedTeaching);
                    break;
                }
                case "RemoveTeaching": {
                    String[] selectedTeachingStrings = request.getParameter("Teaching").split("-");
                    String teacher = selectedTeachingStrings[0];
                    course = selectedTeachingStrings[1];
                    Teaching selectedTeaching = dao.getTeaching(Integer.parseInt(teacher), Integer.parseInt(course));
                    result = dao.removeTeaching(selectedTeaching);
                    break;
                }
                case "Done":
                case "Canceled":
                    int bookingID = Integer.parseInt(request.getParameter("BookingID"));
                    Booking booking = dao.getBooking(bookingID);
                    if (action.equals("Done")) {
                        result = dao.updateBooking(booking, "effettuata");
                    } else {
                        result = dao.updateBooking(booking, "disdetta");
                    }
                    break;
            }
            returningJSON = gson.toJson(result);
        } else {
            returningJSON = "Non puoi accedere a questa funzione.";
        }
        out.print(returningJSON);
        out.flush();
        out.close();
    }

}
