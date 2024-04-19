package org.semester.entity;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import jakarta.persistence.*;
import org.springframework.boot.jackson.JsonComponent;

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
    private Long latitude;
    private Long longitude;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(mappedBy = "subscribedEvents")
    private List<User> subscribedUsers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<EventImage> eventImages;
}
