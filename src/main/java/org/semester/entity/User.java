package org.semester.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String city;
    private Integer age;
    @Column(name = "hashed_password")
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private List<Event> eventList;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "users_events_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"})
    )
    private List<Event> subscribedEvents;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "users_subscriptions",
            joinColumns = @JoinColumn(name = "subscribee_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_id")
    )
    private List<User> subscribers;

    private String userImage;
}
