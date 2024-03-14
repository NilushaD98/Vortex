package Vortex.authservice.entity;

import Vortex.authservice.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "token")
public class Token {

    @Id
    private String id;
    private String token;
    private String userEmail;
    private TokenType tokenType = TokenType.BARER;
    private boolean revoked;
    private boolean expired;

    public Token(String token, String userEmail, boolean revoked, boolean expired) {
        this.token = token;
        this.userEmail = userEmail;
        this.revoked = revoked;
        this.expired = expired;
    }
    public Token(String token, String userEmail) {
        this.token = token;
        this.userEmail = userEmail;
        this.revoked = false;
        this.expired = false;
    }
}
