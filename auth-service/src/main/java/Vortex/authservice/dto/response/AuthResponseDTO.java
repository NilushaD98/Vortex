package Vortex.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private String role;
}
