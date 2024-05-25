package org.semester.service;

import org.semester.entity.Token;

public interface TokenService {
    Boolean checkIfTokenRevoked(Long id);
    Token addToken(Token token);
    Token getToken(String token);
    void updateToken(Token token);
}
