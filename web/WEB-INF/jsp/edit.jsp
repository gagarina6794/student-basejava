<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.BulletedListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page import="com.urise.webapp.model.Organization" %>
<%@ page import="com.urise.webapp.model.Section" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <p>
            <c:forEach var="type" items="<%=ContactType.values()%>">
        <dl>
            <dt>${type.title}</dt>
            <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContacts(type)}"></dd>
        </dl>
        </c:forEach>
        </p>
        <h3>Секции:</h3>
        <p>
            <c:forEach var="sectionType" items="<%=SectionType.values()%>">
        <dl>
                <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
            <dt><h3>${sectionType.title}</h3></dt>
            <c:choose>
            <c:when test="${sectionType=='OBJECTIVE' || sectionType=='PERSONAL'}">

            <dd><input type="text" name='${sectionType}' size="30"
                       value="${resume.getSections(sectionType).getContent()}"></dd>
            </c:when>
            <c:when test="${sectionType=='QUALIFICATION' || sectionType=='ACHIEVEMENTS'}">
            <textarea rows="10" cols="45"
                      name='${sectionType}'><%=String.join("\n", ((BulletedListSection) resume.getSections(sectionType)).getContent())%></textarea>

            </c:when>
            <c:when test="${sectionType=='EXPERIENCE' || sectionType=='EDUCATION'}">

            <c:forEach var="organization"
                       items="<%=((OrganizationSection) resume.getSections(sectionType)).getContent()%>"
                       varStatus="count">
            <dl>
                <dt>Название организации:</dt>
                <dd><input type="text" name="${sectionType}" size=40
                           value="${organization.organizationName}"></dd>
            </dl>
            <dl>
                <dt>Сайт учереждения:</dt>
                <dd><input type="text" name="${sectionType}Link" size=40
                           value="${organization.link.url}"></dd>
            </dl>
            <br>
            <c:forEach var="experience" items="${organization.experiences}">
                <jsp:useBean id="experience" type="com.urise.webapp.model.Organization.Experience"/>
            <dl>
                <dt>Дата начала:</dt>
                <dd>
                    <input type="text" name="${sectionType}DateFrom${count.index}" size=30
                           value="<%=experience.getYearFrom()!=null?experience.getYearFrom():""%>"
                           placeholder="yyyy/MM">
                </dd>
            </dl>
            <dl>
                <dt>Дата окончания:</dt>
                <dd>
                    <input type="text" name="${sectionType}DateTo${count.index}" size=30
                           value="<%=experience.getYearTo()!=null?experience.getYearTo():""%>" placeholder="yyyy/MM">
            </dl>
            <dl>
                <dt>Должность:</dt>
                <dd><input type="text" name="${sectionType}Title${count.index}" size=30
                           value="${experience.title}">
            </dl>
            <dl>
                <dt>Описание:</dt>
                <dd><textarea name="${sectionType}Info${count.index}" rows=10
                              cols=45><%=experience.getInfo() != null ? experience.getInfo() : ""%></textarea></dd>
            </dl>
            </c:forEach>
            </c:forEach>
            </c:when>
            </c:choose>
            </c:forEach>
        </p>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
