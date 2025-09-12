package tvz.jwafp.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tvz.jwafp.api.security.JwtAuthenticationFilter;

import static tvz.jwafp.api.config.Urls.*;
import static tvz.jwafp.api.config.Urls.Headers.X_API_KEY;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, URL_AUTH + URL_WILDCARD).permitAll()
//                        .requestMatchers(HttpMethod.POST, URL_PROFESSOR, URL_STUDENT, URL_USER).permitAll()
                        .requestMatchers(HttpMethod.GET, URL_USER + USERNAME_FILTER + URL_WILDCARD).permitAll()
                        .requestMatchers(HttpMethod.GET, URL_CRON + URL_WILDCARD, URL_AUTH + URL_CRON)
                                .access((authentication, context) -> {
                                    String apiKey = context.getRequest().getHeader(X_API_KEY);
                                    return new AuthorizationDecision(
                                            "supersecretapikeythatihaveconcoctedformycronscheduler".equals(apiKey));
                                })
                        .requestMatchers(HttpMethod.GET, URL_REPORT + URL_WILDCARD).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}