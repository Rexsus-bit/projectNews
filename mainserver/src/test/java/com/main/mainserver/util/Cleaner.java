package com.main.mainserver.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

public class Cleaner {
    public static void cleanData(JdbcTemplate jdbcTemplate){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "news", "users", "comments", "likes", "stats_records");
        jdbcTemplate.execute("ALTER SEQUENCE news_news_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE users_user_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE likes_like_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE comments_comment_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE stats_records_stats_id_seq RESTART WITH 1");

    }
}
