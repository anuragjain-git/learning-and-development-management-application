package com.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	private UserDetailsService userService;
	private PasswordEncoder passwordEncoder;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint entryPoint,
			UserDetailsService userService,PasswordEncoder passwordEncoder) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtAuthenticationEntryPoint = entryPoint;
		this.userService = userService;
		this.passwordEncoder=passwordEncoder;
	}
		
	@Autowired
	protected void configure(AuthenticationManagerBuilder authority) throws Exception {
		authority.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		 
		    AuthenticationManagerBuilder authManagerBuilder =
		            http.getSharedObject(AuthenticationManagerBuilder.class);
		    
		    authManagerBuilder.userDetailsService(userService)
		                      .passwordEncoder(passwordEncoder);
		    
		    return authManagerBuilder.build();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/auth/register", "/auth/login", "/api/gift/welcome").permitAll()
								.requestMatchers(HttpMethod.POST, "/api/gift").hasRole("ADMIN")
								.requestMatchers(HttpMethod.PUT, "/api/gift").hasRole("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/api/gift/*").hasRole("ADMIN")
								.requestMatchers(HttpMethod.GET, "/api/gift", "/api/gift/*")
								.hasAnyRole("ADMIN", "USER").anyRequest().authenticated())
				.httpBasic(h -> {
				}).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
