package dev.rtko_ai.hr_service.service;

import dev.rtko_ai.hr_service.dto.CreateLeaveRequestDto;

import dev.rtko_ai.hr_service.model.LeaveStatus;
import dev.rtko_ai.hr_service.model.LeaveType;
import dev.rtko_ai.hr_service.exception.ResourceNotFoundException;
import dev.rtko_ai.hr_service.model.Leave;
import dev.rtko_ai.hr_service.repository.LeaveRepository;
import dev.rtko_ai.hr_service.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private static final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);

    @Override
    public Leave createLeave(CreateLeaveRequestDto dto) {
        log.info("Creating leave request for employee {}", dto.getEmployeeId());

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        boolean overlapExists = leaveRepository.findByEmployeeId(dto.getEmployeeId()).stream()
                .filter(leave -> leave.getStatus() == LeaveStatus.APPROVED)
                .anyMatch(leave ->
                        !dto.getEndDate().isBefore(leave.getStartDate()) &&
                                !dto.getStartDate().isAfter(leave.getEndDate())
                );

        if (overlapExists) {
            log.warn("Rejected leave creation: overlapping approved leave for employee {} from {} to {}",
                    dto.getEmployeeId(), dto.getStartDate(), dto.getEndDate());
            throw new IllegalArgumentException("Overlapping approved leave request exists for this period.");
        }

        Leave leave = Leave.builder()
                .employeeId(dto.getEmployeeId())
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(LeaveStatus.PENDING)
                .build();

        return leaveRepository.save(leave);
    }

    @Override
    public Leave updateLeaveStatus(Long leaveId, LeaveStatus newStatus) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave request with ID " + leaveId + " not found"));

        leave.setStatus(newStatus);
        return leaveRepository.save(leave);
    }

    @Override
    public Map<String, Integer> getRemainingLeaveDays(Long employeeId) {
        List<Leave> approvedLeaves = leaveRepository.findByEmployeeId(employeeId).stream()
                .filter(leave -> leave.getStatus() == LeaveStatus.APPROVED)
                .toList();

        if (approvedLeaves.isEmpty()) {
            throw new ResourceNotFoundException("Leave request with ID " + employeeId + " not found");
        }

        Map<LeaveType, Integer> usedDays = new EnumMap<>(LeaveType.class);
        for (Leave leave : approvedLeaves) {
            int days = (int) ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            usedDays.merge(leave.getType(), days, Integer::sum);
        }

        Map<String, Integer> remaining = new HashMap<>();
        for (LeaveType type : LeaveType.values()) {
            int allowed = getMaxAllowed(type);
            int used = usedDays.getOrDefault(type, 0);
            int left = Math.max(allowed - used, 0);
            remaining.put(type.name().toLowerCase(), left);
        }

        return remaining;
    }

    @Override
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }
    private int getMaxAllowed(LeaveType type) {
        return switch (type) {
            case ANNUAL -> 20;
            case SICK -> 10;
            case UNPAID -> Integer.MAX_VALUE;
        };
    }
}
