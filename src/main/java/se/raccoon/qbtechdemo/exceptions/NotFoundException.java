package se.raccoon.qbtechdemo.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
