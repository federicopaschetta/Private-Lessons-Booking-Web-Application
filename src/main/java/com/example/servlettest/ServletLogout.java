package com.example.servlettest;

import DAO.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Servlet che gestisce il logout dell'utente
 */

@WebServlet(name = "ServletLogout", value = "/ServletLogout")
public class ServletLogout extends HttpServlet {

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
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * Il metodo ottiene la sessione utente dalla richiesta HTTP e la invalida. Da client, con un refresh si torna
     * sulla homepage.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession();
        if(!(s.getAttribute("UserRole").equals("ospite"))) {
            System.out.println(s.getAttribute("Username"));
            s.invalidate();
        } else {
            out.println("Non puoi accedere a questa funzione");
        }
        out.flush();
        out.close();
    }
}
