package com.iot.backend;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Test {
    public static void main(String[] args) {
        String timeString = "11:00:00";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            LocalTime time = LocalTime.parse(timeString, timeFormatter);
            System.out.println("LocalTime: " + time);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format: " + e.getMessage());
        }
    }
}
