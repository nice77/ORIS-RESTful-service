package org.semester.dto;

import lombok.*;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.userDto.UserDto;

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
    private UserDto userDto;
}
