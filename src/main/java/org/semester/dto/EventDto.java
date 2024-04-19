package org.semester.dto;


import lombok.*;
import org.semester.entity.EventImage;
import org.semester.entity.User;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventDto {
    private Long id;
    private Date date;
    private String name;
    private String description;
    private Long latitude;
    private Long longitude;
    private Long authorId;
    private List<String> eventImages;
}
