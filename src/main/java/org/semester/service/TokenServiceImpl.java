package org.semester.service;

import lombok.AllArgsConstructor;
import org.semester.entity.Token;
import org.semester.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static org.semester.util.StaticString.TOKEN_NOT_FOUND;


@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    @Override
    public Boolean checkIfTokenRevoked(Long tokenId) {
        Token foundToken = tokenRepository.findById(tokenId).orElseThrow(() -> new NoSuchElementException(TOKEN_NOT_FOUND.getValue()));
        return foundToken.getIsRevoked();
    }

    @Override
    public Token addToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token getToken(String token) {
        Token tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity == null) {
            throw new NoSuchElementException(TOKEN_NOT_FOUND.getValue());
        }
        return tokenEntity;
    }

    @Override
    public void updateToken(Token token) {
        tokenRepository.save(token);
    }

}
