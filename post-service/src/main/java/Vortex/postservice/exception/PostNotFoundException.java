package Vortex.postservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "post not founs",value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {
}
