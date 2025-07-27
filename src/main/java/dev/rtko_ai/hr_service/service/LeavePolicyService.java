import dev.arianit.hrservice.model.LeaveType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LeavePolicyService {
    private final Map<LeaveType, Integer> leaveLimits = Map.of(
            LeaveType.ANNUAL, 20,
            LeaveType.SICK, 10,
            LeaveType.UNPAID, Integer.MAX_VALUE
    );

    public int getAllowedDays(LeaveType type) {
        return leaveLimits.getOrDefault(type, 0);
    }
}