package com.urise.webapp.model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrganizationSection implements Section {
    List<Organization> content;

    public OrganizationSection(String text) {
        content = new ArrayList<>();
        fillSection(text);
    }

    private void fillSection(String text) {
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
            String info = data.substring(0, data.indexOf("\n"));
            data.delete(0, data.indexOf("\n") + 1);
            Organization newItem = new Organization(organization, link);
            if (content.contains(newItem)) {
                content.get(content.indexOf(newItem)).addExperience(new Experience(date1, date2, info));
            } else {
                newItem.addExperience(new Experience(date1, date2, info));
                content.add(newItem);
            }
        }

    }

    @Override
    public String toString() {
        return content.toString();
    }
}
