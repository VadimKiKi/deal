package ru.taratonov.deal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.taratonov.deal.dto.ErrorDTO;
import ru.taratonov.deal.exception.IllegalArgumentOfEnumException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public List<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Handle MethodArgumentNotValidException", ex);
        List<ErrorDTO> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                errors.add(new ErrorDTO(e.getField() + " " + e.getDefaultMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST)));
        return errors;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentOfEnumException.class, DateTimeParseException.class})
    public ErrorDTO handleLongException(RuntimeException ex) {
        log.error("Handle Exception", ex);
        return new ErrorDTO(ex.getCause().getCause().getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }



}