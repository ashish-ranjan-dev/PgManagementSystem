package com.pgmanagement.authservice.exception;

public final class AuthExceptions {

    private AuthExceptions() {
        // Prevent instantiation — this is a container for exception types only.
    }

    public static class PhoneAlreadyExistsException extends RuntimeException {
        public PhoneAlreadyExistsException(String phone) {
            super("Phone number already registered: " + phone);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException() {
            super("Invalid phone or password");
        }
    }

    public static class PgServiceException extends RuntimeException {
        public PgServiceException(String message) {
            super(message);
        }

        public PgServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}