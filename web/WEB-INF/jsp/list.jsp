<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/${theme}.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<h2><a href="resume?theme=theme1">Тема 1</a></h2>
<h2><a href="resume?theme=theme2">Тема 2</a></h2>

<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
       <c:forEach items="${resumes}" var="resume">
           <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
           <td><a href="resume?uuid=${resume.uuid}&action=view&theme=${theme}">${resume.fullName}</a></td>
           <td><%=ContactType.EMAIL.toHtml(resume.getContacts(ContactType.EMAIL))%></td>
           <td><a href="resume?uuid=${resume.uuid}&action=delete&theme=${theme}"><img src="img/delete.png"></a></td>
           <td><a href="resume?uuid=${resume.uuid}&action=edit&theme=${theme}"><img src="img/pencil.png"></a></td>
       </tr>
       </c:forEach>
    </table>
    <a href="resume?action=add&theme=${theme}"><img src="img/add.png"></a>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
