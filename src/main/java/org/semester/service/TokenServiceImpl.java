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
    public Boolean checkIfTokenRevoked(Token token) {
        Token foundToken = tokenRepository.findByToken(token.getToken());
        return foundToken != null;
    }

    @Override
    public Token addToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void updateToken(Token token) {
//        tokenRepository.updateTokenById(token.getId(), token.getIsRevoked());
        tokenRepository.save(token);
    }

}
