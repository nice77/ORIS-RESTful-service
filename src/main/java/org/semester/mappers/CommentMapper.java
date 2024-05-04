package org.semester.mappers;

import org.semester.dto.CommentDto;
import org.semester.entity.Comment;
import org.semester.entity.Event;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto getCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .date(comment.getDate())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .build();
    }

    public Comment getComment(CommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .date(commentDto.getDate())
                .event(event)
                .user(user)
                .build();
    }

}
