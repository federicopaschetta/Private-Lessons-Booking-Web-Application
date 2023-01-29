package com.example.servlettest;

import DAO.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/**
 * Servlet che inizializza il DAO allo startup del progetto
 */
@WebServlet(name = "ServletDAO", value = "/ServletDAO", loadOnStartup = 1, asyncSupported = true)
public class ServletDAO extends HttpServlet {

    
    /** 
     * @param config configurazione della Servlet
     * @throws ServletException
     * Se non e' ancora stato creato, crea un oggetto DAO e lo inserisce come attributo nel ServletContext
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        String url = ctx.getInitParameter("DB-URL");
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("pwd");
        DAO dao;
        if (ctx.getAttribute("DAO") == null) {
            dao = new DAO(url, user, pwd);
            ctx.setAttribute("DAO", dao);
        } else {
            dao = (DAO) ctx.getAttribute("DAO");
        }
    }
}