package com.iot.backend.Service;

import com.iot.backend.DTO.MeasureDTO;
import com.iot.backend.Handler.DashboardHandler;
import com.iot.backend.Model.Devices;
import com.iot.backend.Model.Measure;
import com.iot.backend.Repository.DevicesRepository;
import com.iot.backend.Repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    DevicesRepository deviceRepository;
    @Autowired
    MeasureRepository measurementRepository;
    @Autowired
    private DashboardHandler dashboardHandler;

    private List<Measure> measurementHistoriesList = new ArrayList<>();

    public Devices createDeviceHistory(String s) {
        Devices newControlDevice = new Devices();
        if (s.charAt(0) == '1') {
            newControlDevice.setDeviceName("Light");
        } else if (s.charAt(0) == '2') {
            newControlDevice.setDeviceName("FAN");
        } else if (s.charAt(0) == '3') {
            newControlDevice.setDeviceName("AC");
        } else if (s.charAt(0) == '4') {
            newControlDevice.setDeviceName("LED4");
        } else if (s.charAt(0) == '5') {
            newControlDevice.setDeviceName("LED5");
        }
        System.out.println(s);
        newControlDevice.setAction(s.substring(1));
        newControlDevice.setActionDate();
        newControlDevice.setActionTime();

        dashboardHandler.sendMessage(s);

        return deviceRepository.save(newControlDevice);
    }


    public void createMeasurementHistory(MeasureDTO measure) {
        Measure measureCreation = new Measure();
        measureCreation.setTemperature(measure.getTemperature());
        measureCreation.setHumidity(measure.getHumidity());
        measureCreation.setBright(measure.getBright());
        measureCreation.setPressure(measure.getTemperature() + 30);
        measureCreation.setMeasureDate();
        measureCreation.setMeasureTime();

        dashboardHandler.sendMessage(measureCreation.toJson());
        measurementHistoriesList.add(measureCreation);
        System.out.println(measureCreation.toJson());

        if (measurementHistoriesList.size() >= 30) {
            measurementRepository.saveAll(new ArrayList<>(measurementHistoriesList));

            System.out.println("Saving 30 items of measurement history.");
            measurementHistoriesList.clear();
        }
    }

    public Page<Measure> getItemChart() {
        Pageable pageable = PageRequest.of(0,10,
                Sort.by(Sort.Order.desc("measurementDate"),
                        Sort.Order.desc("measurementTime")));

        return measurementRepository.findAll(pageable);
    }
}
