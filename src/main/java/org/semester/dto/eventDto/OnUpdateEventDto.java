package org.semester.dto.eventDto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OnUpdateEventDto {
    private Long id;
    private Date date;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
}
