package com.urise.webapp.model;

import com.urise.webapp.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Comparable<Organization>, Serializable {
    private static final long serialVersionUID = 1L;
    public static final Organization EMPTY = new Organization("", new Link("", ""), Experience.EMPTY);
    private List<Experience> experiences = new ArrayList<>();
    private Link link;
    private String organizationName;

    public Organization() {
    }

    public Organization(String organizationName, Link link) {
        this.organizationName = organizationName;
        this.link = link;
    }

    public Organization(String organizationName, Link link, Experience... experiences) {
        this(organizationName, link, Arrays.asList(experiences));
    }

    public Organization(String organizationName, Link link, List<Experience> experiences) {
        this.organizationName = organizationName;
        this.link = link;
        this.experiences = experiences;
    }


    public Link getLink() {
        return link;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

    @Override
    public int compareTo(Organization o) {
        return organizationName.compareTo(o.getOrganizationName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!Objects.equals(link, that.link)) return false;
        if (!Objects.equals(organizationName, that.organizationName)) return false;
        return experiences.equals(that.experiences);
    }

    @Override
    public int hashCode() {
        int result = experiences != null ? experiences.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "\n*Organization name: '" + organizationName + '\'' +
                "\n   link: " + link + '\n'
                + experiences + '\'' + "";
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Experience implements Serializable {
        private static final long serialVersionUID = 1L;
        public static final Experience EMPTY = new Experience();

        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth yearFrom;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth yearTo;
        private String info;
        private String title;

        public Experience() {
        }

        public Experience(int startYear, Month startMonth, String title, String info) {
            this(YearMonth.of(startYear, startMonth), YearMonth.now(), title, info);
        }

        public Experience(int startYear, Month startMonth, int endYear, Month endMonth, String title, String info) {
            this(YearMonth.of(startYear, startMonth), YearMonth.of(endYear, endMonth), title, info);
        }

        public Experience(YearMonth yearFrom, YearMonth yearTo, String title, String info) {
            Objects.requireNonNull(yearFrom, "yearFrom must not be null");
            Objects.requireNonNull(yearTo, "yearTo must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.yearFrom = yearFrom;
            this.yearTo = yearTo;
            this.title = title;
            this.info = (info == null) ? "" : info;
        }

        public String getInfo() {
            return info;
        }

        public String getTitle() {
            return title;
        }

        public YearMonth getYearFrom() {
            return yearFrom;
        }

        public YearMonth getYearTo() {
            return yearTo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Experience that = (Experience) o;

            if (!Objects.equals(yearFrom, that.yearFrom)) return false;
            if (!Objects.equals(yearTo, that.yearTo)) return false;
            if (!Objects.equals(info, that.info)) return false;
            return Objects.equals(title, that.title);
        }

        @Override
        public int hashCode() {
            int result = yearFrom != null ? yearFrom.hashCode() : 0;
            result = 31 * result + (yearTo != null ? yearTo.hashCode() : 0);
            result = 31 * result + (info != null ? info.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "\n   yearFrom: " + yearFrom +
                    "\n   yearTo: " + yearTo +
                    "\n   title: " + title +
                    "\n   info: " + info + '\'' + "";
        }
    }
}
