package com.iot.backend.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "measurement")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private float temperature;
    private float bright;
    private float humidity;
    private LocalDate measureDate;
    private LocalTime measureTime;
    private float pressure;

    public void setMeasureDate() {
        this.measureDate = LocalDate.now();
    }

    public void setMeasureTime() {
        this.measureTime = LocalTime.now();
    }

    public String toJson() {
        return "{" +
                "\"temperature\":" + temperature + "," +
                "\"humidity\":" + humidity + "," +
                "\"bright\":" + bright + "," +
                "\"pressure\":" + pressure +
                "}";
    }
}
