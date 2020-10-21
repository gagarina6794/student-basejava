package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {

    public static void main(String[] args) {
        System.out.println(fillResume("uuid1", "Григорий Кислин"));
    }

    public static void fillBulltedListSection(String text, List<String> content) {
        StringBuilder data = new StringBuilder(text);
        while (data.length() > 0) {
            content.add(data.substring(0, data.indexOf("\n")));
            data.delete(0, data.indexOf("\n") + 1);
        }
    }

    private static void fillOrganizationSection(String text, List<Organization> content) {
        StringBuilder data = new StringBuilder(text);
        while (data.length() > 0) {
            String organization = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n") + 1);
            String link = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n") + 1);
            YearMonth date1 = YearMonth.parse(data.substring(0, data.indexOf("-")), DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf("-") + 1);
            YearMonth date2 = YearMonth.parse(data.substring(0, data.indexOf(" ")), DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf(" ") + 1);
            String title = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n") + 1);
            String info = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n") + 1);
            Organization newItem = new Organization(organization, new Link(organization, link));
            if (content.contains(newItem)) {
                content.get(content.indexOf(newItem)).addExperience(new Organization.Experience(date1, date2, title, info));
            } else {
                newItem.addExperience(new Organization.Experience(date1, date2, title, info));
                content.add(newItem);
            }
        }
    }

    public static Resume fillResume(String uuid, String fullName) {
        Resume testResume = new Resume(uuid, fullName);
      /*  List<Organization> educationContent = new ArrayList<>();
        fillOrganizationSection("Coursera\n" + "https://www.coursera.org/learn/progfun1\n" +
                "03/2013-05/2013 Слушатель\n" + "Functional Programming Principles in Scala\" by Martin Odersky\n" +
                "Luxoft\n" + "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366\n" +
                "03/2011-04/2011 Слушатель\n"+ "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"\n" +
                "Siemens AG\n" + "https://new.siemens.com/ru/ru.html\n" +
                "01/2005-04/2005 Слушатель\n" +  "3 месяца обучения мобильным IN сетям (Берлин)\n" +
                "Alcatel\n" + "http://www.alcatel.ru/\n" +
                "09/1997-03/1998 Слушатель\n" +  " 6 месяцев обучения цифровым телефонным сетям (Москва)\n" +
                "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики\n" + "http://www.ifmo.ru/\n" +
                "09/1993-07/1996 Аспирантура (программист С, С++)\n" + " Аспирантура (программист С, С++)\n"+
                "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики\n" + "http://www.ifmo.ru/\n" +
                "09/1987-07/1993 Инженер (программист Fortran, C)\n" +"Инженер (программист Fortran, C)\n" +
                "Заочная физико-техническая школа при МФТИ\n" + "p://www.school.mipt.ru/Images/Title.png\n" +
                "09/1984-06/1987  Слушатель\n" + "Закончил с отличием\n", educationContent);

        List<Organization> experienceContent = new ArrayList<>();
        fillOrganizationSection("Java Online Projects\n" + "https://javaops.ru/\n" +
                "10/2013-08/2020 Автор проекта.\n" +
                "Создание, организация и проведение Java онлайн проектов и стажировок.\n" +
                "Wrike\n" + "https://www.wrike.com/\n" +
                "10/2014-01/2016 Старший разработчик (backend)\n " +
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.\n" +
                "RIT Center\n" + "doesn't have web site\n" +
                "04/2012-10/2014 Java архитектор\n" +
                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python\n" +
                "Luxoft (Deutsche Bank)\n" + "http://www.luxoft.ru/\n" +
                "12/2010-04/2012 Ведущий программист\n" +
                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.\n" +
                "Yota\n" + "https://www.yota.ru/\n" +
                "06/2008-12/2010 Ведущий специалист\n" +
                "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)\n" +
                "Enkata\n" + "http://enkata.com/\n" +
                "03/2007-06/2008 Разработчик ПО\n" +
                "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).\n" +
                "Siemens AG\n" + "https://new.siemens.com/ru/ru.html\n" +
                "01/2005-02/2007 Разработчик ПО\n" +
                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).\n" +
                "Alcatel\n" + "http://www.alcatel.ru/\n" +
                "09/1997-01/2005 Инженер по аппаратному и программному тестированию\n" +
                "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).\n", experienceContent);
        testResume.getSections().put(SectionType.EDUCATION, new OrganizationSection(educationContent));

        testResume.getSections().put(SectionType.EXPERIENCE, new OrganizationSection(experienceContent));
*/
        List<String> achievementsContent = new ArrayList<>();
        fillBulltedListSection("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.\n" +
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.\n" +
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.\n" +
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.\n" +
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).\n" +
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.\n", achievementsContent);

        List<String> qualificationContent = new ArrayList<>();
        fillBulltedListSection("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2\n" +
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce\n" +
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,\n" +
                "MySQL, SQLite, MS SQL, HSQLDB\n" +
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,\n" +
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,\n" +
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).\n" +
                "Python: Django.\n" +
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js\n" +
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka\n" +
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.\n" +
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,\n" +
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.\n" +
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования\n" +
                "Родной русский, английский \"upper intermediate\"\n", qualificationContent);



        testResume.getSections().put(SectionType.ACHIEVEMENTS, new BulletedListSection(achievementsContent));

        testResume.getSections().put(SectionType.QUALIFICATION, new BulletedListSection(qualificationContent));

        testResume.getSections().put(SectionType.OBJECTIVE, new SimpleTextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        testResume.getSections().put(SectionType.PERSONAL, new SimpleTextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));


        testResume.getContacts().put(ContactType.EMAIL, "gkisilin@yandex.ru");
        testResume.getContacts().put(ContactType.GITHUB, "https://github.com/gkislin");
        testResume.getContacts().put(ContactType.SKYPE, "grigory.kislin");
        testResume.getContacts().put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        testResume.getContacts().put(ContactType.MOBILEPHONE, "+7(921) 855-0482");
        return testResume;
    }
}
