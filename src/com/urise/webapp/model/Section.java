package com.urise.webapp.model;

public interface Section {
    SectionType sectionType = null;

    void fillSection(String information);

    void clearSection();
}
