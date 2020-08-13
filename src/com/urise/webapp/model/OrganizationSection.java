package com.urise.webapp.model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrganizationSection implements Section{
    HashMap<String, List<Organization>> content;

    public OrganizationSection(String text) {
        content = new HashMap<>();
        fillSection(text);
    }

    private void fillSection(String text){
        StringBuilder data = new StringBuilder(text);
        while (data.length() > 0){
            String organization = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n")+1);
            YearMonth date1 = YearMonth.parse(data.substring(0, data.indexOf("-")), DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf("-")+1);
            YearMonth date2 = YearMonth.parse(data.substring(0, data.indexOf(" ")),DateTimeFormatter.ofPattern("M/uuuu"));
            data.delete(0, data.indexOf(" ")+1);
            String info = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n")+1);
            if (content.containsKey(organization)){
                content.get(organization).add(new Organization(organization,date1,date2,info));
            } else{
                List<Organization> newList =new ArrayList<>();
                newList.add(new Organization(organization,date1,date2,info));
                content.put(organization,newList);
            }
        }

    }
}
