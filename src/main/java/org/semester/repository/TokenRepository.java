package org.semester.repository;

import jakarta.transaction.Transactional;
import org.semester.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
