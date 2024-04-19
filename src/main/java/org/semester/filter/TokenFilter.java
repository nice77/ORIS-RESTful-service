package org.semester.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.semester.dto.ErrorDto;
import org.semester.util.StaticString;
import org.semester.util.TokenUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String email = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                email = tokenUtil.getEmail(jwt);
            } catch (SignatureException | MalformedJwtException  e) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, StaticString.BAD_TOKEN.getValue());
                return;
            } catch (ExpiredJwtException e) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, StaticString.EXPIRED_TOKEN.getValue());
                return;
            }
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority("USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int sc, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorDto error = new ErrorDto(message);
            response.setStatus(sc);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(error));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
