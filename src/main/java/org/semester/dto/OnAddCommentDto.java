package org.semester.dto;


import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class OnAddCommentDto {
    private String text;
    private Date date;
}
