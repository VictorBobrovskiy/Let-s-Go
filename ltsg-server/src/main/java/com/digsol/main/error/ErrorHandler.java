package com.digsol.main.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({
            ConstraintViolationException.class, ValidationException.class,
            MethodArgumentNotValidException.class, IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final Exception e) {

        log.error("----- Exception " + e.getClass() + " caused BAD_REQUEST status");

        return new ErrorResponse(
                getStackTrace(e),
                e.getMessage(),
                "Incorrectly made request",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {

        log.error("----- Error " + e.getClass() + " caused NOT_FOUND status");

        return new ErrorResponse(
                getStackTrace(e),
                e.getMessage(),
                "Not found",
                HttpStatus.NOT_FOUND.name(),
                LocalDateTime.now().format(FORMATTER)
        );
    }


    @ExceptionHandler({
            AccessDeniedException.class,
            DataIntegrityViolationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEditAccess(final Exception e) {

        log.error("----- Error " + e.getClass() + " caused CONFLICT status");

        return new ErrorResponse(
                getStackTrace(e),
                e.getMessage(),
                "Edit access error",
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now().format(FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleError(final Throwable e) {

        log.error("----- Error " + e.getClass() + " caused INTERNAL_SERVER_ERROR status");

        return new ErrorResponse(
                getStackTrace(e),
                e.getMessage(),
                "Ooops, something went really wrong",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now().format(FORMATTER)
        );
    }

    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}