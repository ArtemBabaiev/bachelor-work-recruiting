package edu.chnu.recruiting.exceptions;

public class AlreadyExistsException extends RuntimeException {

	public AlreadyExistsException() {
		this("User with such email/username already in use");
	}

	public AlreadyExistsException(String message) {
		super(message);
	}
}
