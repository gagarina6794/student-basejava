<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.*" %>
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
<p>
<h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
<p>
<h3>Контакты:</h3>
<c:forEach var="contactEntry" items="${resume.contacts}">
    <jsp:useBean id="contactEntry"
                 type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
    <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
</c:forEach>
</p>
<p>
<h3>Секции:</h3>
<c:forEach var="sectionType" items="<%=SectionType.values()%>">
    <p>
        <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
        <%=sectionType.getTitle()%>
        <c:choose>
            <c:when test="${sectionType=='OBJECTIVE' || sectionType=='PERSONAL'}">
                <c:if test="${resume.getSections(sectionType).getContent() != null}">
                <%=((SimpleTextSection) resume.getSections(sectionType)).getContent()%>
                </c:if>
                <c:if test="${(resume.getSections(sectionType)).getContent() == null}">
                    <%=sectionType%>
                </c:if>

            </c:when>
            <c:when test="${sectionType=='QUALIFICATION' || sectionType=='ACHIEVEMENTS'}">
                <c:if test="${(resume.getSections(sectionType)).getContent() != null}">
                    <%=String.join("\n", ((BulletedListSection) resume.getSections(sectionType)).getContent())%>
                </c:if>
                <c:if test="${(resume.getSections(sectionType)).getContent() == null}">
                    <%=sectionType%>
                </c:if>
            </c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </p>
</c:forEach>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
