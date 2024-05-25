package org.semester.mappers;

import lombok.AllArgsConstructor;
import org.semester.dto.CommentDto;
import org.semester.dto.OnAddCommentDto;
import org.semester.entity.Comment;
import org.semester.entity.Event;
import org.semester.entity.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {

    private UserMapper userMapper;

    public CommentDto getCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .date(comment.getDate())
                .userDto(userMapper.getUserDto(comment.getUser()))
                .build();
    }

    public Comment getComment(OnAddCommentDto onAddCommentDto, Event event, User user) {
        return Comment.builder()
                .text(onAddCommentDto.getText())
                .date(onAddCommentDto.getDate())
                .event(event)
                .user(user)
                .build();
    }

}
