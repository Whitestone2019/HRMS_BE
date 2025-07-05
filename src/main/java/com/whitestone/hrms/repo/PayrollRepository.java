package com.whitestone.hrms.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
	
	@Query("SELECT p FROM Payroll p WHERE p.month = :month")
	List<Payroll> findByMonth(@Param("month") String month);
	@Transactional
	@Modifying
	@Query("DELETE FROM Payroll p WHERE p.month = :month")
	void deleteByMonth(@Param("month") String month);
	
	@Query(value = "SELECT COUNT(id) FROM payroll WHERE emp_id = :empId AND month = :month", nativeQuery = true)
	int countByEmpidAndMonth(@Param("empId") String empId, @Param("month") String month);

	default boolean existsByEmpidAndMonth(String empId, String month) {
	    return countByEmpidAndMonth(empId, month) > 0;
	}



}
