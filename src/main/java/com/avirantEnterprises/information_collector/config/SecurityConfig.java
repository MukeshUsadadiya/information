package com.avirantEnterprises.information_collector.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig   {
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .authorizeRequests(auth -> auth
                      .requestMatchers("/login/admin",  "/register/**", "/login/**", "/verify", "/oauth2/**","/static/**", "/css/**").permitAll()  // Add Google OAuth2 authorization URL
                      .requestMatchers("/admin/**","/tasks","/editTask/**", "/updateTask/**", "/deleteTask/**").hasRole("ADMIN")
                      .anyRequest().authenticated()
              )

              .formLogin(login -> login
                      .loginPage("/login/admin")
                      .loginProcessingUrl("/login/admin")
                      .defaultSuccessUrl("/login/adminDashboard",true)
                      .permitAll()
              )
              .oauth2Login(oauth2 -> oauth2
                      .loginPage("/login")  // OAuth2 login page
                      .defaultSuccessUrl("/dashboard", true)  // Redirect to dashboard after successful login
              )
              .logout(logout -> logout
                      .logoutUrl("/logout")
                      .logoutSuccessUrl("/login?logout")
              );

      return http.build();
   }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        // Define the static admin user (you can replace this with database authentication)
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")  // Default admin username
                        .password(passwordEncoder.encode("admin123"))  // Password (encoded)
                        .roles("ADMIN")  // Role assigned
                        .build()
        );
    }

    @Bean
   public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
      return http.getSharedObject(AuthenticationManagerBuilder.class).build();
   }


}
