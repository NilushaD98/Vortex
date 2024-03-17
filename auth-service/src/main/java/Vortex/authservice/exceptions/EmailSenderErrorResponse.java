package Vortex.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE,reason = "The Email Service Unavailable")
public class EmailSenderErrorResponse extends RuntimeException{
}
