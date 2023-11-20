package ee.veebiprojekt.veebiprojekttest.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

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
        return new UsernamePasswordAuthenticationToken(
                claims.get("username"),
                "",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


}
