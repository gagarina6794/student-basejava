package com.urise.webapp.model;

public class Content implements Section {
    private String content;

    @Override
    public void fillSection(String information) {
        content = information;
    }

    @Override
    public void clearSection() {
        content = null;
    }
}
