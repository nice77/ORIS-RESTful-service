package org.semester.service;

import org.semester.dto.*;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.userDto.FullUserDto;
import org.semester.dto.userDto.RegisterUserDto;
import org.semester.dto.userDto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, String> addUser(RegisterUserDto registerUserDto);
    List<UserDto> getUsers(Integer page);
    List<UserDto> findByNameContaining(String name, Integer page);
    UserDto findByEmail(String email);
    UserDto findById(Long id);
    List<UserDto> getSusbcribers(Long id, Integer page);
    List<UserDto> getSubscribees(Long id, Integer page);
    Boolean subscribe(Long idAuthor, Long idSubscriber);
    Boolean updateUser(UserDto userDto);
    void deleteUser(Long id);
    byte[] getProfileImage(Long id);
    byte[] getProfileImageByFileName(String fileName);
    Boolean addProfileImage(MultipartFile file, String userEmail);
    Boolean deleteProfileImage(String userEmail);
    Boolean subscribeToEvent(String email, Long eventId);
    Boolean unsubscribeFromEvent(String email, Long eventId);
    List<EventDto> getSubscribedEvents(Long id, Integer page);
    List<EventDto> getCreatedEvents(Long id, Integer page);
    String getPassword(String email);
    RoleDto getRole(String email);
    Boolean changeIsBanned(Long id);
    FullUserDto getFullUserByEmail(String email);
}
