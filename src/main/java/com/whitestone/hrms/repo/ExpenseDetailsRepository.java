package com.whitestone.hrms.repo;
import com.whitestone.entity.AdvancesDetailsMod;
import com.whitestone.entity.ExpenseDetails;
import com.whitestone.entity.ExpenseDetailsMod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.math.BigDecimal;

@Repository
public interface ExpenseDetailsRepository extends JpaRepository<ExpenseDetailsMod, String> {

	 @Query("SELECT e FROM ExpenseDetailsMod e WHERE e.delflg = 'N'")
	    List<ExpenseDetailsMod> findActiveExpenses();

	    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseDetailsMod e WHERE delflg = 'N'")
	    BigDecimal getTotalExpensesAmount();

	    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseDetailsMod e WHERE delflg = 'N' AND LOWER(status) = 'pending'")
	    BigDecimal getTotalPendingExpenses();

	    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseDetailsMod e WHERE delflg = 'N' AND LOWER(status) = 'approved'")
	    BigDecimal getTotalApprovedExpenses();

	    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AdvancesDetailsMod a WHERE del_flg = 'N' AND LOWER(status) = 'open'")
	    BigDecimal getTotalOpenAdvances();
	    
	    @Query("SELECT e FROM ExpenseDetailsMod e WHERE e.status = :status")
	    List<ExpenseDetailsMod> findByStatus(String status);



	}