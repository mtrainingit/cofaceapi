package com.coface.usuario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    ResponseEntity<?> recursoNoEncontradoException(RecursoNoEncontradoException exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictoCampoUnicoException.class)
    ResponseEntity<?> recursoNoEncontradoException(ConflictoCampoUnicoException exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> badCredentialsException(BadCredentialsException exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    ResponseEntity<?> InsufficientAuthenticationException(InsufficientAuthenticationException exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> cualquierException(Exception exception, WebRequest request) {
        DetalleDeError detalleDeError = new DetalleDeError(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(detalleDeError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
