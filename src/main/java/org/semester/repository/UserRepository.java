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
                    "JOIN users_subscriptions ON users_subscriptions.subscriber_id = users.id " +
                    "WHERE users.id = :id",
            countQuery = "SELECT count(*) FROM users " +
                    "JOIN users_subscriptions ON users.id = users_subscriptions.subscriber_id " +
                    "WHERE users.id = :id",
            nativeQuery = true
    )
    List<User> findAuthors(Long id, Pageable pageable);

    @Query(
            value = "SELECT users.* FROM users " +
                    "JOIN users_subscriptions ON users_subscriptions.author_id = users.id " +
                    "WHERE users.id = :id",
            countQuery = "SELECT count(*) FROM users " +
                    "JOIN users_subscriptions ON users.id = users_subscriptions.author_id " +
                    "WHERE users.id = :id",
            nativeQuery = true
    )
    List<User> findSubscribers(Long id, Pageable pageable);


    @Query(
            nativeQuery = true,
            value = "select count(*) = 1 from users u " +
                    "join public.users_events_subscriptions us on u.id = us.user_id " +
                    "where us.event_id = :eventId and u.id = :userId"
    )
    Boolean amISubscribedToEvent(Long userId, Long eventId);
}
