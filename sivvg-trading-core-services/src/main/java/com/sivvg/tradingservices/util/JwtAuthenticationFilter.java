package com.sivvg.tradingservices.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		// NO TOKEN → CONTINUE
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);

		// INVALID / EXPIRED TOKEN
		if (!jwtUtil.validateToken(token)) {

			log.warn("Invalid or expired JWT token | path={}", request.getRequestURI());

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"status\":401,\"message\":\"Invalid or expired JWT token.\"}");
			return;
		}

		// AUTHENTICATION SETUP
		if (SecurityContextHolder.getContext().getAuthentication() == null) {

			String userId = jwtUtil.extractUserId(token);
			String role = jwtUtil.extractRole(token);

			if (role != null && !role.isBlank()) {

				String finalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

				List<SimpleGrantedAuthority> authorities = Collections
						.singletonList(new SimpleGrantedAuthority(finalRole));

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
						null, authorities);

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				log.info("JWT authentication successful | userId={} | role={}", userId, finalRole);
			}
		}

		filterChain.doFilter(request, response);
	}
}
