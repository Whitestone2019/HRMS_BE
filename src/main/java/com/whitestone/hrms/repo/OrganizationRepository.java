package com.whitestone.hrms.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	List<Organization> findByDelFlgNot(String delFlg);
	boolean existsByBusinessEmailAndDelFlg(String businessEmail, String delFlg);
	boolean existsByTaxIdAndDelFlg(String taxId, String delFlg);
    // You can define custom queries here if needed
}