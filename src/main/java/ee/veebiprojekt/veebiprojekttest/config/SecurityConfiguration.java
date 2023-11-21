package ee.veebiprojekt.veebiprojekttest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/carts/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers("/api/cart_items/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers("/api/ratings/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers("/api/jokes/add").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers("/api/jokes/get/**").hasAnyAuthority(USER, ADMIN)
                        .requestMatchers("/api/jokes/all").hasAuthority(ADMIN)
                        .requestMatchers("/api/jokes/buy").hasAnyAuthority(USER, ADMIN)
                        .requestMatchers("/api/jokes/").authenticated()
                        .requestMatchers("/api/jokes/setups").permitAll()
                        .requestMatchers("/api/jokes/top3").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
