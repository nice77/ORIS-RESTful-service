package org.semester.dto;


import lombok.*;

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
