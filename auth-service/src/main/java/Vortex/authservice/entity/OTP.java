package Vortex.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "otp")
public class OTP {
    @Id
    private String id;
    private String userEmail;
    private String otp;
    private Date otpGeneratedTime;
    private Date otpExpirationTime;
    private boolean otpExpiredStatus;

    public OTP(String userEmail, String otp, Date otpGeneratedTime, Date otpExpirationTime, boolean otpExpiredStatus) {
        this.userEmail = userEmail;
        this.otp = otp;
        this.otpGeneratedTime = otpGeneratedTime;
        this.otpExpirationTime = otpExpirationTime;
        this.otpExpiredStatus = otpExpiredStatus;
    }
}
