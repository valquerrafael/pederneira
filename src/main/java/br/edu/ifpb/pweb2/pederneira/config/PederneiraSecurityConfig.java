package br.edu.ifpb.pweb2.pederneira.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class PederneiraSecurityConfig {
    @Autowired
    DataSource dataSource;

    protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests()
            .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin(form -> form
                .loginPage("/auth")
                .defaultSuccessUrl("/home", true)
                .permitAll())
            .logout(logout -> logout.logoutUrl("/auth/logout"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return User.builder()
                    .username("pederneira")
                    .password(passwordEncoder().encode("flintstone"))
                    .roles("ADMIN")
                    .build();
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }
}

