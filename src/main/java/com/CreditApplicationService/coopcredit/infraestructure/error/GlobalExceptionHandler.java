package com.CreditApplicationService.coopcredit.infraestructure.error;

import com.CreditApplicationService.coopcredit.domain.exception.DomainException;
import com.CreditApplicationService.coopcredit.infraestructure.error.exception.ErrorResponseFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles bean validation errors (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        StringBuilder message = new StringBuilder("Error de validación: ");

        for (FieldError field : ex.getBindingResult().getFieldErrors()) {
            message.append(field.getField())
                    .append(" ")
                    .append(field.getDefaultMessage())
                    .append("; ");
        }

        ProblemDetail problem = ErrorResponseFactory.createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                message.toString(),
                request
        );

        problem.setProperty("errors",
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(f -> Map.of(
                                "field", f.getField(),
                                "message", f.getDefaultMessage()
                        )).toList()
        );

        return problem;
    }

    /**
     * Domain business errors
     */
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(
            DomainException ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Business Rule Error",
                ex.getMessage(),
                request
        );
    }

    /**
     * Not found errors (JPA or service layer)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(
            EntityNotFoundException ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request
        );
    }

    /**
     * JSON parse errors
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonFormatError(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON",
                "El formato del cuerpo JSON es inválido.",
                request
        );
    }

    /**
     * Unauthorized / token errors
     */
    @ExceptionHandler(SecurityException.class)
    public ProblemDetail handleSecurityException(
            SecurityException ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Authentication Error",
                "Token inválido o expirado.",
                request
        );
    }

    /**
     * Access denied (roles)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.FORBIDDEN,
                "Access Denied",
                "No tienes permisos para realizar esta acción.",
                request
        );
    }

    /**
     * Anything else that was not caught above
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(
            Exception ex,
            WebRequest request
    ) {
        return ErrorResponseFactory.createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Ha ocurrido un error inesperado.",
                request
        );
    }
}
