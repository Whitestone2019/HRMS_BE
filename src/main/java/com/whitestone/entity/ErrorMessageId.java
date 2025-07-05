package com.whitestone.entity;

import java.io.Serializable;
import java.util.Objects;

//This class defines the composite key for the error_messages table
public class ErrorMessageId implements Serializable {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String languageCode;

	// Default constructor
	public ErrorMessageId() {
	}

	// Constructor
	public ErrorMessageId(String errorCode, String languageCode) {
		this.errorCode = errorCode;
		this.languageCode = languageCode;
	}

	// Getters, Setters, hashCode, and equals methods
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ErrorMessageId that = (ErrorMessageId) o;
		return Objects.equals(errorCode, that.errorCode) && Objects.equals(languageCode, that.languageCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(errorCode, languageCode);
	}
}