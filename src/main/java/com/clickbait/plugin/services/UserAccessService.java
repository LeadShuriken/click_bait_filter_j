package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import com.clickbait.plugin.security.Role;
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

        List<User> getAllUsers() {
                return jdbcTemplate.query("SELECT * FROM plugin.get_all_users()", mapUsersFomDb());
        }

        User getUser(String name, String password) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.get_user(?, ?)", mapUsersFomDb(),
                                new Object[] { name, password });
        }

        int insertUser(String name, String password, String role) {
                return jdbcTemplate.update("CALL plugin.insert_user(?, ?, ?)", name, password, role);
        }

        private RowMapper<User> mapUsersFomDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");
                        String role = resultSet.getString("role");
                        return new User(userId, name, password, Role.valueOf(role));
                };
        }

        boolean isPasswordTaken(String password) {
                return jdbcTemplate.queryForObject("SELECT EXISTS ( SELECT 1 FROM plugin.users WHERE password = ? )",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { password });
        }

        int deleteUser(UUID userId) {
                return jdbcTemplate.update("DELETE FROM plugin.users WHERE user_id = ?", userId);
        }

        int updatePassword(UUID userId, String password) {
                return jdbcTemplate.update("UPDATE plugin.users SET password = ? WHERE user_id = ?", password, userId);
        }

        int updateName(UUID userId, String name) {
                return jdbcTemplate.update("UPDATE plugin.users SET name = ? WHERE user_id = ?", name, userId);
        }

        int updateRole(UUID userId, String role) {
                return jdbcTemplate.update("UPDATE plugin.role SET name = ? WHERE role_id = ?", role, userId);
        }
}
