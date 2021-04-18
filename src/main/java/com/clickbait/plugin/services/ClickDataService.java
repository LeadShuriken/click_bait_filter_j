package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class ClickDataService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public ClickDataService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<UserClick> getAllClicks() {
                return jdbcTemplate.query("SELECT * FROM plugin.get_clicks()", mapClickFromDb());
        }

        UUID addClick(UUID userId, String domain, String link) {
                return jdbcTemplate.queryForObject("SELECT plugin.add_click(?, ?, ?)",
                                (resultSet, i) -> UUID.fromString(resultSet.getString("add_click")),
                                new Object[] { userId, domain, link });
        }

        private RowMapper<UserClick> mapClickFromDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String domain = resultSet.getString("domain");
                        String link = resultSet.getString("link");
                        LocalDate atTime = resultSet.getDate("at_time").toLocalDate();
                        return new UserClick(userId, domain, link, atTime);
                };
        }
}