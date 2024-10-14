package Vortex.marketplace_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(EmailSenderErrorResponse.class)
    public ResponseEntity<Object> handleEmailSenderErrorResponse(
            EmailSenderErrorResponse ex,WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("The Email Service Not Available");
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public ResponseEntity<Object> handleItemUnavailableException(
            ItemUnavailableException ex,WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The Item was Unavailable");
    }

}
