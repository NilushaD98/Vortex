package Vortex.userservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StandardResponse {

    private int code;
    private String message;
    private Object data;
}
