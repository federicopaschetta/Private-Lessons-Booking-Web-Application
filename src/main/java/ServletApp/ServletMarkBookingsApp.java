package ServletApp;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletMarkBookingsApp", value = "/ServletMarkBookingsApp")
public class ServletMarkBookingsApp extends HttpServlet {

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
        boolean result = false;
        String returningJson;
        String action = request.getParameter("Action");
        int bookingID = Integer.parseInt(request.getParameter("Booking"));
        Booking booking = dao.getBooking(bookingID);
        if(action.equals("Done")) {
            result = dao.updateBooking(booking, "effettuata");
        } else if (action.equals("Canceled")) {
            result = dao.updateBooking(booking, "disdetta");
        }
        returningJson = gson.toJson(result);
        out.println(returningJson);
        out.flush();
        out.close();
    }
}
