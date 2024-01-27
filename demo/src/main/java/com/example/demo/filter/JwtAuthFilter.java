package com.example.demo.filter;

import com.example.demo.service.JwtService;
import com.example.demo.service.LoginUserService;
import com.example.demo.service.ServiceClass;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    private LoginUserService loginUserService;

    public String username;

    @Autowired
    private ServiceClass serviceClass;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("called");
        String authHeader = request.getHeader("Authorization");
        String token;
        if(authHeader!=null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            System.out.println(jwtService.extractExpirationData(token));

        }


        filterChain.doFilter(request,response);
    }

    public String giveExtractedUsername(){
        return username;
    }
}
