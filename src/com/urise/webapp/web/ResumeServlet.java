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
        response.getWriter().write("<link rel=\"stylesheet\" type =\"text/css\" href=\"css/style.css\"/>");

        response.getWriter().write("<table border=\"1\"><tr><th>Uuid: " + "</th>");
        response.getWriter().write("<th>Name: " + "</th></tr>");

        for (Resume resume : storage.getAllSorted()) {
            response.getWriter().write("<tr><td>" + resume.getUuid() + "</td>");
            response.getWriter().write("<td>" + resume.getFullName() + "</td></tr>");
        }
        response.getWriter().write("</table>");

    }
}
