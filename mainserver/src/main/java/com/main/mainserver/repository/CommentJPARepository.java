package com.main.mainserver.repository;

import com.main.mainserver.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentJPARepository extends JpaRepository<Comment, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Comment c WHERE c.id = :commentId AND c.owner.id = :ownerId")
    int deleteCommentByIdAndOwnerId(Long commentId, Long ownerId);

}
