package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
        //    boolean isNew = true;
        Resume resume;
        if (fullName.length() == 0) {
            response.sendRedirect("resume");
            return;
        }
        if (uuid.length() == 0) {
            resume = new Resume(fullName);
            storage.save(resume);
        } else {
            resume = storage.get(uuid);
            //       isNew = false;
        }

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
            String[] orgInfo = request.getParameterValues(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        String[] informaion = value.split("\n");
                        List<String> contentList = new ArrayList<>();
                        for (int i = 0; i < informaion.length; i++) {
                            if (informaion[i].trim().length() != 0) {
                                contentList.add(informaion[i]);
                            }
                        }
                        String last = contentList.get(contentList.size() - 1).replace("\r", "");
                        contentList.remove(contentList.size() - 1);
                        contentList.add(last);
                        resume.addSection(type, new BulletedListSection(contentList));
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new SimpleTextSection(value));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizationList = new ArrayList<>();
                        String[] links = request.getParameterValues(type.name() + "Link");
                        for (int i = 0; i < orgInfo.length; i++) {
                            String name = orgInfo[i];
                            if (name.length() != 0) {
                                List<Organization.Experience> experiences = new ArrayList<>();
                                String[] dateFrom = request.getParameterValues(type.name() + "DateFrom" + i);
                                String[] dateTo = request.getParameterValues(type.name() + "DateTo" + i);
                                String[] title = request.getParameterValues(type.name() + "Title" + i);
                                String[] information = request.getParameterValues(type.name() + "Info" + i);
                                for (int j = 0; j < title.length; j++) {
                                    if (dateFrom[j].length() != 0 &&
                                            dateTo[j].length() != 0 &&
                                            title[j].length() != 0 &&
                                            information[j].length() != 0) {
                                        experiences.add(new Organization.Experience(YearMonth.parse(dateFrom[j], DateTimeFormatter.ofPattern("uuuu-M")),
                                                YearMonth.parse(dateTo[j], DateTimeFormatter.ofPattern("uuuu-M")), title[j], information[j].replaceAll("\r|\n", "")));
                                    }
                                }
                                organizationList.add(new Organization(name, new Link(name, links[i]), experiences));
                            }
                        }
                        resume.getSections().put(type, new OrganizationSection(organizationList));
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
                // resume = new Resume("", "");
                //  storage.save(resume);
                resume = Resume.EMPTY;
                dispatcher = "/WEB-INF/jsp/edit.jsp";
                break;
            case "view":
                resume = storage.get(uuid);
                dispatcher = "/WEB-INF/jsp/view.jsp";
                break;
            case "edit":
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section section = resume.getSections(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = SimpleTextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENTS:
                        case QUALIFICATION:
                            if (section == null) {
                                section = BulletedListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section;
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.EMPTY);
                            if (orgSection != null) {
                                for (Organization org : ((OrganizationSection) section).getContent()) {
                                    List<Organization.Experience> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Organization.Experience.EMPTY);
                                    emptyFirstPositions.addAll(org.getExperiences());
                                    emptyFirstOrganizations.add(new Organization(org.getOrganizationName(), org.getLink(), emptyFirstPositions));
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganizations);
                            break;
                    }
                    resume.addSection(type, section);
                }
           /*     for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    OrganizationSection section = (OrganizationSection) resume.getSections(type);
                    List<Organization> emptyFirstOrganizations = new ArrayList<>();
                    emptyFirstOrganizations.add(Organization.EMPTY);
                    if (section != null) {
                        for (Organization org : section.getContent()) {
                            List<Organization.Experience> emptyFirstPositions = new ArrayList<>();
                            emptyFirstPositions.add(Organization.Experience.EMPTY);
                            emptyFirstPositions.addAll(org.getExperiences());
                            emptyFirstOrganizations.add(new Organization(org.getOrganizationName(),org.getLink(), emptyFirstPositions));
                        }
                    }
                    resume.addSection(type, new OrganizationSection(emptyFirstOrganizations));
                }*/
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
