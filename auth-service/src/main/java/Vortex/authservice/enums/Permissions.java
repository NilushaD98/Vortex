package Vortex.authservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    SELLER_READ("seller:read"),
    SELLER_UPDATE("seller:update"),
    SELLER_CREATE("seller:create"),
    SELLER_DELETE("seller:delete"),
    ADMIN_READ("seller:read"),
    ADMIN_UPDATE("seller:update"),
    ADMIN_CREATE("seller:create"),
    ADMIN_DELETE("seller:delete"),

    ;

    @Getter
    private final String permission;
}
