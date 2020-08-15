package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection implements Section {
    List<Organization> content;

    public OrganizationSection(List<Organization> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
