package com.whitestone.hrms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.whitestone.entity.EmployeeProfile;
import com.whitestone.hrms.repo.EmployeeProfileRepository;

@Service
public class PayslipService {

	@Autowired
	private EmployeeProfileRepository employeeRepository;

	@Autowired
	private PayslipGenerator payslipGenerator; // Utility for PDF generation

	@Autowired
	private EmailService emailService;

	public void sendPayslipToAll(String month) {
		List<EmployeeProfile> employees = employeeRepository.findAll();
		for (EmployeeProfile employee : employees) {
			sendPayslip(employee.getEmpid(), month);
		}
	}

	public void sendPayslip(String employeeId, String month) {
		Optional<EmployeeProfile> employee = employeeRepository.findByEmpId(employeeId);
		if (employee == null) {
			throw new RuntimeException("Employee not found with ID: " + employeeId);
		}

		ByteArrayResource payslip = generatePayslip(employeeId, month);
		emailService.sendPayslipEmail(employee.get().getOfficialemail(), payslip, month);
	}

	public ByteArrayResource generatePayslip(String employeeId, String month) {
		byte[] pdfBytes = payslipGenerator.createPayslipPdf(employeeId, month);
		return new ByteArrayResource(pdfBytes);
	}
}
