package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.urise.webapp.ResumeTestData.fillBulltedListSection;
import static com.urise.webapp.model.SectionType.ACHIEVEMENTS;
import static com.urise.webapp.model.SectionType.QUALIFICATION;

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
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume = storage.get(uuid);
        resume.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContacts(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        String[] informaion = value.split("\n");
                        List<String> contentList = new ArrayList<>();
                        for(int i = 0; i<informaion.length; i++){
                            if(informaion[i].trim().length() != 0){
                               contentList.add(informaion[i]);
                            }
                        }
                        resume.addSection(type,new BulletedListSection(contentList));
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new SimpleTextSection(value));
                        break;
                }
            } else {
                resume.getSections().remove(type);
            }
        }
        storage.update(resume);
        response.sendRedirect("resume");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        String dispatcher = "/WEB-INF/jsp/list.jsp";
        Resume resume = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "add":
                resume = new Resume("");
                storage.save(resume);
                dispatcher = "/WEB-INF/jsp/edit.jsp";
                break;
            case "view":
                resume = storage.get(uuid);
                dispatcher = "/WEB-INF/jsp/view.jsp";
                break;
            case "edit":
                resume = storage.get(uuid);
                dispatcher = "/WEB-INF/jsp/edit.jsp";
                break;
            default:
                throw new IllegalArgumentException("Action" + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(dispatcher).forward(request, response);
      //  request.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/list.jsp" : "/WEB-INF/jsp/edit.jsp")).forward(request, response);
    }
}
