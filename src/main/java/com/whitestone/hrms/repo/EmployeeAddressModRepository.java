package com.whitestone.hrms.repo;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeAddressMod;

@Repository
public interface EmployeeAddressModRepository extends JpaRepository<EmployeeAddressMod, Long> {
	void deleteByUserid(Long userid);
	Optional<EmployeeAddressMod> findByUserid(Long userid);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM EmployeeAddressMod a WHERE a.userid = :userid")
    void deleteAllByUserid(@Param("userid") Long userid);
}