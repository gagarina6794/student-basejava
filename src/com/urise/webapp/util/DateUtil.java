package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

public class DateUtil {
    public static YearMonth NOW = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth());

    public static YearMonth of(int year, Month month){
        return YearMonth.of(year, month);
    }
}
