package com.example.mohammadabumusarabiul.util;

import java.time.LocalDateTime;

public class DateTimeHelper {

    public boolean isWithinRange(LocalDateTime testDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (testDateTime.isEqual(startDateTime) || testDateTime.isBefore(startDateTime)) &&
                (testDateTime.isEqual(endDateTime) || testDateTime.isAfter(endDateTime));
    }

    public boolean isInDeleteRange(LocalDateTime testDateTime, LocalDateTime endDateTime) {
        return testDateTime.isEqual(endDateTime) || testDateTime.isBefore(endDateTime);
    }
}
