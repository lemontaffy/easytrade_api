package com.cake.easytrade.config.core;

import com.cake.easytrade.service.CustomUserDetailsService;
import com.cake.easytrade.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Extract token after "Bearer "

            try {
                // Validate token and extract claims
                Claims claims = JwtUtil.validateAccessToken(token);
                Integer userIdClaim = claims.get("userId", Integer.class);
                String role = claims.get("role", String.class);

                if (userIdClaim != null && role != null) {
                    Long userId = userIdClaim.longValue();

                    // Load user details using userId
                    UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                    // Create granted authorities
                    List<GrantedAuthority> authorities =
                            Collections.singletonList(new SimpleGrantedAuthority(role));

                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication set in SecurityContext: {} " + SecurityContextHolder.getContext().getAuthentication());

                }
            } catch (ExpiredJwtException e) {
                logger.error("JWT expired: {} " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token expired");
                return;
            } catch (NumberFormatException e) {
                logger.error("Invalid userId in JWT token: {} " +  e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format in token");
                return;
            } catch (Exception e) {
                logger.error("JWT validation failed: {} " +  e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}


