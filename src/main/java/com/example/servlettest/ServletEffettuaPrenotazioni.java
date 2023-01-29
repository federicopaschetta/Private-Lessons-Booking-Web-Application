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
 * Servlet che gestisce l'inserimento di una prenotazione da parte dell'utente
 */

@WebServlet(name = "ServletEffettuaPrenotazioni", value = "/ServletEffettuaPrenotazioni")
public class ServletEffettuaPrenotazioni extends HttpServlet {

    private DAO dao = null;

    /** 
     * @param config configurazione della Servlet
     * @throws ServletException
     * Inizializza il DAO con l'istanza salvata nel ServletContext
     */

    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        ServletContext ctx = config.getServletContext();
        dao = (DAO)ctx.getAttribute("DAO");
    }
    
    /** 
     * @param request richiesta della Post
     * @param response risposta della Post
     * @throws ServletException
     * @throws IOException
     * Questo metodo gestisce per intero la prenotazione, gestita sul client come una serie di dropdown list
     * collegate e influenzate le une dalle altre. Per questo motivo, in base al parametro della richiesta "Azione",
     * entra nell'if corretto e inizialmente fornisce l'elenco delle lezioni disponibili, in seguito, in un'altra richiesta
     * aggiunge la scelta del corso come parametro e fornisce i docenti disponibili dato il corso scelto. La scelta prosegue
     * fino agli slot disponibili dati corso, docente, giorno e ora. A questo punto, se l'utente e' un cliente, puo'
     * inserire la prenotazione nel database, altrimenti non gli e' permesso.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        Gson gson = new Gson();
        String teacherName, teacherSurname, courseTitle, selectedDay, selectedHour;
        int userID, teacherID, courseID;
        Course selectedCourse;
        Teacher selectedTeacher;
        String[] selectedTeacherStrings;
        String action = request.getParameter("Action");
        switch (action) {
            case "AvailableCourses":
                ArrayList<Course> coursesAvailable = dao.getCourses();
                String availableCourses = gson.toJson(coursesAvailable);
                out.print(availableCourses);
                break;
            case "AvailableTeachers":
                courseTitle = request.getParameter("Course");
                selectedCourse = dao.getCourse(dao.getCourseID(courseTitle));
                ArrayList<Teacher> availableTeachers = dao.getTeachersforCourse(selectedCourse);
                String availableTeachersString = gson.toJson(availableTeachers);
                out.print(availableTeachersString);
                break;
            case "AvailableHours":
                courseTitle = request.getParameter("Course");
                selectedCourse = dao.getCourse(dao.getCourseID(courseTitle));
                selectedTeacherStrings = request.getParameter("Teacher").split(",");
                teacherName = selectedTeacherStrings[1].trim();
                teacherSurname = selectedTeacherStrings[0];
                selectedTeacher = dao.getTeacher(teacherName, teacherSurname);
                selectedDay = request.getParameter("Day");
                ArrayList<Lesson> availableLesson = dao.getLessonsAvailableForTeacherandDay(selectedCourse, selectedTeacher, selectedDay);
                String availableSlots = gson.toJson(availableLesson);
                out.print(availableSlots);
                break;
            default:
                if (s.getAttribute("UserRole") == null || !(s.getAttribute("UserRole").equals("cliente"))) {
                    out.println("Fai il login per effettuare una prenotazione");
                } else {
                    String username = s.getAttribute("Username").toString();
                    userID = dao.getUserID(username);
                    selectedTeacherStrings = request.getParameter("Teacher").split(",");
                    teacherName = selectedTeacherStrings[1].trim();
                    teacherSurname = selectedTeacherStrings[0];
                    teacherID = dao.getTeacherID(teacherName, teacherSurname);
                    courseTitle = request.getParameter("Course");
                    courseID = dao.getCourseID(courseTitle);
                    selectedDay = request.getParameter("Day");
                    selectedHour = request.getParameter("Hour");
                    Booking b = new Booking(courseID, teacherID, userID, selectedDay, selectedHour);
                    if (dao.insertBooking(b)) {
                        out.print(gson.toJson(true));
                    } else {
                        out.print(gson.toJson(false));
                    }
                }
                break;
        }
    }
}
