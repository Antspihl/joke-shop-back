package ee.veebiprojekt.veebiprojekttest.config;

import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.entity.UserRole;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private final Key jwtSecretKey = Keys.hmacShaKeyFor("Kui on meri hülgehall, ja sind ründamas suur hall".getBytes());

    private Optional<String> getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        Optional<String> jwt = getToken(request);
        if (jwt.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        Claims tokenBody = parseToken(jwt.get());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(buildAuthToken(tokenBody));

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(Claims claims) {
        String username = claims.get("username", String.class);
        log.debug("Building auth token for user " + username);
        return new UsernamePasswordAuthenticationToken(
                username,
                "",
                getAuth(username)
        );
    }

    private Set<SimpleGrantedAuthority> getAuth(String username) {
        Set<SimpleGrantedAuthority> auths = new HashSet<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.debug("User with username " + username + " not found.");
            return auths;
        }
        log.debug("User with username " + username + " found.");

        Long userId = user.getUserId();
        UserRole userRole = userRoleRepository.getUserRoleByUserId(userId);
        Long userRoleId = userRole.getRoleId();
        if (userRoleId == 1) {
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (userRoleId == 2) {
            auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return auths;
    }
}
