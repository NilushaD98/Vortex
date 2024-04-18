package Vortex.postservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(reason = "comment not found",value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException{

}
