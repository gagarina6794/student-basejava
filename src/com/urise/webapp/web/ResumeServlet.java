package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ResumeServlet extends HttpServlet {
    private Storage storage;
    static final String JDBC_DRIVER = "org.postgresql.Driver";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName(JDBC_DRIVER);
            storage = Config.get().getSqlStorage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid= request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume = storage.get(uuid);
        resume.setFullName(fullName);
        for (ContactType type: ContactType.values()){
            String value = request.getParameter(type.name());
            if(value != null && value.trim().length() != 0){
                resume.  addContacts(type,value);
            }else {
                resume.getContacts().remove(type);
            }
        }
        storage.update(resume);
        response.sendRedirect("resume");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid=  request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null){
        request.setAttribute("resumes", storage.getAllSorted());
        request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request,response);
        return;
        }
        Resume resume = null;
        switch (action){
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action" + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/list.jsp": "/WEB-INF/jsp/edit.jsp")).forward(request,response);
      /*  request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("<link rel=\"stylesheet\" type =\"text/css\" href=\"css/style.css\"/>");

        writer.write("<table border=\"1\"><tr><th>Uuid: " + "</th>");
        writer.write("<th>Name: " + "</th></tr>");

        for (Resume resume : storage.getAllSorted()) {
            writer.write("<tr><td>" + resume.getUuid() + "</td>");
            writer.write("<td>" + resume.getFullName() + "</td></tr>");
        }
        writer.write("</table>");
*/
    }
}
