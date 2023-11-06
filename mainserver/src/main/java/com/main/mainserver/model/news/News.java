package com.main.mainserver.model.news;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "news")
@ToString
public class News {
// TODO оставить потом валидацию только в DTO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    @Column(name = "news_text")
    private String text;
    @Enumerated(EnumType.STRING)
    @Column(name = "news_status")
    NewsStatus newsStatus;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "datetime")
    private LocalDateTime dateTime;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User publisher;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // TODO OrphanRemoval уместно?
    @JoinColumn(name = "like_id")
    private Set<User> likesList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // TODO OrphanRemoval уместно?
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;


}
