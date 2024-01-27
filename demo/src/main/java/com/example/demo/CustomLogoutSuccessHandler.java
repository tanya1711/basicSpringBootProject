package com.example.demo;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final TokenStore tokenStore;

    public CustomLogoutSuccessHandler(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenStore.removeAccessToken(accessToken); // Revoke access token
        }

        response.setStatus(HttpStatus.OK.value());
    }
}
