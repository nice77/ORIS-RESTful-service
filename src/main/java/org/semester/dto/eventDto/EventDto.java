package org.semester.dto.eventDto;


import lombok.*;
import org.semester.dto.CommentDto;
import org.semester.entity.Comment;

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
    private Double latitude;
    private Double longitude;
    private Long authorId;
    private List<String> eventImages;
}
