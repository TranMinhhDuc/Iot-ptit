package com.iot.backend.Service;

import com.iot.backend.Model.Devices;
import com.iot.backend.Repository.DevicesRepository;
import com.iot.backend.Specification.DeviceSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    DevicesRepository devicesRepository;

    public Page<Devices> getPage(int page, int pageSize, String searchBy, String searchValue, String sortBy, String direction){

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Devices> spec = DeviceSpecification.searchDevice(searchBy, searchValue);

        return devicesRepository.findAll(spec, pageable);
    }
}
