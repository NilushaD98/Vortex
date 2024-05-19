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
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String email;
    private String contact;
    private String country;
    private String deliveryAddress;
    private String nic;
    private String password;
    private String profilePhotoURL;
    private String bio;
    private Roles role;

    public User(String firstName,  String lastName,  Date birthDay,  String email,  String contact,  String country, String password, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.email = email;
        this.contact = contact;
        this.country = country;
        this.password = password;
        this.role = role;
    }

    public User( String firstName,  String lastName,  String email, String password, String profilePhotoURL,  Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profilePhotoURL = profilePhotoURL;
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
