package com.whitestone.hrms.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitestone.hrms.repo.ExpenseDetailsModRepository;
import com.whitestone.hrms.repo.ExpenseDetailsRepository;

@Service
public class ExpenseDetailsService {

	@Autowired
	private ExpenseDetailsRepository expenseDetailsRepository;

	@Autowired
	private ExpenseDetailsModRepository expenseDetailsModRepository;

	public BigDecimal getTotalExpenses() {
		BigDecimal pendingExpenses = expenseDetailsRepository.getTotalPendingExpenses();
		BigDecimal approvedAdvances = expenseDetailsRepository.getTotalApprovedExpenses();

		return pendingExpenses.add(approvedAdvances); // Sum of pending expenses and approved advances
	}

	public BigDecimal getTotalPendingExpenses() {
		return expenseDetailsRepository.getTotalPendingExpenses();
	}

	public BigDecimal getTotalApprovedExpenses() {
		return expenseDetailsRepository.getTotalApprovedExpenses();
	}

	public BigDecimal getTotalOpenAdvances() {
		return expenseDetailsRepository.getTotalOpenAdvances();
	}
}
