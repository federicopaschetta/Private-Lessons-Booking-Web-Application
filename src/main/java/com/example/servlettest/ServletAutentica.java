package com.example.servlettest;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Servlet che gestisce l'autenticazione dell'utente o admin nella piattaforma
 */
@WebServlet(name = "ServletAutentica", value = "/ServletAutentica")
public class ServletAutentica extends HttpServlet {

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
     * Ottiene username e password come parametri della richiesta HTTP, se non sono nulli o vuoti, invoca un metodo del DAO
     * che restituisce il ruolo. Inserisce in sessione gli attributi Username e Ruolo.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("loginUsername");
        String password = request.getParameter("loginPassword");
        Gson gson = new Gson();
        HttpSession s = request.getSession();
        if ((username != null && !username.equals("")) && (password != null && !password.equals(""))) {
            String role = dao.getUserRole(username, password);
            if (role!=null && role.equals("cliente") || Objects.equals(role, "admin")) {
                s.setAttribute("Username", username);
                s.setAttribute("UserRole", role);
                User u = dao.getUser(username);
                String user = gson.toJson(u);
                out.print(user);
            } else {
                String error = gson.toJson(false);
                out.print(error);
            }
        } else {
            String error = gson.toJson(false);
            out.print(error);        
        }
        out.close();
    }
}
