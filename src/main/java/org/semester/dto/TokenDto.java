package org.semester.dto;

import lombok.*;
import org.semester.entity.User;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TokenDto {
    private Long id;
    private UUID token;
    private User user;
}
