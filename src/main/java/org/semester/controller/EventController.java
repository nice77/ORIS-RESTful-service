package org.semester.controller;

import lombok.AllArgsConstructor;
import org.semester.dto.EventDto;
import org.semester.dto.UserDto;
import org.semester.entity.Event;
import org.semester.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

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
    public void addEvent(@RequestBody Event event) {
        System.out.println("Event: " + event);
        eventService.addEvent(event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @PostMapping("/{id}/event-image")
    public void addImage(@PathVariable Long id, MultipartFile file) {
        eventService.addImage(id, file);
    }

    @DeleteMapping("/{id}/event-image")
    public void deleteImage(@PathVariable Long id) {
        eventService.deleteImage(id);
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