package org.semester.mappers;

import lombok.AllArgsConstructor;
import org.semester.dto.FullUserDto;
import org.semester.dto.UserDto;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private RoleMapper roleMapper;

    public FullUserDto getFullUserDto(User user) {
        return FullUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .city(user.getCity())
                .email(user.getEmail())
                .age(user.getAge())
                .userImage("users/user-image/" + user.getUserImage())
                .role(roleMapper.getRoleDto(user.getRole()))
                .isBanned(user.getIsBanned())
                .build();
    }

    public UserDto getUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .city(user.getCity())
                .email(user.getEmail())
                .age(user.getAge())
                .userImage("users/user-image/" + user.getUserImage())
                .build();
    }

    public User getUserEntity(FullUserDto fullUserDto) {
        return User.builder()
                .id(fullUserDto.getId())
                .name(fullUserDto.getName())
                .city(fullUserDto.getCity())
                .email(fullUserDto.getEmail())
                .age(fullUserDto.getAge())
                .userImage(fullUserDto.getUserImage())
                .role(roleMapper.getRoleEntity(fullUserDto.getRole()))
                .isBanned(fullUserDto.getIsBanned())
                .build();
    }
}
