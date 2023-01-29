package com.example.servlettest;

import DAO.DAO;
import DAO.User;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet che, data una sessione, restituisce il corrispondente oggetto utente
 */

@WebServlet(name = "ServletUser", value = "/ServletUser")
public class ServletUser extends HttpServlet {
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
     * Il metodo ottiene lo username dalla sessione utente, invoca il metodo nel DAO che ottiene il corrispondente
     * utente, e lo restituisce in output, dopo averlo convertito in JSON.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String returningJSON = null;
        HttpSession session = request.getSession();
        Object username = session.getAttribute("Username");
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
