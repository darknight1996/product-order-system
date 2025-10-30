package org.example.catalog.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      final EntityNotFoundException exception, final WebRequest request) {
    final String message = exception.getMessage();
    final HttpStatus status = HttpStatus.NOT_FOUND;
    final String path = request.getDescription(false);

    final ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      final MethodArgumentNotValidException exception, final WebRequest request) {
    final StringBuilder errors = new StringBuilder();

    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(fieldError -> errors.append(fieldError.getDefaultMessage()));

    final String message = errors.toString();
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final String path = request.getDescription(false);

    final ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  private ErrorResponse createErrorResponse(
      final HttpStatus status, final String message, final String path) {
    return new ErrorResponse(
        LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
  }
}
