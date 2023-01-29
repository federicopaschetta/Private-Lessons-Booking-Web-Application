package com.example.servlettest;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Servlet che visualizza i corsi presenti sulla piattaforma
 */

@WebServlet(name = "ServletCorsi", value = "/ServletCorsi")
public class ServletCorsi extends HttpServlet {

    private DAO dao = null;

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
     * Viene invocato un metodo dal DAO che restituisce la lista dei corsi, dopo essere convertita in JSON viene
     * inviata in output.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<Course> coursesList = dao.getCourses();
        Gson gson = new Gson();
        String coursesString = gson.toJson(coursesList);
        out.print(coursesString);
        out.flush();
        out.close();
    }
}
