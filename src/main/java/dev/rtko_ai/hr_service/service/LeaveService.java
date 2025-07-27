package dev.rtko_ai.hr_service.service;

import dev.rtko_ai.hr_service.dto.CreateLeaveRequestDto;
import dev.rtko_ai.hr_service.model.Leave;
import dev.rtko_ai.hr_service.model.LeaveStatus;

import java.util.List;
import java.util.Map;

public interface LeaveService {
    Leave createLeave(CreateLeaveRequestDto dto);
    Leave updateLeaveStatus(Long leaveId, LeaveStatus newStatus);
    Map<String, Integer> getRemainingLeaveDays(Long employeeId);
    List<Leave> getAllLeaves();
}
