package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.ErrorMessage;
import com.whitestone.entity.ErrorMessageId;

import java.util.Optional;
//The repository for interacting with the error_messages table
@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, ErrorMessageId> {

 // Custom query method to find by error code and language code
 Optional<ErrorMessage> findByErrorCodeAndLanguageCode(String errorCode, String languageCode);
}