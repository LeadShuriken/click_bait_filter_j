package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ClickDataService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public ClickDataService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<UserClick> getDomainClicks(UUID userId, String name) {
                return jdbcTemplate.query("SELECT * FROM plugin.get_clicks(?, ?)", mapClickFromDb(),
                                new Object[] { userId, name });
        }

        UUID addClick(UUID userId, String domain, String link, Float score) {
                return jdbcTemplate.queryForObject("SELECT plugin.insert_click(?, ?, ?, ?::plugin.bait_score)",
                                (resultSet, i) -> UUID.fromString(resultSet.getString("insert_click")),
                                new Object[] { userId, domain, link, score });
        }

        int createPageModel(String domain, List<Link> links) {
                String[] linkType = new String[links.size()];
                Float[] baitScore = new Float[links.size()];
                int count = 0;
                for (Link entry : links) {
                        linkType[count] = entry.getName();
                        baitScore[count++] = entry.getScore();
                }
                return jdbcTemplate.update(
                                "CALL plugin.create_page_model(?, ?::plugin.link_type[], ?::plugin.bait_score[])",
                                domain, linkType, baitScore);
        }

        private RowMapper<UserClick> mapClickFromDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String link = resultSet.getString("link");
                        LocalDate atTime = resultSet.getDate("at_time").toLocalDate();
                        return new UserClick(userId, link, atTime);
                };
        }
}
