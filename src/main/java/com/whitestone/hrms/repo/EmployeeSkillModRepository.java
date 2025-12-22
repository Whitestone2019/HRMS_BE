package com.whitestone.hrms.repo;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeSkillMod;

@Repository
public interface EmployeeSkillModRepository extends JpaRepository<EmployeeSkillMod, String> {
	 // Query to get the max serial number for a user's skills
    @Query(value = "SELECT COALESCE(MAX(e.SRL_NUM), 0) FROM HRMSUSER.EMPLOYEE_SKILL_MOD_TABLE e WHERE e.user_id = ?1", nativeQuery = true)
    Long findSkillMaxSerialNumber(Long userId);

    // JPA Query to find skills by userId (use entity name, not table name)
    @Query("SELECT e FROM EmployeeSkillMod e WHERE e.userid = :userid")
    List<EmployeeSkillMod> findByUserid(@Param("userid") Long userid);
    
    void deleteByUserid(Long userid);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EmployeeSkillMod s WHERE s.userid = :userid")
    void deleteAllByUserid(@Param("userid") Long userid);
}