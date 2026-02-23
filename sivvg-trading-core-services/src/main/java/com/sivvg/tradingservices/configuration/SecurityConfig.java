package com.sivvg.tradingservices.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sivvg.tradingservices.util.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth

						// 🔓 PUBLIC APIs
						.requestMatchers("/api/v1/auth/login", "/api/v1/auth/send-otp", "/api/v1/auth/resend-otp",
								"/api/v1/auth/verify-otp", "/api/v1/auth/user-register", "/api/v1/auth/forgot/send-otp",
								"/api/v1/auth/forgot/verify-otp", "/api/v1/auth/forgot/reset-password")
						.permitAll()

						// 🔐 SUPER ADMIN
						.requestMatchers("/api/v1/admin/create-admin").hasAuthority("ROLE_SUPER_ADMIN")

						// 🔐 ADMIN
						.requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN")

						// 🔐 HOME
						.requestMatchers("/api/v1/home").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 PROFILE
						.requestMatchers("/api/v1/profile/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// ✅ FIXED: EXCEL UPLOAD (ISSUE-1)
						.requestMatchers("/api/v1/excel/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN")

						// 🔐 RECORDS
						.requestMatchers("/api/v1/records/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 CONTACT
						.requestMatchers("/api/v1/contact").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 TIPS
						.requestMatchers("/api/v1/tips/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 PAST PERFORMANCE
						.requestMatchers("/api/v1/pastperformance/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 SECTORS
						.requestMatchers("/api/v1/sectors/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 HOLIDAYS
						.requestMatchers("/api/v1/holidays/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 NOTIFICATIONS
						.requestMatchers("/api/v1/notifications/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 SUMMARY DOWNLOAD
						.requestMatchers("/api/v1/summary/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

						// 🔐 DEVICE TOKEN
						.requestMatchers("/api/v1/device-token/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN")

						// 🔐 ANY OTHER
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("""
				    ROLE_SUPER_ADMIN > ROLE_ADMIN
				    ROLE_ADMIN > ROLE_USER
				""");
		return hierarchy;
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
