package com.whitestone.hrms.service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.whitestone.entity.AdvancesDetailsMod;
import com.whitestone.hrms.repo.AdvancesDetailsModRepository;

@Service
public class AdvanceService {
	
	@Autowired
    private AdvancesDetailsModRepository advancesDetailsModRepository;

	public Map<String, BigDecimal> getAdvanceStats() {
        Map<String, BigDecimal> stats = new HashMap<>();

        // Fetch correct values from the database
        BigDecimal totalAdvances = advancesDetailsModRepository.getTotalAdvanceAmount();
        System.out.println("Total Advances from DB: " + totalAdvances);

        BigDecimal pendingAdvances = advancesDetailsModRepository.getTotalPendingAdvance();
        System.out.println("Pending Advances from DB: " + pendingAdvances);

        BigDecimal approvedAdvances = advancesDetailsModRepository.getApprovedAdvances(); // Get approved advances separately
        System.out.println("Approved Advances from DB: " + approvedAdvances);

        // Ensure values are not null
        totalAdvances = (totalAdvances != null) ? totalAdvances : BigDecimal.ZERO;
        pendingAdvances = (pendingAdvances != null) ? pendingAdvances : BigDecimal.ZERO;
        approvedAdvances = (approvedAdvances != null) ? approvedAdvances : BigDecimal.ZERO;

        // Ensure approvedAdvances is positive
        if (approvedAdvances.compareTo(BigDecimal.ZERO) < 0) {
            approvedAdvances = approvedAdvances.abs();
        }

        BigDecimal openAdvances = pendingAdvances; // Open Advances is the same as pending

        // Debugging logs
        System.out.println("Total Advances: " + totalAdvances);
        System.out.println("Pending Advances: " + pendingAdvances);
        System.out.println("Approved Advances (Corrected): " + approvedAdvances);
        System.out.println("Open Advances: " + openAdvances);

        stats.put("totalAdvances", totalAdvances);
        stats.put("pendingAdvances", pendingAdvances);
        stats.put("approvedAdvances", approvedAdvances);
        stats.put("openAdvances", openAdvances);

        return stats;
    }
}

