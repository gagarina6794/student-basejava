package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class BulletedListSection extends Section {
    private List<String> content;

    public BulletedListSection(List<String> content){
        this.content = content;
    }

    public BulletedListSection() {
    }

    @Override
    public String toString() {
        return content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BulletedListSection that = (BulletedListSection) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
