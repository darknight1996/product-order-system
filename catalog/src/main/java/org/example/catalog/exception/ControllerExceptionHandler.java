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
      EntityNotFoundException exception, WebRequest request) {
    String message = exception.getMessage();
    HttpStatus status = HttpStatus.NOT_FOUND;
    String path = request.getDescription(false);

    ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException exception, WebRequest request) {
    StringBuilder errors = new StringBuilder();

    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(fieldError -> errors.append(fieldError.getDefaultMessage()));

    String message = errors.toString();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = request.getDescription(false);

    ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  private ErrorResponse createErrorResponse(HttpStatus status, String message, String path) {
    return new ErrorResponse(
        LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
  }
}
