package com.example.springsecuritydemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.springsecuritydemo.security.UserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("somethingverysecured")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/exit", HttpMethod.GET.name()))
                .logoutUrl("/exit")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .logoutSuccessUrl("/login");
    }

    /**
     * it's how retrieve USER from DB
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails ivanUser = User.builder()
                .username("ivan")
                .password("password5834")
                .authorities(ADMIN.getGrantedAuthorities())
                .passwordEncoder(passwordEncoder::encode)
                .build();
        UserDetails tomUser = User.builder()
                .username("tom")
                .password("password1")
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .passwordEncoder(passwordEncoder::encode)
                .build();
        UserDetails annaStudent = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(ivanUser, tomUser, annaStudent);
    }
}
