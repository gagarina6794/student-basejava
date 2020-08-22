package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Comparable<Organization> {
    private List<Experience> experiences;
    private String link;
    private String organizationName;

    public Organization(){
    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!Objects.equals(link, that.link)) return false;
        if (!Objects.equals(organizationName, that.organizationName)) return false;
        return  experiences.equals(that.experiences);
    }

    @Override
    public int hashCode() {
        int result = experiences != null ? experiences.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        return result;
    }
}
