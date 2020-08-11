package com.urise.webapp.model;

import java.time.YearMonth;

public class Organization {
    String name;
    YearMonth yearFrom, yearTo;
    String info;

    public Organization(String name, YearMonth yearFrom, YearMonth yearTo, String info){
        this.name = name;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public YearMonth getYearFrom() {
        return yearFrom;
    }

    public YearMonth getYearTo() {
        return yearTo;
    }

    public String getName() {
        return name;
    }
}
