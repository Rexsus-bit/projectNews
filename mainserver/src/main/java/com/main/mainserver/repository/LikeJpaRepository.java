package com.main.mainserver.repository;

import com.main.mainserver.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface LikeJpaRepository extends JpaRepository<Like, Long> {

    @Transactional
    @Modifying (clearAutomatically = true)
    @Query(value = "DELETE FROM Like l WHERE l.userId = :userId and l.newsId = :newsId")
    int deleteLikeByUserIdAndNewsId(Long userId, Long newsId);

}
