package com.example.mohammadabumusarabiul.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeHelper {

    public boolean isWithinRange(LocalDateTime testDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return testDateTime.isBefore(startDateTime) &&
                (testDateTime.isEqual(endDateTime) || testDateTime.isAfter(endDateTime));
    }

    public boolean isInDeleteRange(LocalDateTime testDateTime, LocalDateTime endDateTime) {
        return testDateTime.isBefore(endDateTime);
    }
}
