package ru.taratonov.deal.exception;

public class ApplicationNotFoundException extends RuntimeException{
    private final Long id;

    public static ApplicationNotFoundException createWith(Long id) {
        return new ApplicationNotFoundException(id);
    }

    private ApplicationNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Application '" + id + "' not found";
    }
}
