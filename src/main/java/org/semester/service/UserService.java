package org.semester.service;

import org.semester.dto.EventDto;
import org.semester.dto.RoleDto;
import org.semester.dto.UserDto;
import org.semester.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User addUser(User user);
    List<UserDto> getUsers(Integer page);
    List<UserDto> findByNameContaining(String name, Integer page);
    UserDto findByEmail(String email);
    UserDto findById(Long id);
    List<UserDto> getSusbcribers(Long id, Integer page);
    List<UserDto> getSubscribees(Long id, Integer page);
    void updateUser(User user);
    void deleteUser(Long id);
    byte[] getProfileImage(Long id);
    Boolean addProfileImage(MultipartFile file, String userEmail);
    Boolean deleteProfileImage(String userEmail);
    Boolean subscribeToEvent(String email, Long eventId);
    Boolean unsubscribeFromEvent(String email, Long eventId);
    List<EventDto> getSubscribedEvents(Long id);
    String getPassword(String email);
    RoleDto getRole(String email);
    Boolean changeIsBanned(Long id);
}
