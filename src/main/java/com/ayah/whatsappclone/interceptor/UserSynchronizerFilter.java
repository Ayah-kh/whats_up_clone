package com.ayah.whatsappclone.interceptor;

import com.ayah.whatsappclone.user.UserSynchronizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// TODO: 09/03/2026  AuthenticationSuccessEvent, make the user sync more efficient
/*
If you want, I can also show you a much cleaner architecture used in production
 with Keycloak where user synchronization happens automatically during JWT conversion,
  eliminating this filter entirely. It’s significantly better design.
 */
@Component
@RequiredArgsConstructor
public class UserSynchronizerFilter extends OncePerRequestFilter {

    private final UserSynchronizer userSynchronizer;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof AbstractAuthenticationToken) {
            JwtAuthenticationToken token = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());

            userSynchronizer.synchronizeWithIdp(token.getToken());
        }
        filterChain.doFilter(request, response);
    }
}
