package myproject.SummerSpringBootProject.config;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.exception.FilterChainExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsFilter corsFilter;
    private final AuthenticationProvider authenticationProvider;

    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/posts/**").authenticated()
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .requestMatchers("/api/v1/hiuser").authenticated()
                        .requestMatchers("/api/v1/hiadmin").hasRole("ADMIN")
                        .requestMatchers("/api/v1/quiz/all").permitAll() //viena is endpointu visi gali pasiekti
                        .requestMatchers("/api/v1/quiz/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);

        return http.build();
    }

}
