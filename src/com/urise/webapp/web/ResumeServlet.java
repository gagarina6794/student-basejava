package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");

        if (uuid == null) {
            response.getWriter().write("");
        } else {
            Resume resume = storage.get(uuid);
            response.getWriter().write("<table border=\"1\"><tr><th>Name: "  + "</th><td>" +  resume.getFullName() + "</td></tr>");
            response.getWriter().write("<tr><th>Uuid: "  + "</th><td>" +  uuid + "</td></tr></table>");
           /* response.getWriter().write("<br>" + "Contacts: ");
            response.getWriter().write("<br>" + "<ul>");
            for (var item : resume.getContacts().entrySet()) {
                response.getWriter().write("<li>" + item.getKey().toString() + ": " + item.getValue() + "</li>");
            }
            response.getWriter().write("</ul>");
            response.getWriter().write("Sections: ");
            response.getWriter().write("<ul>");
            for (var entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                switch (sectionType) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        response.getWriter().write("<br><li>" + sectionType + ": " + "</li>");
                        response.getWriter().write("<ul>");

                        for (var elem : ((BulletedListSection) entry.getValue()).getContent()) {

                            response.getWriter().write("<li>" + elem + "</li>");
                        }
                        response.getWriter().write("</ul>");
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        response.getWriter().write("<br><li>" + sectionType + ": " + "</li>");
                        response.getWriter().write(entry.getValue().toString());
                        break;
                }
            }*/
           // response.getWriter().write("</ul>");
        }
    }
}
