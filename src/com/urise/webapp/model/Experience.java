package com.urise.webapp.model;

import java.time.YearMonth;

public class Experience {
    YearMonth yearFrom;
    YearMonth yearTo;
    String info;


    public Experience(YearMonth yearFrom, YearMonth yearTo, String info) {
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


    @Override
    public String toString() {
        return "\n   yearFrom: " + yearFrom +
                "\n   yearTo: " + yearTo +
                "\n   info: " + info + '\'' + "";
    }
}
