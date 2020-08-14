package com.urise.webapp.model;

public enum SectionType {
    PERSONAL("Личный данные"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENTS("Достижения"),
    QUALIFICATION("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return '\n' + title + ": ";
    }
}
