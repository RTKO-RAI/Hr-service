package dev.rtko_ai.hr_service.repository;

import dev.rtko_ai.hr_service.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave,Long> {
    List<Leave> findByEmployeeId(Long employeeId);
}
