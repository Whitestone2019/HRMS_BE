/**
 * 
 */
package com.whitestone.hrms.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitestone.entity.ErrorMessage;
import com.whitestone.hrms.repo.ErrorMessageRepository;

@Service
public class ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;
    private static final String DEFAULT_LANGUAGE = "en";
    
    @Autowired
    public ErrorMessageService(ErrorMessageRepository errorMessageRepository) {
        this.errorMessageRepository = errorMessageRepository;
    }

    // Get the error message by error code and language
    public String getErrorMessage(String errorCode, String languageCode) {
        Optional<ErrorMessage> errorMessage = errorMessageRepository.findByErrorCodeAndLanguageCode(errorCode, languageCode);
        return errorMessage.map(ErrorMessage::getErrorDescription)
                           .orElse("Error message not found");
    }
    
    // Fallback to default language if the error message is not found in the requested language
    private String fallbackToDefaultLanguage(String errorCode) {
        return errorMessageRepository.findByErrorCodeAndLanguageCode(errorCode, DEFAULT_LANGUAGE)
                                     .map(ErrorMessage::getErrorDescription)
                                     .orElse("Error message not found");
    }
}