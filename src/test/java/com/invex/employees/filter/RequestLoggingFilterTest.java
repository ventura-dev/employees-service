package com.invex.employees.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;

import static org.mockito.Mockito.*;

/**
 * Unit tests for RequestLoggingFilter.
 */
@ExtendWith(MockitoExtension.class)
class RequestLoggingFilterTest {

    private RequestLoggingFilter requestLoggingFilter;
    private FilterChain filterChain;
    private ServletResponse servletResponse;

    @BeforeEach
    void setUp() {
        requestLoggingFilter = new RequestLoggingFilter();
        filterChain = mock(FilterChain.class);
        servletResponse = mock(ServletResponse.class);
    }

    @Test
    void shouldContinueFilterChainWithRegularHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/employees");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("User-Agent", "PostmanRuntime");

        requestLoggingFilter.doFilter(request, servletResponse, filterChain);

        verify(filterChain, times(1)).doFilter(request, servletResponse);
    }

    @Test
    void shouldContinueFilterChainWithSensitiveHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/employees");
        request.addHeader("Authorization", "Bearer my-secret-token");
        request.addHeader("Cookie", "JSESSIONID=abc123");
        request.addHeader("Set-Cookie", "session=value");

        requestLoggingFilter.doFilter(request, servletResponse, filterChain);

        verify(filterChain, times(1)).doFilter(request, servletResponse);
    }

    @Test
    void shouldContinueFilterChainWhenNoHeadersArePresent() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("DELETE");
        request.setRequestURI("/employees/1");

        requestLoggingFilter.doFilter(request, servletResponse, filterChain);

        verify(filterChain, times(1)).doFilter(request, servletResponse);
    }
}