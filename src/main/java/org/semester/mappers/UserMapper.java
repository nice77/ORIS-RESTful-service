package org.semester.mappers;

import org.semester.dto.UserDto;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto getUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .city(user.getCity())
                .email(user.getEmail())
                .age(user.getAge())
                .userImage(user.getUserImage())
                .build();
    }

    public User getUserEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .city(userDto.getCity())
                .email(userDto.getEmail())
                .age(userDto.getAge())
                .userImage(userDto.getUserImage())
                .build();
    }
}
