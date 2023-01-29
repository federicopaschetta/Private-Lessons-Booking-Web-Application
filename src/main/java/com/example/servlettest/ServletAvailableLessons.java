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
 * Servlet che gestisce la visualizzazione delle lezioni disponibili sulla piattaforma
 */

@WebServlet(name = "ServletAvailableLessons", value = "/ServletAvailableLessons")
public class ServletAvailableLessons extends HttpServlet {

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
     * Si effettua un controllo sul ruolo dell'utente, se passa il controllo, invoca un metodo dal DAO che
     * restituisce le lezioni disponibili. Le lezioni vengono inviate in output dopo la conversione in JSON.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        String username = s.getAttribute("Username").toString();
        String userRole = s.getAttribute("UserRole").toString();
        String returningJSON;
        Gson gson = new Gson();
        if (username != null && (userRole.equals("cliente") || userRole.equals("admin"))) {
            ArrayList<Lesson> lessonsAvailable = dao.getLessons();
            if(lessonsAvailable.size()>0) {
                returningJSON = gson.toJson(lessonsAvailable);
                out.println(returningJSON);
            } else {
                returningJSON = "No lessons available";
            }
        } else {
            returningJSON = "Non puoi accedere a questa funzione.";
        }
        out.println(returningJSON);
        out.flush();
        out.close();
    }
}
