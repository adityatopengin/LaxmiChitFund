package com.laxmichitfund.config;

import com.laxmichitfund.entity.User;
import com.laxmichitfund.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabling CSRF and Frame Options strictly so the H2 memory database console works for testing
            .csrf(csrf -> csrf.disable()) 
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) 
            
            // Define which pages require login
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/register", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            
            // Wire up the custom HTML login page we built earlier
            .formLogin(form -> form
                .loginPage("/") // The root URL serves the index.html login page
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/?error=true")
                .permitAll()
            )
            
            // Handle logouts safely
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout=true")
                .permitAll()
            );

        return http.build();
    }

    // This method teaches Spring how to fetch users from your custom database table
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                // {noop} tells Spring we aren't encrypting passwords yet (fine for a dummy game)
                .password("{noop}" + user.getPassword()) 
                .roles("USER")
                .build();
        };
    }
}

