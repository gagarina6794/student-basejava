package com.urise.webapp.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class SimpleTextSection extends Section implements Writable{
    private static final long serialVersionUID = 1L;

    private String content;

    public String getContent() {
        return content;
    }

    public SimpleTextSection(){}

    public SimpleTextSection(String information) {
        content = information;
    }

    @Override
    public String toString() {
        return content;
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
    public void writeCollection(DataOutputStream dos) throws IOException {
        dos.writeUTF(getContent());
    }
}
