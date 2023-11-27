package com.main.mainserver.model.comment;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @Column(name = "comment_date")
    private LocalDateTime creationTime;
}
