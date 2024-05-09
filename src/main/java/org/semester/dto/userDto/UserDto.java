package org.semester.dto.userDto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private String city;
    private String userImage;
    private Integer subscribersCount;
    private Integer authorsCount;
}
