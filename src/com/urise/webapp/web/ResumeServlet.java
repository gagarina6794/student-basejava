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
    private Storage storage = Config.get().getSqlStorage();
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DATABASE_URL = Config.get().getDbUrl();
    static final String DATABASE_USER = Config.get().getDbUser();
    static final String DATABASE_PASSWORD = Config.get().getDbPassword();

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
            try {
                Class.forName(JDBC_DRIVER);
                Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

                Resume resume = storage.get(uuid);
                response.getWriter().write("Name: " + resume.getFullName());
                response.getWriter().write("<br>" + "Contacts: ");
                response.getWriter().write("<br>"+"<ul>");
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
                            response.getWriter().write("<br><li>" + sectionType+ ": " + "</li>");
                            response.getWriter().write("<ul>");

                            for (var elem:((BulletedListSection) entry.getValue()).getContent()) {

                                response.getWriter().write("<li>" + elem+ "</li>");
                            }
                            response.getWriter().write("</ul>");
                            break;
                        case OBJECTIVE:
                        case PERSONAL:
                            response.getWriter().write("<br><li>" + sectionType+ ": " + "</li>");
                            response.getWriter().write(  entry.getValue().toString() );
                            break;
                    }
                }
                response.getWriter().write("</ul>");
            }catch(ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
