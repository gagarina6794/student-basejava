package com.urise.webapp.model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrganizationSection implements Section{
    ArrayList<Organization> content;

    public OrganizationSection(String text) {
        content = new ArrayList<>();
        fillSection(text);
    }

    private void fillSection(String text){
        StringBuilder data = new StringBuilder(text);
        while (data.length() > 0){
            String organization = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n")+1);
            var xheck = data.substring(0, data.indexOf("-"));
            YearMonth date1 = YearMonth.parse(data.substring(0, data.indexOf("-")), DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf("-")+1);
            YearMonth date2 = YearMonth.parse(data.substring(0, data.indexOf(" ")),DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf(" ")+1);
            String info = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n")+1);
            content.add(new Organization(organization,date1,date2,info));
        }

    }
}
