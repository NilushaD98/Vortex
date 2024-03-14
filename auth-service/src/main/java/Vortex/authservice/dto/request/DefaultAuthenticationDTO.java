package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultAuthenticationDTO {
    private String username;
    private String password;
}
