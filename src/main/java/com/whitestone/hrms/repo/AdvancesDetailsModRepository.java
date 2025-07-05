package com.whitestone.hrms.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.AdvancesDetailsMod;
import com.whitestone.entity.ExpenseDetailsMod;

@Repository
public interface AdvancesDetailsModRepository extends JpaRepository<AdvancesDetailsMod, String> {

    List<AdvancesDetailsMod> findByEmpIdAndDelFlg(String empId,String delFlg);
    
    List<AdvancesDetailsMod> findByDelFlg(String delFlg);
    
    @Query("SELECT e FROM AdvancesDetailsMod e WHERE e.empId IN :empIds AND TRIM(e.delFlg) = :delflg")
    List<AdvancesDetailsMod> findByEmpIdInAndDelflg(@Param("empIds") List<String> empIds, @Param("delflg") String delflg);


    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AdvancesDetailsMod a WHERE a.delFlg = 'N' AND LOWER(a.status) = 'pending'")
    BigDecimal getTotalPendingAdvance();

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AdvancesDetailsMod a WHERE a.delFlg = 'N' AND LOWER(a.status) = 'approved'")
    BigDecimal getApprovedAdvances();

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AdvancesDetailsMod a WHERE a.delFlg = 'N'")
    BigDecimal getTotalAdvanceAmount();

    
    @Query("SELECT a FROM AdvancesDetailsMod a WHERE a.empId IN :empIds AND a.delFlg = :delFlg")
    List<AdvancesDetailsMod> findByEmpIdInAndDelFlg(@Param("empIds") List<String> empIds, @Param("delFlg") String delFlg);
	//List<AdvancesDetailsMod> findByEmpIdAndDelflg(String empid, String delflg);

	//List<AdvancesDetailsMod> findByEmpIdInAndDelflg(List<String> empIds, String delflg);
}
