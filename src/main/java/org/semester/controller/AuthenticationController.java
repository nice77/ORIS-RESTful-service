package org.semester.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.semester.dto.RoleDto;
import org.semester.dto.authServiceDto.AuthorizeRequest;
import org.semester.dto.UserDto;
import org.semester.dto.authServiceDto.RefreshRequest;
import org.semester.entity.Token;
import org.semester.dto.ErrorDto;
import org.semester.service.TokenService;
import org.semester.service.UserService;
import org.semester.util.StaticString;
import org.semester.util.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private TokenUtil tokenUtil;
    private UserService userService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    @PostMapping(
            value = "/authorization",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> authenticate(@RequestBody AuthorizeRequest request) {
        UserDto userDto = userService.findByEmail(request.getEmail());
        if (userDto == null) {
            ErrorDto error = new ErrorDto(StaticString.WRONG_CREDENTIALS.getValue());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        boolean isPasswordCorrect = passwordEncoder.matches(request.getPassword(), userService.getPassword(userDto.getEmail()));
        if (!isPasswordCorrect) {
            ErrorDto error = new ErrorDto(StaticString.WRONG_CREDENTIALS.getValue());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        String email = request.getEmail();
        String role = userDto.getRole().getRole();
        Map<String, String> tokens = tokenUtil.generatePair(email, role);
        tokenService.addToken(Token.builder()
                .token(tokens.get("refresh"))
                .isRevoked(false)
                .build());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping(
            value = "/refresh_token",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> refresh_tokens(@RequestBody RefreshRequest request) {
        Token found =
                tokenService.getToken(
                        request.getRefresh()
                );
        if (found.getIsRevoked()) {
            ErrorDto error = new ErrorDto(StaticString.USED_TOKEN.getValue());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        String email;
        String role;
        String jwt = request.getRefresh();
        try {
            email = tokenUtil.getEmail(jwt);
            role = tokenUtil.getRole(jwt);
        } catch (ExpiredJwtException e) {
            ErrorDto error = new ErrorDto(StaticString.EXPIRED_TOKEN.getValue());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        } catch (SignatureException | MalformedJwtException e) {
            ErrorDto error = new ErrorDto(StaticString.BAD_TOKEN.getValue());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        found.setIsRevoked(true);
        tokenService.updateToken(found);
        Map<String, String> tokens = tokenUtil.generatePair(email, role);
        tokenService.addToken(Token.builder()
                        .token(tokens.get("refresh"))
                        .isRevoked(false)
                .build());
        return ResponseEntity.ok(tokens);
    }
}
