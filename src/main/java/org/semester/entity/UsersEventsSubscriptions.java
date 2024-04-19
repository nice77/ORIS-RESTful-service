package org.semester.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users_events_subscriptions")
public class UsersEventsSubscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_events_subscriptions",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "event_id")
//    )
//    private List<User> user;
//
//    @OneToOne
//    @JoinColumn(name = "event_id")
//    private Event event;
}
