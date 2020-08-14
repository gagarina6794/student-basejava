package com.urise.webapp.model;

public class SimpleTextSection implements Section {
    private String content;

    public SimpleTextSection(String information) {
        content = information;
    }

    @Override
    public String toString() {
        return content;
    }
}
