package org.semester.service;

import org.semester.dto.EventDto;
import org.semester.dto.UserDto;
import org.semester.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Event addEvent(Event event);
    List<EventDto> getEvents(Integer page);
    List<EventDto> findByNameContaining(String name, Integer page);
    EventDto findById(Long id);
    List<UserDto> getSubscribers(Long id, Integer page);
    void deleteEvent(Long id);
    Boolean addImage(Long id, MultipartFile file, String email);
    void deleteImage(Long id);
    byte[] getImage(Long id, Integer number);
    byte[] getImage(String name);
}
