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
 * Servlet che visualizza e conferma/elimina le prenotazioni di un dato utente.
 */

@WebServlet(name = "ServletVisualizzaPrenotazioni", value = "/ServletVisualizzaPrenotazioni")
public class ServletVisualizzaPrenotazioni extends HttpServlet {

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
     * @param request richiesta della get
     * @param response risposta della get
     * @throws ServletException
     * @throws IOException
     * Il metodo ottiene username e ruolo dalla sessione utente, se questi passano il check sul permesso per l'operazione,
     * invoca un metodo del DAO che fornisce le prenotazioni dell'utente e, se la lista non e' vuota, converte la lista 
     * di prenotazioni nella corrispettiva lista di oggetti lezioni con un altro metodo del DAO, la converte a sua volta
     * in JSON e la stampa in output.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String username = s.getAttribute("Username").toString();
        String userRole = s.getAttribute("UserRole").toString();
        Gson gson = new Gson();
        if(username != null && userRole.equals("cliente")) {
            ArrayList<Booking> bookingsList = dao.getUserBookings(username);
            if(bookingsList.size()==0) {
                out.print("Non hai prenotazioni.");
            } else {
                ArrayList<Lesson> bookedLessonsList = dao.getLessonListFromBookingList(bookingsList);
                String bookingsListString = gson.toJson(bookedLessonsList);
                out.print(bookingsListString);
            }
        } else {
                out.print("Devi fare login per visualizzare le tue prenotazioni");
            }
        }

    
    /** 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String username = s.getAttribute("Username").toString();
        String userRole = s.getAttribute("UserRole").toString();
        String returningJSON;
        Gson gson = new Gson();
        if (username != null && userRole.equals("cliente")) {
            String action = request.getParameter("Action");
            String course = request.getParameter("Course");
            String teacherName = request.getParameter("TeacherName");
            int courseID = dao.getCourseID(course);
            String teacherSurname = request.getParameter("TeacherSurname");
            int teacherID = dao.getTeacherID(teacherName, teacherSurname);
            String day = request.getParameter("Day");
            String time = request.getParameter("Time").toString();
            int userID = dao.getUserID(username);
            Booking booking = dao.getBooking(courseID, teacherID, userID, day, time);
            boolean result = false;
            if(action.equals("Done")) {
                result = dao.updateBooking(booking, "effettuata");
            } else if(action.equals("Canceled")){
                result = dao.updateBooking(booking, "disdetta");
            }
            returningJSON = gson.toJson(result);
            out.println(returningJSON);
            out.flush();
            out.close();
        } else {
            String forbiddenAccess = "Non ti e' permessa questa funzione";
            returningJSON = gson.toJson(forbiddenAccess);
            out.println(returningJSON);
            out.flush();
            out.close();
        }
    }
}
