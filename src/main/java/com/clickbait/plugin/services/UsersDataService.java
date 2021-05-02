package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.clickbait.plugin.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UsersDataService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public UsersDataService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<User> getAllUsers() {
                return jdbcTemplate.query("SELECT * FROM plugin.get_all_users()", mapUsersFomDb());
        }

        User getUser(UUID id, String name, String password) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.get_user(?, ?, ?)", mapUserFomDb(),
                                new Object[] { id, name, password });
        }

        User getServiceUser(UUID id, String name, String password) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.get_user(?, ?, ?, ?)", mapUserFomDb(),
                                new Object[] { id, name, password, true });
        }

        UUID insertUser(String name, String password, String role) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.insert_user(?, ?, ?::plugin.user_role_type)",
                                (resultSet, i) -> UUID.fromString(resultSet.getString("insert_user")),
                                new Object[] { name, password, role });
        }

        private RowMapper<User> mapUsersFomDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String name = resultSet.getString("name");
                        String role = resultSet.getString("role");
                        List<ApplicationUserPrivilege> privileges = Arrays
                                        .stream((String[]) resultSet.getArray("privileges").getArray())
                                        .map(ApplicationUserPrivilege::valueOf).collect(Collectors.toList());
                        return new User(userId, name, ApplicationUserRole.valueOf(role), privileges);
                };
        }

        private RowMapper<User> mapUserFomDb() {
                return (resultSet, i) -> {
                        String userIdStr = resultSet.getString("user_id");
                        UUID userId = UUID.fromString(userIdStr);
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");
                        String role = resultSet.getString("role");
                        Boolean enabled = resultSet.getBoolean("enabled");
                        Boolean accountExpired = resultSet.getBoolean("account_expired");
                        Boolean accountLocked = resultSet.getBoolean("account_locked");
                        Boolean credExpired = resultSet.getBoolean("cred_expired");
                        List<ApplicationUserPrivilege> privileges = Arrays
                                        .stream((String[]) resultSet.getArray("privileges").getArray())
                                        .map(ApplicationUserPrivilege::valueOf).collect(Collectors.toList());
                        return new User(userId, name, password, ApplicationUserRole.valueOf(role), privileges, enabled,
                                        accountExpired, accountLocked, credExpired);
                };
        }

        boolean isPasswordTaken(String password) {
                return jdbcTemplate.queryForObject("SELECT * FROM plugin.is_password_taken(?)",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { password });
        }

        int deleteUser(UUID userId) {
                return jdbcTemplate.update("CALL plugin.delete_user(?)", userId);
        }

        int updatePassword(UUID userId, String password) {
                return jdbcTemplate.update("CALL plugin.update_password(?,?)", userId, password);
        }

        int updateName(UUID userId, String name) {
                return jdbcTemplate.update("CALL plugin.update_name(?,?)", userId, name);
        }

        int updateUser(UUID userId, String name, String password, String role, String[] privileges) {
                return jdbcTemplate.update(
                                "CALL plugin.update_user(?, ?, ?, ?::plugin.user_role_type, ?::plugin.privilege_type[])",
                                userId, name, password, role, privileges);
        }

        int addPrivilige(UUID userId, String[] privileges) {
                return jdbcTemplate.update("CALL plugin.add_privilege(?, ?::plugin.privilege_type[])", userId,
                                privileges);
        }

        int removePrivilige(UUID userId, String[] privileges) {
                return jdbcTemplate.update("CALL plugin.remove_privilege(?, ?::plugin.privilege_type[])", userId,
                                privileges);
        }

        int activateUser(UUID userId, Boolean enabled, Boolean accountExpired, Boolean accountLocked,
                        Boolean credExpired) {
                return jdbcTemplate.update("CALL plugin.user_authentication(?, ?, ?, ?, ?)", userId, enabled,
                                accountExpired, accountLocked, credExpired);
        }
}
