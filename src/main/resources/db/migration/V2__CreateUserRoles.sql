CREATE TABLE IF NOT EXISTS plugin.role (
    role_id plugin.id_type PRIMARY KEY,
    name plugin.user_role_type DEFAULT 'USER' NOT NULL,
    FOREIGN KEY (role_id) REFERENCES plugin.users (user_id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION plugin.get_all_users()
RETURNS SETOF plugin.user_with_role
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT users.user_id, users.name, users.password, role.name as role
    FROM plugin.users INNER JOIN plugin.role ON role.role_id = users.user_id;
END;
$$;

CREATE OR REPLACE FUNCTION plugin.insert_user(
    name_p plugin.user_name_type,
    password_p plugin.user_password_type,
    role_p plugin.user_role_type
)
RETURNS plugin.id_type
LANGUAGE plpgsql
AS $$
    DECLARE ident CONSTANT plugin.id_type := uuid_generate_v4();
BEGIN
    INSERT INTO plugin.users (user_id, name, password ) VALUES (ident, name_p, password_p);
    INSERT INTO plugin.role (role_id, name) VALUES (ident, role_p);
    RETURN ident;
    COMMIT;
END;
$$;

CREATE OR REPLACE FUNCTION plugin.get_user(
    name_p plugin.user_name_type,
    password_p plugin.user_password_type
)
RETURNS SETOF plugin.user_with_role
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT users.user_id, users.name, users.password, role.name as role
    FROM plugin.users INNER JOIN plugin.role ON role.role_id = users.user_id
    WHERE users.name = name_p AND users.password = password_p;
END;
$$;

CREATE OR REPLACE PROCEDURE plugin.update_user(
    user_id_p plugin.id_type,
    name_p plugin.user_name_type,
    password_p plugin.user_password_type,
    role_p plugin.user_role_type
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE plugin.users SET name = name_p, password = password_p WHERE user_id = user_id_p;
    UPDATE plugin.role SET name = role_p WHERE role_id = user_id_p;
    COMMIT;
END;
$$;