package org.semester.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.semester.dto.EventDto;
import org.semester.dto.UserDto;
import org.semester.entity.Event;
import org.semester.entity.EventImage;
import org.semester.entity.User;
import org.semester.mappers.EventMapper;
import org.semester.mappers.UserMapper;
import org.semester.repository.EventImageRepository;
import org.semester.repository.EventRepository;
import org.semester.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private EventImageRepository eventImageRepository;
    private UserRepository userRepository;
    private static final int PAGE_SIZE = 10;
    private Environment environment;
    private static final String envPath = "spring.servlet.multipart.location";
    private UserMapper userMapper;
    private EventMapper eventMapper;

    @Override
    public Event addEvent(Event event) {
        return eventRepository.saveAndFlush(event);
    }
    @Override
    public List<EventDto> getEvents(Integer page) {
        return eventRepository.findAll(PageRequest.of(page, PAGE_SIZE)).map(
                event -> eventMapper.getEventDto(event)
        ).toList();
    }

    @Override
    public List<EventDto> findByNameContaining(String name, Integer page) {
        return eventRepository.findByNameContaining(name, PageRequest.of(page, PAGE_SIZE))
                .stream()
                .map(event -> eventMapper.getEventDto(event))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto findById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return null;
        }
        Event event = optionalEvent.get();
        return eventMapper.getEventDto(event);
    }

    @Override
    public List<UserDto> getSubscribers(Long id, Integer page) {
        return eventRepository.findById(id).orElseThrow().getSubscribedUsers()
                .stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        deleteImageFromDisk(event.getEventImages());
        eventRepository.delete(event);
    }

    @Override
    public Boolean addImage(Long id, MultipartFile file, String email) {
        Event event = eventRepository.findById(id).orElseThrow();
        User user = userRepository.findByEmail(email);
        if (event.getAuthor().getId() != user.getId()) {
            return false;
        }
        String type = (file.getContentType().equals("image/png")) ? "png" : "jpg";
        String name = UUID.randomUUID() + "." + type;
        event.getEventImages().add(
                EventImage.builder()
                        .event(event)
                        .path(name)
                        .build()
        );
        try {
            file.transferTo(new File(environment.getProperty(envPath) + "/events/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        eventRepository.saveAndFlush(event);
        return true;
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        List<EventImage> images = event.getEventImages();
        deleteImageFromDisk(images);
        eventImageRepository.deleteByEventId(id);
    }

    @Override
    public byte[] getImage(Long id, Integer number) {
        List<EventImage> eventImages = eventRepository.findById(id).orElseThrow().getEventImages();
        String name = eventImages.get(number).getPath();
        try {
            return Files.readAllBytes(Path.of(environment.getProperty(envPath) + "/events/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getImage(String name) {
        try {
            return Files.readAllBytes(Path.of(environment.getProperty(envPath) + "/events/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteImageFromDisk(List<EventImage> images) {
        images.forEach(eventImage -> {
            try {
                String fullPath = environment.getProperty(envPath) + "/events/" + eventImage.getPath();
                Files.delete(Path.of(fullPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
