package ServletApp;

import DAO.DAO;
import DAO.User;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletUserInfo", value = "/ServletUserInfo")
public class ServletUserInfo extends HttpServlet {

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
        Object username = request.getParameter("Username");
        User user;
        if(username!=null) {
            user = dao.getUser(username.toString());
            returningJSON = gson.toJson(user);
        }
        out.print(returningJSON);
        out.flush();
        out.close();
    }
}
