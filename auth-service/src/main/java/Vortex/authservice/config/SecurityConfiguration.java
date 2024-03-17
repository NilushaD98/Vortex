package Vortex.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;

    public static final String[] permittedURL = {
            "/vortexcoreservice/api/v1/user/**",
            "/vortexcoreservice/api/v1/**",
            "/vortexcoreservice/api/v1/user/find_account_by_email",
            "/vortexcoreservice/api/v1/user/send_otp"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
              .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable())
              .authorizeHttpRequests(
                      req -> req.requestMatchers(permittedURL).permitAll()
                              .anyRequest().authenticated()
              )
              .sessionManagement(httpSecuritySessionManagementConfigurer ->
                      httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .logout(logout ->
                      logout.logoutUrl("/vortexcoreservice/api/v1/logout")
                              .addLogoutHandler(logoutHandler)
                              .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                      )
              .build();
    }
}
