package Vortex.authservice.dto;

import java.util.Date;

public class OTP {
    private String id;
    private String userEmail;
    private String otp;
    private Date otpGeneratedTime;
    private Date otpExpirationTime;
    private boolean otpExpiredStatus;
}
