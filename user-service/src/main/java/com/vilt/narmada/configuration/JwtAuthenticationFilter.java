package com.vilt.narmada.configuration;
import java.io.IOException;

import com.vilt.narmada.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vilt.narmada.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;   
    private CustomUserDetailsService userService;
    
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userService) {
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
               
        String token = jwtUtils.extractToken(request);
          if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //Without this step, every request would be treated as unauthenticated.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);//call the next filter in sequence
    }
}