package com.vingcard.athos.interview.exception;

import java.time.LocalDateTime;
import java.util.Objects;

public class CustomExceptionResponse {

	private LocalDateTime timestamp;
	private Boolean success = false;
	private int code;
	private String message;

	public CustomExceptionResponse(LocalDateTime timestamp,
	                               Boolean success,
	                               int code,
	                               String message) {
		this.timestamp = timestamp;
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		CustomExceptionResponse that = (CustomExceptionResponse) o;
		return code == that.code && Objects.equals(timestamp, that.timestamp) && Objects.equals(success, that.success) && Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(timestamp, success, code, message);
	}

	@Override
	public String toString() {
		return "CustomExceptionResponse{" +
				"timestamp=" + timestamp +
				", success=" + success +
				", code=" + code +
				", message='" + message + '\'' +
				'}';
	}
}
