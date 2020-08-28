package com.urise.webapp.model;

import java.util.Objects;

public class SimpleTextSection extends Section {
    private static final long serialVersionUID = 1L;

    private String content;

    public String getContent() {
        return content;
    }

    public SimpleTextSection() {
    }

    public SimpleTextSection(String information) {
        Objects.requireNonNull(information, "information must not be null");
        content = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTextSection that = (SimpleTextSection) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return content;
    }
}
