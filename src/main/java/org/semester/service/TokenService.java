package org.semester.service;

import org.semester.entity.Token;

public interface TokenService {
    Boolean checkIfTokenRevoked(Token token);
    Token addToken(Token token);
    Token getToken(String token);
    void updateToken(Token token);
}
