package com.stat.statserver.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

public class Cleaner {
    public static void cleanData(JdbcTemplate jdbcTemplate){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "stats_records");
        jdbcTemplate.execute("ALTER SEQUENCE stats_records_stats_id_seq RESTART WITH 1");



    }
}
