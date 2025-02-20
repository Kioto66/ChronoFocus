package ru.k4nk.chronofocus.http_api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ApiHelper {


    /**
     * Parse string date local date time with time of start or end of day.
     *
     * @param dateStr    the date string formatted "yyyy-MM-dd"
     * @param startOfDay the start of day, if true time will be set to 00:00:00 else to 23:59:59
     * @return the local date time with time 00:00:00 or 23:59:59
     */
    public static LocalDateTime parseStringDate(String dateStr, boolean startOfDay ) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid must not be null or empty");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return startOfDay
                    ? date.atStartOfDay()
                    : date.atTime(23, 59, 59);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        }
    }

    public static LocalDateTime parseStringDateTime(String dateTimeStr, boolean b) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid must not be null or empty");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        try {
            return  LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        }
    }
}
