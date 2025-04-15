package com.playfooty.backend_api.security;

import com.playfooty.backendCore.exception.UnauthorizedException;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.backend_api.service.UserDetailsService;
import com.playfooty.userManagement.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final HandlerExceptionResolver handlerExceptionResolver;
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTRequestFilter.class);

    private static List<String> skipFilterUrls = Arrays.asList("/auth/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info(request.getRequestURI());

        // Skip filter for specific paths
        if (skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Process JWT validation
        try {
            String tokenHeader = request.getHeader("Authorization");

            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                throw new ServletException("Invalid or no authorization token");
            }

            String token = tokenHeader.substring(7);  // Get the token part after "Bearer "
            UUID userId = UUID.fromString(jwtService.getClaim(token, "id").asString());
            UserDetails user = userDetailsService.findById(userId)
                    .orElseThrow(UnauthorizedException::new);

            // Set authentication in the context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);  // Proceed with the filter chain

        } catch (Exception ex) {
            // Let the exception propagate to the global exception handler
            handlerExceptionResolver.resolveException(request, response, null, new UnauthorizedException(ex.getMessage()));
        }
    }
}
