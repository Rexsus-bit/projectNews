package com.main.mainserver.repository;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsCriteriaRepository {

    private final EntityManager entityManager;

    public List<News> findNewsByCustomCriteria(String text, List<Long> users, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, Integer from, Integer size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> query = cb.createQuery(News.class);
        Root<News> root = query.from(News.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.in(root.get("newsStatus")).value(NewsStatus.PUBLISHED));

        if (users != null) {
            predicates.add(cb.in(root.get("publisher").get("id")).value(users));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThan(root.get("dateTime"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThan(root.get("dateTime"), rangeEnd));
        }

        if (text != null) {
            predicates.add(cb.or(cb.like(cb.upper(root.get("title")),
                    "%" + text.toUpperCase() + "%"), cb.like(cb.upper(root.get("description")),
                    "%" + text.toUpperCase() + "%"), cb.like(cb.upper(root.get("text")),
                    "%" + text.toUpperCase() + "%")));
        }

        return entityManager.createQuery(query.select(root)
                        .where(cb.and(predicates.toArray(new Predicate[]{}))))
                .setFirstResult(from).setMaxResults(size).getResultList();
    }

}
