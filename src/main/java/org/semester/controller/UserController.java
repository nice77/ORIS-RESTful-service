package org.semester.controller;


import lombok.AllArgsConstructor;
import org.semester.dto.*;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.userDto.FullUserDto;
import org.semester.dto.userDto.RegisterUserDto;
import org.semester.dto.userDto.UserDto;
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

    @GetMapping(value = "/current-user")
    public Long getCurrentUser(Principal principal) {
        return userService.getFullUserByEmail(principal.getName()).getId();
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

    @PostMapping(
            value = "/{id}/subscribe"
    )
    public Boolean subscribe(@PathVariable Long id, Principal principal) {
        Long principalId = userService.getFullUserByEmail(principal.getName()).getId();
        return userService.subscribe(id, principalId);
    }

    @GetMapping(
            value = "/{id}/subscription"
    )
    public Boolean amISubscribedToUser(@PathVariable Long id, Principal principal) {
        Long principalId = userService.getFullUserByEmail(principal.getName()).getId();
        return userService.amISubscribedToUser(id, principalId);
    }

    @GetMapping("/{id}/created-events")
    public List<EventDto> getCreatedEvents(@PathVariable Long id, @RequestParam Integer page) {
        return userService.getCreatedEvents(id, page);
    }

    @GetMapping("/{id}/subscribed-events")
    public List<EventDto> getSubscribedEvents(@PathVariable Long id, @RequestParam Integer page) {
        return userService.getSubscribedEvents(id, page);
    }

    @GetMapping("/event")
    public Boolean amISubscribedToEvent(Principal principal, @RequestParam(name = "event_id") Long eventId) {
        return userService.amISubscribedToEvent(principal.getName(), eventId);
    }

    @PostMapping("/event")
    public ResponseEntity<?> subscribeToEvent(Principal principal, @RequestParam(name = "event_id") Long eventId) {
        if (userService.manageSubscriptionToEvent(principal.getName(), eventId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/event")
    public ResponseEntity<?> unsubscribeFromEvent(Principal principal, @RequestParam(name = "event_id") Long eventId) {
        if (userService.manageSubscriptionToEvent(principal.getName(), eventId)) {
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
            System.out.println(1);
            return ResponseEntity.ok(userService.getProfileImageByFileName(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/user-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProfileImage(MultipartFile file, Principal principal) {
        return ResponseEntity.ok("\"" + userService.addProfileImage(file, principal.getName()) + "\"");
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
    public ResponseEntity<?> addUser(@RequestBody RegisterUserDto registerUserDto) {
        UserDto userDto = userService.findByEmail(registerUserDto.getEmail());
        if (userDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(StaticString.EMAIL_IN_USE.getValue()));
        }
        return ResponseEntity.ok(userService.addUser(registerUserDto));
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, Principal principal) {
        FullUserDto fullUserDto = userService.getFullUserByEmail(principal.getName());
        if (!fullUserDto.getId().equals(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @DeleteMapping
    public void deleteUser(Principal principal) {
        userService.deleteUser(userService.findByEmail(principal.getName()).getId());
    }
}
