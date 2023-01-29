package com.example.servlettest;

import DAO.DAO;
import DAO.Lesson;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Servlet che fornisce la lista delle lezioni disponibili
 */

@WebServlet(name = "ServletLezioni", value = "/ServletLezioni")
public class ServletLezioni extends HttpServlet {

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
        Gson gson = new Gson();
        String returningJSON = null;
        ArrayList<Lesson> lessonsList = dao.getLessons();
        returningJSON = gson.toJson(lessonsList);
        out.print(returningJSON);
        out.flush();
        out.close();
    }
}
