package org.semester.controller;


import lombok.AllArgsConstructor;
import org.semester.dto.ErrorDto;
import org.semester.dto.EventDto;
import org.semester.dto.UserDto;
import org.semester.entity.User;
import org.semester.service.UserService;
import org.semester.util.StaticString;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping(
            params = "page",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserDto> getUsers(@RequestParam(defaultValue = "0") Integer page) {
        return userService.getUsers(page);
    }

    @GetMapping(
            params = {"name", "page"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserDto> findByNameContaining(@RequestParam String name, @RequestParam Integer page) {
        return userService.findByNameContaining(name, page);
    }



    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping(
            value = "/{id}/subscribers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserDto> getSubscribers(@PathVariable Long id, @RequestParam Integer page) {
        return userService.getSusbcribers(id, page);
    }

    @GetMapping(
            value = "/{id}/subscribees",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserDto> getSubscribees(@PathVariable Long id, @RequestParam Integer page) {
        return userService.getSubscribees(id, page);
    }



    @GetMapping("/{id}/subscribed-events")
    public List<EventDto> getSubscribedEvents(@PathVariable Long id) {
        return userService.getSubscribedEvents(id);
    }

    @PostMapping("/event")
    public ResponseEntity<?> subscribeToEvent(Principal principal, @RequestParam Long eventId) {
        if (userService.subscribeToEvent(principal.getName(), eventId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/event")
    public ResponseEntity<?> unsubscribeFromEvent(Principal principal, @RequestParam Long eventId) {
        if (userService.unsubscribeFromEvent(principal.getName(), eventId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



    @GetMapping(value = "/{id}/user-image", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> getUserImage(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getProfileImage(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/user-image/{fileName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> getUserImage(@PathVariable("fileName") String fileName) {
        try {
            return ResponseEntity.ok(userService.getProfileImageByFileName(fileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/user-image")
    public ResponseEntity<?> addProfileImage(MultipartFile file, Principal principal) {
        if (userService.addProfileImage(file, principal.getName())) {
            System.out.println("Returnin image");
            return ResponseEntity.ok().build();
        }
        System.out.println("Not returnin image");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/user-image")
    public ResponseEntity<?> deleteProfileImage(Principal principal) {
        if (userService.deleteProfileImage(principal.getName())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



    @PostMapping(
            value = "/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addUser(@RequestBody User user) {
        UserDto userDto = userService.findByEmail(user.getEmail());
        if (userDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(StaticString.EMAIL_IN_USE.getValue()));
        }
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(Principal principal) {
        userService.deleteUser(userService.findByEmail(principal.getName()).getId());
    }
}
