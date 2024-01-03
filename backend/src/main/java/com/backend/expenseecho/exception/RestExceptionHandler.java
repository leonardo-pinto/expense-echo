package com.backend.expenseecho.exception;

import com.backend.expenseecho.model.dto.Error.ApiErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(BAD_REQUEST, ex);
        return buildResponseEntity(apiErrorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(BadCredentialsException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(UNAUTHORIZED, ex);
        return buildResponseEntity(apiErrorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(NOT_FOUND, ex);
        return buildResponseEntity(apiErrorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(UNAUTHORIZED, ex);
        return buildResponseEntity(apiErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(BAD_REQUEST);
        apiErrorResponse.setMessage("Validation error");
        apiErrorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());

        return buildResponseEntity(apiErrorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }
}
