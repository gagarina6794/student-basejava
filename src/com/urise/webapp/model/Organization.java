package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class Organization implements Comparable<Organization> {
    private List<Experience> experiences;
    private String link;
    private String organizationName;

    public Organization(String organizationName, String link) {
        this.organizationName = organizationName;
        this.link = link;
        experiences = new ArrayList<>();
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

    public String getLink() {
        return link;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public int compareTo(Organization o) {
        return organizationName.compareTo(o.getOrganizationName());
    }

    public String toString() {
        return "\n*Organization name: '" + organizationName + '\'' +
                "\n   link: " + link + '\n'
                + experiences + '\'' + "";
    }
}
