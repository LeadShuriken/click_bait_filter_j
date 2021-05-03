package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TabDataService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public TabDataService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<UserTab> getAllTabs() {
                return jdbcTemplate.query("SELECT * FROM plugin.get_tabs(null)", mapTabsFomDb());
        }

        List<UserTab> getUserTabs(UUID userId) {
                return jdbcTemplate.query("SELECT * FROM plugin.get_tabs(?)", mapTabsFomDb(), new Object[] { userId });
        }

        UserTab getUserTab(UUID userId, int index) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.get_tab_data(?, ?)", mapTabFomDb(),
                                new Object[] { userId, index });
        }

        UUID insertTab(UUID userId, String name, int index) {
                return jdbcTemplate.queryForObject("CALL plugin.insert_tab(?, ?, ?)",
                                (resultSet, i) -> UUID.fromString(resultSet.getString("tad_id")),
                                new Object[] { userId, name, index });
        }

        private RowMapper<UserTab> mapTabFomDb() {
                return (resultSet, i) -> {
                        int tabId = resultSet.getInt("index");
                        String name = resultSet.getString("name");
                        Map<String, Float> links = Arrays.stream((Object[]) resultSet.getArray("links").getArray())
                                        .map(UserClick::valueOf).collect(Collectors.toMap(UserClick::getLink, UserClick::getScore));
                        return new UserTab(tabId, name, links);
                };
        }

        private RowMapper<UserTab> mapTabsFomDb() {
                return (resultSet, i) -> {
                        int tabId = resultSet.getInt("index");
                        String name = resultSet.getString("name");
                        return new UserTab(tabId, name, null);
                };
        }
}
