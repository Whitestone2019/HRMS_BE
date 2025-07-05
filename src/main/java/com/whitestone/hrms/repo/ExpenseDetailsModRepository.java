package com.whitestone.hrms.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.ExpenseDetailsMod;

@Repository
public interface ExpenseDetailsModRepository extends JpaRepository<ExpenseDetailsMod, String> {

 

	List<ExpenseDetailsMod> findByEmpId(String empid);

	List<ExpenseDetailsMod> findByEmpIdAndStatus(String empid, String string);
    
    List<ExpenseDetailsMod> findByEmpIdAndDelflg(String empid, String delflg);
    
    List<ExpenseDetailsMod> findByDelflg(String delflg);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseDetailsMod e WHERE del_flg = 'N' AND LOWER(status) = 'pending'")
    BigDecimal getTotalPendingExpenses();

	 @Query("SELECT e FROM ExpenseDetailsMod e WHERE e.status = :status")
    List<ExpenseDetailsMod> findByStatus(String status);

	 @Query("SELECT e FROM ExpenseDetailsMod e WHERE e.empId IN :empIds AND TRIM(e.delflg) = :delflg")
	 List<ExpenseDetailsMod> findByEmpIdInAndDelflg(@Param("empIds") List<String> empIds, @Param("delflg") String delflg);

//	List<ExpenseDetailsMod> findByEmpIdInAndDelflg(List<String> empIds, String delflg);


   
	


}
