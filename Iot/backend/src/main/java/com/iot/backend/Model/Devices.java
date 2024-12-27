package com.iot.backend.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "device")
public class Devices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String deviceName;
    private String action;
    private LocalDate actionDate;
    private LocalTime actionTime;


    public void setActionDate() {
        this.actionDate = LocalDate.now();
    }

    public void setActionTime() {
        this.actionTime = LocalTime.now();
    }

}
