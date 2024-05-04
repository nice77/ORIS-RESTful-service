package org.semester.service;

import org.semester.dto.CommentDto;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.eventDto.OnCreateEventDto;
import org.semester.dto.userDto.UserDto;
import org.semester.entity.Event;
import org.semester.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    EventDto addEvent(OnCreateEventDto onCreateEventDto, String email);
    List<EventDto> getEvents(Integer page);
    List<EventDto> findByNameContaining(String name, Integer page);
    EventDto findById(Long id);
    List<UserDto> getSubscribers(Long id, Integer page);
    Boolean deleteEvent(Long id);
    Boolean addImage(Long id, MultipartFile file, String email);
    Boolean deleteImage(Long id);
    byte[] getImage(Long id, Integer number);
    byte[] getImage(String name);
    List<CommentDto> getComments(Long id, Integer page);
    Boolean addComment(CommentDto commentDto, Long eventId, String email);
}
