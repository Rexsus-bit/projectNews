package com.main.mainserver.controller;

import com.main.mainserver.mapper.CommentMapper;
import com.main.mainserver.mapper.NewsShortDtoMapper;
import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

   private final UserService userService;


// TODO   getNewsByDate — пользователь получает новости опубликованные в указанную дату
//  TODO  getNewsOfTheAuthor — пользователь получает новости конкретного автора (издания)
// TODO   getNewsByApi — пользователь получает подборку новостей из внешнего API. Будет использоваться этот сервис https://newsapi.org/ или возможно какой-то аналогичный
//  TODO  createCommentToTheNews — пользователь оставляет комментарий под новостью

//TODO     createLikeToTheNews — пользователь ставит лайк новости
// TODO    deleteLikeToTheNews — пользователь убирает ранее поставленный лайк новости

//    getTheMostIntrestingNews — пользователь получает новости, отсортированные по количеству лайков и/или комментариев

// TODO !!!! Подумать на счет поиска по теме
    @GetMapping("/news")
    public List<NewsShortDto> findNewsByDates(@RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
//        List<News> newsList = userService.findNews(rangeStart, rangeEnd , from, size);
//        newsList = new ArrayList<>(){{
//            User user = new User();
//            user.setId(1L);
//            user.setName("Rone");
//
//            News news = new News();
//            news.setPublisher(user);
//            news.setLikesList(new HashSet<User>() {{
//                add(user);
//            }
//            });
//            news.setComments(new ArrayList<Comment>(){{
//                Comment comment = new Comment(1L, "sss", user, news, LocalDateTime.now());
//                add(comment);
//            }});
//
//            news.setTitle("1L");
//
//            add(news);
//            add(new News());
//        }};
//        System.out.println(newsList);
//        List<NewsDto> li = NewsDtoMapper.INSTANCE.toNewsDtoList(newsList);
//        System.out.println(NewsDtoMapper.INSTANCE.toNewsDtoList(newsList));

        return NewsShortDtoMapper.INSTANCE.toNewsShortDtoList(userService.findNews(rangeStart, rangeEnd , from, size));
    }

    @GetMapping("/news/{newsId}")
    public List<?> getNewsFromWeatherApiService(@PathVariable Long newsId,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        UserService.getNewsFromWeatherApiService();
        return null;
    }



    @PostMapping("/{userId}/comment") // TODO что должно быть path variable?, Удаление и добавление коммента?
    public CommentDto addCommentToTheNews(@PathVariable Long userId,
                                          @RequestParam Long newsId,
                                          @RequestParam String commentText) {
        return CommentMapper.INSTANCE.toCommentDto(userService.addCommentToNews(userId, newsId, commentText));
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFromUser(@PathVariable("id") Long newsId,
                                @PathVariable Long userId) {
        userService.addLikeFromUser(newsId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromUser(@PathVariable("id") Long newsId,
                                   @PathVariable Long userId) {
        userService.deleteLikeFromUser(newsId, userId);
    }

    @GetMapping("/popular")
    public List<NewsShortDto> getTopNews(@RequestParam(defaultValue = "10") int count) {

        return userService.getTopNews(count);
    }


}
