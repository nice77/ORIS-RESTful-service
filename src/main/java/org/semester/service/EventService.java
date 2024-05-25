package org.semester.service;

import org.semester.dto.CommentDto;
import org.semester.dto.OnAddCommentDto;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.eventDto.OnCreateEventDto;
import org.semester.dto.eventDto.OnUpdateEventDto;
import org.semester.dto.userDto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    EventDto addEvent(OnCreateEventDto onCreateEventDto, String email);
    List<EventDto> getEvents(Integer page);
    List<EventDto> findByNameContaining(String name, Integer page);
    EventDto findById(Long id);
    List<UserDto> getSubscribers(Long id, Integer page);
    Boolean updateEvent(OnUpdateEventDto onUpdateEventDto, String email);
    Boolean deleteEvent(Long id);
    Boolean addImage(Long id, MultipartFile file, String email);
    Boolean deleteImages(Long id);
    void deleteImagesByName(List<String> names);
    byte[] getImage(Long id, Integer number);
    byte[] getImage(String name);
    List<CommentDto> getComments(Long id, Integer page);
    CommentDto addComment(OnAddCommentDto onAddCommentDto, Long eventId, String email);
}
