package org.semester.dto.userDto;


import lombok.*;
import org.semester.dto.RoleDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullUserDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private String city;
    private String userImage;
    private RoleDto role;
    private Boolean isBanned;
}
