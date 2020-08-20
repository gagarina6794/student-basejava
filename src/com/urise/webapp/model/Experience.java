package com.urise.webapp.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

public class Experience implements Serializable {
    private YearMonth yearFrom;
    private YearMonth yearTo;
    private String info;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (yearFrom.compareTo(that.yearFrom) < 0) return false;
        if (!Objects.equals(yearTo, that.yearTo)) return false;
        return info.equals(that.info);
    }

    @Override
    public int hashCode() {
        int result = yearFrom != null ? yearFrom.hashCode() : 0;
        result = 31 * result + (yearTo != null ? yearTo.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }
}
