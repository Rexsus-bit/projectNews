package com.main.mainserver.mapper;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.comment.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

}
