package com.iot.backend.Controller;

import com.iot.backend.Model.Devices;
import com.iot.backend.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping("/device-page")
    public Page<Devices> getDevicePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchBy,
            @RequestParam(required = false) String searchValue,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortValue
    ){
        return deviceService.getPage(page, pageSize, searchBy, searchValue, sortBy, sortValue);
    }
}
