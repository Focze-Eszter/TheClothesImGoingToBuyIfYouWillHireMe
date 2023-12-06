package com.EszterFocze.TCIGTB.site.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //spring configuration class
@EnableWebSecurity //for security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    //The @Bean annotation tells Spring to create an object of a given class, and manage that object in the application context. It is similar to @Service, @Controller
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll(); //allow public access to the admin app without authentication, override the default
    }
}
