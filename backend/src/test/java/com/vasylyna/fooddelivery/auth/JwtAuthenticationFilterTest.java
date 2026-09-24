package com.vasylyna.fooddelivery.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vasylyna.fooddelivery.common.ApiErrorResponseWriter;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtAuthenticationFilterTest {
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void invalidBearerTokenReturnsSafeJson401AndStopsTheChain() throws Exception {
        JwtService jwt = mock(JwtService.class);
        when(jwt.subject("bad-token")).thenThrow(new MalformedJwtException("token internals"));
        FilterChain chain = mock(FilterChain.class);
        var response = new MockHttpServletResponse();
        var filter = new JwtAuthenticationFilter(jwt, mock(UserDetailsService.class), errorWriter());

        filter.doFilterInternal(request("Bearer bad-token"), response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).startsWith("application/json");
        assertThat(response.getContentAsString()).contains("\"error\":\"Invalid or expired token\"")
                .doesNotContain("token internals");
        verifyNoInteractions(chain);
    }

    @Test
    void missingBearerTokenContinuesAsAnAnonymousRequest() throws Exception {
        FilterChain chain = mock(FilterChain.class);
        var response = new MockHttpServletResponse();
        var filter = new JwtAuthenticationFilter(mock(JwtService.class), mock(UserDetailsService.class), errorWriter());

        filter.doFilterInternal(request(null), response, chain);

        verify(chain).doFilter(any(), any());
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void unexpectedUserLookupFailuresReturnASafeServerError() throws Exception {
        JwtService jwt = mock(JwtService.class);
        when(jwt.subject("valid-token")).thenReturn("person@example.com");
        UserDetailsService users = mock(UserDetailsService.class);
        when(users.loadUserByUsername("person@example.com"))
                .thenThrow(new IllegalStateException("database unavailable"));
        FilterChain chain = mock(FilterChain.class);
        var response = new MockHttpServletResponse();
        var filter = new JwtAuthenticationFilter(jwt, users, errorWriter());

        filter.doFilterInternal(request("Bearer valid-token"), response, chain);

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getContentAsString()).contains("\"error\":\"An unexpected error occurred\"")
                .doesNotContain("database unavailable");
        verifyNoInteractions(chain);
    }

    private MockHttpServletRequest request(String authorization) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (authorization != null) {
            request.addHeader("Authorization", authorization);
        }
        return request;
    }

    private ApiErrorResponseWriter errorWriter() {
        return new ApiErrorResponseWriter(new ObjectMapper().findAndRegisterModules());
    }
}