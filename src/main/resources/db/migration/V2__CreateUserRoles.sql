CREATE TABLE IF NOT EXISTS plugin.role (
    role_id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(5) DEFAULT 'USER' NOT NULL
    CHECK (
        name = 'ADMIN' OR
        name = 'USER'
    ),
    FOREIGN KEY (role_id) REFERENCES plugin.users (user_id) ON DELETE CASCADE
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_with_role') THEN
        CREATE TYPE plugin.user_with_role AS (user_id UUID, name VARCHAR(100), password VARCHAR(200), role VARCHAR(5));
    END IF;
END
$$;

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

CREATE OR REPLACE FUNCTION plugin.get_user(name_p VARCHAR(100), password_p VARCHAR(200))
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

CREATE OR REPLACE PROCEDURE plugin.insert_user(name VARCHAR(100), password VARCHAR(100), role VARCHAR(5))
LANGUAGE plpgsql
AS $$
DECLARE
    DECLARE ident CONSTANT UUID := uuid_generate_v4();
BEGIN
    INSERT INTO plugin.users (user_id, name, password ) VALUES (ident, name, password);
    INSERT INTO plugin.role ( role_id, name ) VALUES (ident, role);
    COMMIT;
END;
$$;