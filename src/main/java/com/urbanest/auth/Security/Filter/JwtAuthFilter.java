package com.urbanest.auth.Security.Filter;

import com.urbanest.auth.Entities.User;
import com.urbanest.auth.Repository.UserRepository;
import com.urbanest.auth.Utility.JwtUtill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());

        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String bearerToken = token.split("Bearer ")[1];
        String username = JwtUtill.getUserName(bearerToken);
        Long tokenVersion = JwtUtill.getTokenVersion(bearerToken);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> user = userRepo.findByUsername(username);
            if(user.isPresent() && !JwtUtill.isTokenExpired(bearerToken) && tokenVersion.equals(user.get().getTokenVersion())){
                UsernamePasswordAuthenticationToken contextToken = new UsernamePasswordAuthenticationToken(user.get(),null,user.get().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(contextToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
