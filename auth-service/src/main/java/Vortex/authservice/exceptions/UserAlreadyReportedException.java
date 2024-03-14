package Vortex.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED,reason = "user already present in the database")
public class UserAlreadyReportedException extends RuntimeException{
}
