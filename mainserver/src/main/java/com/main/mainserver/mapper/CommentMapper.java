package com.main.mainserver.mapper;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.comment.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toCommentDto (Comment comment);

}
