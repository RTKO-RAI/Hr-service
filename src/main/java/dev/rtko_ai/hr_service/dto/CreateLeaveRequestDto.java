package dev.rtko_ai.hr_service.dto;
import dev.rtko_ai.hr_service.model.LeaveType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateLeaveRequestDto {
    @NotNull(message = "Employee ID must not be null")
    private Long employeeId;

    @NotNull(message = "Leave type must be specified")
    private LeaveType type;

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;
}
