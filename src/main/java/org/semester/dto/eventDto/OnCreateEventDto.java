package org.semester.dto.eventDto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OnCreateEventDto {
    private Date date;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
}
