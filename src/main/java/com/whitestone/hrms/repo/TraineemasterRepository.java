package com.whitestone.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.TraineeMaster;

@EnableJpaRepositories(basePackages = "com.whitestone.hrms.repo")

@Repository
public interface TraineemasterRepository extends JpaRepository<TraineeMaster, String> {
	
	@Query("SELECT u FROM TraineeMaster u WHERE u.trngid = :Trngid OR u.userid = :Trngid")
	Optional<TraineeMaster> findByTrngidOrUserId(@Param("Trngid") String Trngid);

	@Query("SELECT u FROM TraineeMaster u WHERE u.repoteTo = :Trngid")
	List<TraineeMaster> findByRepoteToCustom(@Param("Trngid") String Trngid);

	@Query("SELECT u FROM TraineeMaster u WHERE u.repoteTo = :repoteTo")
    List<TraineeMaster> findByRepoteTo(String repoteTo);
    // You can define custom query methods here if needed.
    
    TraineeMaster findByTrngid(String Trngid);
    
	List<TraineeMaster> findByTrngidIn(List<String> Trngids);
	
    boolean existsByTrngid(String trngid);

}