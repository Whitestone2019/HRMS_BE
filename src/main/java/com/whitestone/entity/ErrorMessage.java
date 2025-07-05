package com.whitestone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
@Entity
@Table(name = "error_messages", schema = "HRMSUSER")
@IdClass(ErrorMessageId.class)
public class ErrorMessage {
    @Id
    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Id
    @Column(name = "language_code", length = 10)
    private String languageCode;

    @Column(name = "error_description", length = 255)
    private String errorDescription;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}    
}
