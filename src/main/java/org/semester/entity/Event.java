package org.semester.entity;


import lombok.*;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "events")
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Date date;
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(mappedBy = "subscribedEvents")
    private List<User> subscribedUsers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<EventImage> eventImages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<Comment> commentList;
}
