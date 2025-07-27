package dev.rtko_ai.hr_service.controller;
import dev.rtko_ai.hr_service.dto.CreateLeaveRequestDto;
import dev.rtko_ai.hr_service.model.Leave;
import dev.rtko_ai.hr_service.model.LeaveStatus;
import dev.rtko_ai.hr_service.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    @PostMapping
    public ResponseEntity<Leave> createLeave(@Valid @RequestBody CreateLeaveRequestDto dto) {
        Leave createdLeave = leaveService.createLeave(dto);
        return ResponseEntity.status(201).body(createdLeave);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Leave> updateLeaveStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        LeaveStatus status = LeaveStatus.valueOf(body.get("status"));
        Leave updatedLeave = leaveService.updateLeaveStatus(id, status);
        return ResponseEntity.ok(updatedLeave);
    }
    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/employee/{employeeId}/remaining")
    public ResponseEntity<Map<String, Integer>> getRemainingDays(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getRemainingLeaveDays(employeeId));
    }
}
