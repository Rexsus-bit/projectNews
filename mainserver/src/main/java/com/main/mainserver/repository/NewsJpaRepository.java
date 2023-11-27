package com.main.mainserver.repository;

import com.main.mainserver.model.news.News;

import com.main.mainserver.model.news.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsJpaRepository extends JpaRepository<News, Long> {

    @Query(value = "SELECT n.news_id, title, description, news_text, news_status, datetime, publisher_id " +
            "FROM news n LEFT JOIN likes l ON n.news_id = l.news_id LEFT JOIN comments c ON n.news_id = c.news_id " +
            "WHERE n.news_status = 'PUBLISHED' " +
            "GROUP BY n.news_id ORDER BY COUNT(DISTINCT l.like_id) DESC, COUNT(DISTINCT c.comment_id) DESC " +
            "LIMIT ?",
            nativeQuery = true)
    List<News> findAllAndSortByLikesAndComments(Integer limit);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE News n SET n.newsStatus = :newsStatus WHERE n.id = :newsId and n.newsStatus = 'CREATED'")
    int setNewsStatus(NewsStatus newsStatus, Long newsId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM News n WHERE n.id = :newsId")
    int deleteNewsById(Long newsId);

    boolean existsByIdAndNewsStatus(Long id, NewsStatus newsStatus);

}
