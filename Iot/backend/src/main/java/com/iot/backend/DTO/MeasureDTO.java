package com.iot.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MeasureDTO {

    private float temperature;
    private float bright;
    private float humidity;
    private LocalDate measureDate;
    private LocalTime measureTime;

    public void setMeasureDate() {
        this.measureDate = LocalDate.now();
    }

    public void setMeasureTime() {
        this.measureTime = LocalTime.now();
    }
}
