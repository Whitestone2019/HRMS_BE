package com.whitestone.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.TravelEntityMod;

@Repository

public interface TravelModRepository extends JpaRepository<TravelEntityMod, String> {
    
	@Query("SELECT t FROM TravelEntityMod t WHERE t.delFlg = 'Y'")
    List<TravelEntityMod> findByDelFlg(String delFlg);
}
