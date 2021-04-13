package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserAccessService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public UserAccessService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<Users> getAllUsers() {
                return jdbcTemplate.query("SELECT user_id, name, password FROM users", mapUsersFomDb());
        }

        Users getUser(String name, String password) {
                return jdbcTemplate.queryForObject(
                                "SELECT user_id, name, password FROM users WHERE name = ? AND password = ?",
                                mapUsersFomDb(), new Object[] { name, password });
        }

        int insertUser(UUID userId, Users user) {
                return jdbcTemplate.update("INSERT INTO users ( user_id, name, password ) VALUES (?, ?, ?)", userId,
                                user.getName(), user.getPassword());
        }

        private RowMapper<Users> mapUsersFomDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");
                        return new Users(userId, name, password);
                };
        }

        boolean isPasswordTaken(String password) {
                return jdbcTemplate.queryForObject("SELECT EXISTS ( SELECT 1 FROM users WHERE password = ? )",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { password });
        }

        int deleteUser(UUID userId) {
                return jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
        }

        int updatePassword(UUID userId, String password) {
                return jdbcTemplate.update("UPDATE users SET password = ? WHERE user_id = ?", password, userId);
        }

        int updateName(UUID userId, String name) {
                return jdbcTemplate.update("UPDATE users SET name = ? WHERE user_id = ?", name, userId);
        }
}
