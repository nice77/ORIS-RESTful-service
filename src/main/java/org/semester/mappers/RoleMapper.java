package org.semester.mappers;

import org.semester.dto.RoleDto;
import org.semester.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto getRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .role(role.getRole())
                .build();
    }

    public Role getRoleEntity(RoleDto roleDto) {
        return Role.builder()
                .id(roleDto.getId())
                .role(roleDto.getRole())
                .build();
    }
}
