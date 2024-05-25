package org.semester.controller;

import lombok.AllArgsConstructor;
import org.semester.dto.CommentDto;
import org.semester.dto.OnAddCommentDto;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.eventDto.OnCreateEventDto;
import org.semester.dto.eventDto.OnUpdateEventDto;
import org.semester.dto.userDto.UserDto;
import org.semester.service.EventService;
import org.semester.service.UserService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
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

    @PostMapping(value = "/{id}/event-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Boolean addImage(@PathVariable Long id, @RequestBody MultipartFile file, Principal principal) {
        return eventService.addImage(id, file, principal.getName());
    }

//    @PostMapping(value = "/{id}/event-image", params = {"fileList"})
//    public void loadImages(@PathVariable Long id, @RequestParam("fileList") List<MultipartFile> fileList, Principal principal) {
//        EventDto eventDto = eventService.findById(id);
//        fileList.forEach(file -> eventService.addImage(eventDto.getId(), file, principal.getName()));
//    }

    @PatchMapping()
    public void updateEvent(Principal principal, @RequestBody OnUpdateEventDto onUpdateEventDto) {
        eventService.updateEvent(onUpdateEventDto, principal.getName());
    }

//    @DeleteMapping("/{id}/event-image")
//    public ResponseEntity<?> deleteImages(@PathVariable Long id) {
//        if (eventService.deleteImages(id)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }

    @DeleteMapping("/{id}/event-image")
    public void deleteImage(@PathVariable Long id, Principal principal, @RequestBody List<String> imageNameList) {
        System.out.println("Deleting image");
        eventService.deleteImagesByName(imageNameList);
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



    @GetMapping("/{id}/comments")
    public List<CommentDto> getComments(@PathVariable Long id, @RequestParam("page") Integer page) {
        return eventService.getComments(id, page);
    }

    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable Long id, @RequestBody OnAddCommentDto onAddCommentDto, Principal principal) {
        return eventService.addComment(onAddCommentDto, id, principal.getName());
    }
}