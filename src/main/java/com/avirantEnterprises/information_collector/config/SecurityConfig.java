    package com.avirantEnterprises.information_collector.config;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.provisioning.JdbcUserDetailsManager;
    import org.springframework.security.provisioning.UserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

    import javax.sql.DataSource;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/register/**", "/login/**", "/oauth2/**", "/static/**", "/css/**").permitAll()
                            .requestMatchers("/tasks").hasRole("ADMIN")
                            .requestMatchers("/admin/**","/login/adminDashboard").hasRole("ADMIN")
                            .requestMatchers("/login/userDashboard").hasRole("USER")
                            .anyRequest().authenticated()
                    )
                    .formLogin(login -> login
                            .loginPage("/login/admin")
                            .loginProcessingUrl("/login/admin")
                            .defaultSuccessUrl("/login/adminDashboard", true)
                            .permitAll()
                    )
                    .formLogin(login -> login
                            .loginPage("/login/user")
                            .loginProcessingUrl("/login/user")
                            .defaultSuccessUrl("/login/userDashboard", true)
                            .permitAll()
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .loginPage("/login")
                            .defaultSuccessUrl("/dashboard", true)
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .invalidateHttpSession(true)                  // Invalidate the session
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/login")
                    )
                    .csrf(csrf -> csrf.disable())
                            .sessionManagement(session -> session
                                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

            return http.build();
        }

        /*@Bean
        public UserDetailsService inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
            return new InMemoryUserDetailsManager(
                    org.springframework.security.core.userdetails.User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .roles("ADMIN")
                            .build()
            );
        }*/

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }
