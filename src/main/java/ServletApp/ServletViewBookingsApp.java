package ServletApp;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletViewBookingsApp", value = "/ServletViewBookingsApp")
public class ServletViewBookingsApp extends HttpServlet {

    private DAO dao = null;

    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        ServletContext ctx = config.getServletContext();
        dao = (DAO)ctx.getAttribute("DAO");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String returningJSON = null;

        String action = request.getParameter("Action");
        ArrayList<Booking> bookingsList = new ArrayList<>();
        ArrayList<Lesson> bookedLessonsList;
        if(action.equals("AdminPending")) {
            bookingsList = dao.getPendingBookings();
            if(bookingsList.size()>0) {
                bookedLessonsList = dao.getLessonListFromBookingList(bookingsList);
                returningJSON = gson.toJson(bookedLessonsList);
            }
        } else if (action.equals("AdminFinished")) {
            bookingsList = dao.getFinishedBookings();
            if(bookingsList.size()>0) {
                bookedLessonsList = dao.getLessonListFromBookingList(bookingsList);
                returningJSON = gson.toJson(bookedLessonsList);
            }
        }
        else {
            Object username = request.getParameter("Username");
            if(username != null) {
                User u = dao.getUser(username.toString());
                if(action.equals("Pending")) {
                    bookingsList = dao.getPendingUserBookings(u);

                } else if(action.equals("Finished")) {
                    bookingsList = dao.getFinishedUserBookings(u);
                }
                if(bookingsList.size()==0) {
                    returningJSON = gson.toJson(bookingsList);
                } else {
                    bookedLessonsList = dao.getLessonListFromBookingList(bookingsList);
                    returningJSON = gson.toJson(bookedLessonsList);
                }
            }
        }
        out.print(returningJSON);
        out.flush();
        out.close();
    }
}
