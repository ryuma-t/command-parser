package org.tatemichi.api.commandparser.exception;

public enum ErrorCode {

	DUPLICATE("Duplicate parameter name."),
	INVALID_FIELD("Field type is invalid."),
	
	;

	private String message;

	private ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
