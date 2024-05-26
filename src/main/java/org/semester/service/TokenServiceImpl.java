package org.semester.service;

import lombok.AllArgsConstructor;
import org.semester.entity.Token;
import org.semester.repository.TokenRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    @Override
    public Boolean checkIfTokenRevoked(Long tokenId) {
        Token foundToken = tokenRepository.findById(tokenId).orElseThrow();
        return foundToken.getIsRevoked();
    }

    @Override
    public Token addToken(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void updateToken(Token token) {
        tokenRepository.save(token);
    }

}
