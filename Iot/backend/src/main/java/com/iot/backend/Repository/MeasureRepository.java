package com.iot.backend.Repository;

import com.iot.backend.Model.Measure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, String>, JpaSpecificationExecutor<Measure> {
}
