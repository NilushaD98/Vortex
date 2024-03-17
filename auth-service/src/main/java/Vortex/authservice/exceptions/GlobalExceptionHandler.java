package Vortex.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(UserAlreadyReportedException.class)
    public ResponseEntity<Object> handleUserAlreadyReportedException(
            UserAlreadyReportedException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body("User already present in the database");
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found  in the database");
    }
    @ExceptionHandler(EmailSenderErrorResponse.class)
    public ResponseEntity<Object> handleEmailSenderErrorResponse(
            EmailSenderErrorResponse ex,WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("The Email Service Not Available");
    }
    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<Object> handleBadCredentialException(
            BadCredentialException ex,WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password Invalid");
    }

}
