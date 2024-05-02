package org.semester.controller;

import lombok.AllArgsConstructor;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.eventDto.OnCreateEventDto;
import org.semester.dto.userDto.UserDto;
import org.semester.entity.Event;
import org.semester.service.EventService;
import org.semester.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private UserService userService;

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @GetMapping(params = {"name", "page"})
    public List<EventDto> getEventByNameContaining(@RequestParam String name, @RequestParam Integer page) {
        return eventService.findByNameContaining(name, page);
    }

    @GetMapping(params = {"page"})
    public List<EventDto> getEvents(@RequestParam Integer page) {
        return eventService.getEvents(page);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody OnCreateEventDto onCreateEventDto, Principal principal) {
        return eventService.addEvent(onCreateEventDto, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/{id}/event-image")
    public Boolean addImage(@PathVariable Long id, MultipartFile file, Principal principal) {
        return eventService.addImage(id, file, principal.getName());
    }

    @DeleteMapping("/{id}/event-image")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        if (eventService.deleteImage(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping(value = "/{id}/event-image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable Long id,  @RequestParam Integer number) {
        return eventService.getImage(id, number);
    }

    @GetMapping(value="/event-image/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable String fileName) {
        return eventService.getImage(fileName);
    }

    @GetMapping("/{id}/subscribers")
    public List<UserDto> getSubscribers(@PathVariable Long id, @RequestParam Integer page) {
        return eventService.getSubscribers(id, page);
    }
}