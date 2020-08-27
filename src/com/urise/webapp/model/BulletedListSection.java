package com.urise.webapp.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BulletedListSection extends Section implements Writable {
    private static final long serialVersionUID = 1L;

    private List<String> content;

    public BulletedListSection() {
    }

    public BulletedListSection(String... items) {
        this(Arrays.asList(items));
    }

    public BulletedListSection(List<String> content) {
        Objects.requireNonNull(content, "content must not be null");
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

        BulletedListSection that = (BulletedListSection) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    public List<String> getContent() {
        return content;
    }


    @Override
    public void writeCollection(DataOutputStream dos) throws IOException {
        dos.writeInt(getContent().size());
        for (String item : getContent()) {
            dos.writeUTF(item);
        }
    }
}

