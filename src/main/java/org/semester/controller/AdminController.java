package org.semester.controller;

import lombok.AllArgsConstructor;
import org.semester.dto.EventDto;
import org.semester.dto.UserDto;
import org.semester.service.EventService;
import org.semester.service.UserService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private UserService userService;
    private EventService eventService;

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam("page") Integer page) {
        System.out.println("In controller");
        return ResponseEntity.ok(userService.getUsers(page));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/event")
    public ResponseEntity<List<EventDto>> getEvents(@RequestParam("page") Integer page) {
        return ResponseEntity.ok(eventService.getEvents(page));
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
