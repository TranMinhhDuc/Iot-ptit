package com.iot.backend.Service;

import com.iot.backend.Model.Measure;
import com.iot.backend.Repository.MeasureRepository;
import com.iot.backend.Specification.MeasureSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MeasureService {
    @Autowired
    MeasureRepository measureRepository;

    public Page<Measure> getPage(int page, int pageSize, String searchBy, String searchValue, String sortBy, String direction) {

        Sort sort = "asc".equalsIgnoreCase("direction") ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Measure> spec = MeasureSpecification.searchDevice(searchBy, searchValue);

        return measureRepository.findAll(spec, pageable);
    }
}
