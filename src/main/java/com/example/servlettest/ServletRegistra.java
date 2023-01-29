package com.example.servlettest;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet che gestisce la registrazione dell'utente
 */

@WebServlet(name = "ServletRegistra", value = "/ServletRegistra")
public class ServletRegistra extends HttpServlet {

    private DAO dao = null;

    /** 
     * @param config configurazione della Servlet
     * @throws ServletExceptio
     * Inizializza il DAO con l'istanza salvata nel ServletContext
     */

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }

    
    /** 
     * @param request richiesta della Post
     * @param response risposta della Post
     * @throws ServletException
     * @throws IOException
     * Il metodo ottiene tutti i parametri necessari per la registrazione dalla richiesta, controlla che non siano nulli,
     * dopo i controlli sul client, inserisce l'utente nel database, assegna i dati dell'utente agli attributi della sessione
     * e ritorna il risultato della procedura.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("SignUpName");
        String surname = request.getParameter("SignUpSurname");
        String email = request.getParameter("SignUpEmail");
        String username = request.getParameter("SignUpUsername");
        String password = request.getParameter("SignUpPassword");
        Gson gson = new Gson();
        boolean result = false;
        String returningJSON = null;
        HttpSession session = request.getSession();
        if (username != null && password != null && name != null && surname != null && email != null) {
            User user = new User(name, surname, email, username, password, "cliente");
            result = dao.insertUser(user);
            String role = dao.getUserRole(username, password);
            session.setAttribute("Username", username);
            session.setAttribute("UserRole", role);
        }
        System.out.println(response.encodeRedirectURL(request.getContextPath())+"/index.html");
        returningJSON = gson.toJson(result);
        out.print(returningJSON);
        out.flush();
        out.close();
    }

    
    /** 
     * @param request richiesta della get
     * @param response risposta della Get
     * @throws ServletException
     * @throws IOException
     * Metodo che compie le operazioni accessorie per la registrazione, in base al parametro action controlla
     * se l'email o lo username inseriti sono unici invocando
     * i rispettivi metodi del DAO e restituisce true o false in base alla risposta.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String returningJSON;
        boolean result = false;
        String action = request.getParameter("Action");
        if(action.equals("UniqueEmail")) {
            String emailAddress = request.getParameter("mailAddress");
            result = dao.uniqueUserEmail(emailAddress);
        } else if(action.equals("UniqueUsername")) {
            String username = request.getParameter("username");
            result = dao.uniqueUserUsername(username);
        }
        returningJSON = gson.toJson(result);
        out.print(returningJSON);
        out.flush();
        out.close();
    }
}
