package org.semester.repository;

import org.semester.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContaining(String name, Pageable pageable);
    User findByEmail(String email);

    @Query(
            value = "SELECT users.* FROM users " +
                    "JOIN users_subscriptions ON users_subscriptions.subscribee_id = users.id " +
                    "WHERE users.id = :id",
            countQuery = "SELECT count(*) FROM users " +
                    "JOIN users_subscriptions ON users.id = users_subscriptions.subscribee_id " +
                    "WHERE users.id = :id",
            nativeQuery = true
    )
    List<User> findSubscribees(Long id, Pageable pageable);

    @Query(
            value = "SELECT users.* FROM users " +
                    "JOIN users_subscriptions ON users_subscriptions.subscription_id = users.id " +
                    "WHERE users.id = :id",
            countQuery = "SELECT count(*) FROM users " +
                    "JOIN users_subscriptions ON users.id = users_subscriptions.subscription_id " +
                    "WHERE users.id = :id",
            nativeQuery = true
    )
    List<User> findSubscribers(Long id, Pageable pageable);
}
