package org.semester.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.semester.dto.userDto.FullUserDto;
import org.semester.dto.authServiceDto.AuthorizeRequest;
import org.semester.dto.authServiceDto.RefreshRequest;
import org.semester.entity.Token;
import org.semester.exceptionHandler.exception.BadTokenException;
import org.semester.exceptionHandler.exception.WrongCredentialsException;
import org.semester.service.TokenService;
import org.semester.service.UserService;
import org.semester.util.StaticString;
import org.semester.util.TokenUtil;
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
    public ResponseEntity<Object> authenticate(@RequestBody AuthorizeRequest request) throws WrongCredentialsException {
        FullUserDto fullUserDto = userService.getFullUserByEmail(request.getEmail());
        if (fullUserDto == null) {
            throw new WrongCredentialsException(StaticString.WRONG_CREDENTIALS.getValue());
        }
        boolean isPasswordCorrect = passwordEncoder.matches(request.getPassword(), userService.getPassword(fullUserDto.getEmail()));
        if (!isPasswordCorrect) {
            throw new WrongCredentialsException(StaticString.WRONG_CREDENTIALS.getValue());
        }
        if (fullUserDto.getIsBanned()) {
            throw new WrongCredentialsException(StaticString.BANNED.getValue());
        }
        String email = request.getEmail();
        String role = fullUserDto.getRole().getRole();
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
    public ResponseEntity<Object> refresh_tokens(@RequestBody RefreshRequest request) throws BadTokenException {
        Token found =
                tokenService.getToken(
                        request.getRefresh()
                );
        if (found.getIsRevoked()) {
            throw new BadTokenException(StaticString.USED_TOKEN.getValue());
        }
        String email;
        String role;
        String jwt = request.getRefresh();
        try {
            email = tokenUtil.getEmail(jwt);
            role = tokenUtil.getRole(jwt);
        } catch (ExpiredJwtException e) {
            throw new BadTokenException(StaticString.EXPIRED_TOKEN.getValue());
        } catch (SignatureException | MalformedJwtException e) {
            throw new BadTokenException(StaticString.BAD_TOKEN.getValue());
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
