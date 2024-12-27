package com.iot.backend.Repository;

import com.iot.backend.Model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, String>, JpaSpecificationExecutor<Devices> {
}
