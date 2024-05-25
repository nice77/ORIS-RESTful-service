package org.semester.mappers;

import lombok.AllArgsConstructor;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.eventDto.OnCreateEventDto;
import org.semester.entity.Event;
import org.semester.entity.EventImage;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EventMapper {

    public EventDto getEventDto(Event event) {
        List<EventImage> eventImages = event.getEventImages();
        if (eventImages == null) {
            eventImages = Collections.emptyList();
        }
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .authorId(event.getAuthor().getId())
                .date(event.getDate())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .eventImages(eventImages.stream().map( path ->
                        "events/event-image/" + path.getPath()
                ).toList())
                .build();
    }

    public Event getEventEntity(OnCreateEventDto onCreateEventDto, User author) {
        return Event.builder()
                .name(onCreateEventDto.getName())
                .description(onCreateEventDto.getDescription())
                .author(author)
                .date(onCreateEventDto.getDate())
                .latitude(onCreateEventDto.getLatitude())
                .longitude(onCreateEventDto.getLongitude())
                .commentList(Collections.emptyList())
                .build();
    }

    public Event getEventEntity(EventDto eventDto, User author) {
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .description(eventDto.getDescription())
                .author(author)
                .date(eventDto.getDate())
                .latitude(eventDto.getLatitude())
                .longitude(eventDto.getLongitude())
                .eventImages(eventDto.getEventImages().stream().map(path ->
                        EventImage.builder()
                                .path(path)
                                .build()
                ).toList())
                .build();
    }
}
