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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
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
    @Column(name = "datetime")
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "likes",
            joinColumns = {@JoinColumn(name = "news_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> likesSet;
    @OneToMany(mappedBy = "news")
    private List<Comment> commentsList;

}
