package com.urise.webapp.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section implements Writable{
    private static final long serialVersionUID = 1L;

    private List<Organization> content;

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> content) {
        this.content = content;
    }

    public List<Organization> getContent() {
        return content;
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

    @Override
    public void writeCollection(DataOutputStream dos) throws IOException {
        dos.writeInt(getContent().size());
        for (var organization : getContent()) {
            dos.writeUTF(organization.getOrganizationName());
            dos.writeUTF(organization.getLink().getName());
            dos.writeUTF(organization.getLink().getUrl());
            dos.writeInt(organization.getExperiences().size());
            for (var experience : organization.getExperiences()) {
                dos.writeUTF(experience.getYearFrom().toString());
                dos.writeUTF(experience.getYearTo().toString());
                dos.writeUTF(experience.getTitle());
                dos.writeUTF(experience.getInfo());
            }
        }
    }
}
