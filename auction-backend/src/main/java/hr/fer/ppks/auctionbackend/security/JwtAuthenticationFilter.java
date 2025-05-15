package hr.fer.ppks.auctionbackend.security;

import hr.fer.ppks.auctionbackend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Add this line to debug CORS preflight requests
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // Debug information
        System.out.println("==== REQUEST START ====");
        System.out.println("Path: " + request.getMethod() + " " + request.getRequestURI());

        // Enumerate all headers to find any issues
        System.out.println("=== ALL REQUEST HEADERS ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + ": " + request.getHeader(headerName));
            }
        }

        System.out.println("Auth header: " + (authHeader != null ? (authHeader.length() > 15 ?
                authHeader.substring(0, 15) + "..." : authHeader) : "null"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid Authorization header found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            System.out.println("JWT token found, attempting to validate");
            final String username = jwtTokenUtil.extractUsername(jwt);
            System.out.println("Extracted username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication successful for user: " + username);
                } else {
                    System.out.println("Token validation failed for user: " + username);
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing JWT token: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("==== REQUEST END ====");
        filterChain.doFilter(request, response);
    }
}