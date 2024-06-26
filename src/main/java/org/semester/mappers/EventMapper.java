package org.semester.mappers;

import org.semester.dto.EventDto;
import org.semester.entity.Event;
import org.semester.entity.EventImage;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDto getEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .authorId(event.getAuthor().getId())
                .date(event.getDate())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .eventImages(event.getEventImages().stream().map( path ->
                        "events/event-image/" + path.getPath()
                ).toList())
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
