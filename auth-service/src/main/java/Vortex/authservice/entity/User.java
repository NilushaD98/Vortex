package Vortex.authservice.entity;

import Vortex.authservice.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "user")
public class User implements UserDetails {


    @Id
    private String userid;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private Date birthDay;
    @NonNull
    private String email;
    @NonNull
    private String contact;
    @NonNull
    private String country;
    @NonNull
    private String deliveryAddress;
    private String nic;
    private String password;
    private String profilePhotoURL;
    private String bio;
    @NonNull
    private Roles role;

    public User(@NonNull String firstName, @NonNull String lastName, @NonNull Date birthDay, @NonNull String email, @NonNull String contact, @NonNull String country, @NonNull String password, @NonNull Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.email = email;
        this.contact = contact;
        this.country = country;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
