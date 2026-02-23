package com.sivvg.tradingservices.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

        // ✅ Mock response writer (VERY IMPORTANT)
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    // ✅ VALID TOKEN
    @Test
    public  void validToken_authenticatesUser() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.isTokenExpired("validtoken")).thenReturn(false);
        when(jwtUtil.validateToken("validtoken")).thenReturn(true);
        when(jwtUtil.extractUserId("validtoken")).thenReturn("user123");
        when(jwtUtil.extractRole("validtoken")).thenReturn("ROLE_USER");

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("user123");
        assertThat(auth.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_USER");

        verify(filterChain).doFilter(request, response);
    }

    // ❌ INVALID TOKEN (should NOT continue filter chain)
    @Test
    public void invalidToken_doesNotAuthenticate() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtUtil.isTokenExpired("invalidtoken")).thenReturn(false);
        when(jwtUtil.validateToken("invalidtoken")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        verify(filterChain, never()).doFilter(request, response);
    }

    // ❌ EXPIRED TOKEN
    @Test
    public void expiredToken_doesNotAuthenticate() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer expiredtoken");
        when(jwtUtil.isTokenExpired("expiredtoken")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        verify(filterChain, never()).doFilter(request, response);
    }

    // ✅ NO AUTH HEADER
    @Test
    public  void noAuthorizationHeader_doesNotAuthenticate() throws Exception {

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        verify(filterChain).doFilter(request, response);
    }

    // ✅ BLANK ROLE
    @Test
    public  void blankRole_doesNotAuthenticate() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.isTokenExpired("token")).thenReturn(false);
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.extractUserId("token")).thenReturn("user123");
        when(jwtUtil.extractRole("token")).thenReturn("   ");

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();

        verify(filterChain).doFilter(request, response);
    }
}
