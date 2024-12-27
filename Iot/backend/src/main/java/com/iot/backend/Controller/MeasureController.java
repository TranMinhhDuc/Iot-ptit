package com.iot.backend.Controller;

import com.iot.backend.Model.Measure;
import com.iot.backend.Service.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/measure")
public class MeasureController {

    @Autowired
    MeasureService measureService;

    @GetMapping("/measure-page")
    public Page<Measure> getMeasurePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchBy,
            @RequestParam(required = false) String searchValue,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortValue
    ){
        return measureService.getPage(page, pageSize, searchBy, searchValue, sortBy, sortValue);
    }
}
