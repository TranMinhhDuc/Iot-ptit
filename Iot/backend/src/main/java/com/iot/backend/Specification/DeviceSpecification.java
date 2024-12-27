package com.iot.backend.Specification;

import com.iot.backend.Model.Devices;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DeviceSpecification {

    public static Specification<Devices> searchDevice(String searchBy, String searchValue) {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra các giá trị null hoặc rỗng
            if (searchBy == null || searchBy.isEmpty() || searchValue == null || searchValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Xử lý khi tìm kiếm theo 'date'
            if ("date".equals(searchBy)) {
                if (searchValue.length() == 10) { // Chỉ có ngày (yyyy-MM-dd)
                    LocalDate date = LocalDate.parse(searchValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return criteriaBuilder.equal(root.get("actionDate"), date);
                }
                else if (searchValue.length() == 8) { // Chỉ có giờ phút giây (HH:mm:ss)
                    LocalTime time = LocalTime.parse(searchValue, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    return criteriaBuilder.equal(root.get("actionTime"), time);
                }
                // chỉ có tháng năm yyyy-MM
                else if(searchValue.length() == 7) {
                    LocalDate date = LocalDate.parse(searchValue, DateTimeFormatter.ofPattern("yyyy-MM"));
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("actionDate")), date.getYear()),
                            criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("actionDate")), date.getMonth())
                    );
                }
                else if (searchValue.length() == 5) { // Chỉ có giờ phút (HH:mm)
                    LocalTime time = LocalTime.parse(searchValue, DateTimeFormatter.ofPattern("HH:mm"));
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(criteriaBuilder.function("HOUR", Integer.class, root.get("actionTime")), time.getHour()),
                            criteriaBuilder.equal(criteriaBuilder.function("MINUTE", Integer.class, root.get("actionTime")), time.getMinute())
                    );
                }
                else if (searchValue.length() == 2) { // Chỉ có giờ (HH)
                    LocalTime time = LocalTime.parse(searchValue, DateTimeFormatter.ofPattern("HH"));
                    return criteriaBuilder.equal(criteriaBuilder.function("HOUR", Integer.class, root.get("actionTime")), time.getHour());
                }
                else { // Có cả năm tháng ngày giờ phút giây (yyyy-MM-dd'T'HH:mm:ss)
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(searchValue, DateTimeFormatter.ISO_DATE_TIME);
                        return criteriaBuilder.and(
                                criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get("actionDate")), dateTime.toLocalDate()),
                                criteriaBuilder.equal(criteriaBuilder.function("TIME", LocalTime.class, root.get("actionTime")), dateTime.toLocalTime())
                        );
                    } catch (Exception e) {
                        // Trường hợp không parse được, trả về điều kiện rỗng
                        return criteriaBuilder.conjunction();
                    }
                }
            }

            return criteriaBuilder.equal(root.get(searchBy), searchValue);
        };
    }
}
