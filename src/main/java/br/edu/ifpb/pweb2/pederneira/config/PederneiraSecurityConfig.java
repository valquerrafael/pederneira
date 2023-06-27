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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

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
                .loginPage("/layouts/login")
                .defaultSuccessUrl("/home", true)
                .permitAll())
            .logout(logout -> logout.logoutUrl("/auth/logout"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDetails user = User.withUsername("valquer")
                    .password(encoder.encode("pp1"))
                    .roles("ADMIN")
                    .build();
        UserDetails user1 = User.withUsername("pedro")
                    .password(encoder.encode("pp1"))
                    .roles("STUDENT")
                    .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        // users.createUser(user);
        // users.createUser(user1);
        return users;
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

