package org.semester.dto;


import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class CommentDto {
    private Long id;
    private String text;
    private Date date;
    private Long eventId;
    private Long userId;
}
