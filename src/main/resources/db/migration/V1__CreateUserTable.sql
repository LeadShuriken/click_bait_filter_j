CREATE SCHEMA IF NOT EXISTS plugin;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$ BEGIN
    CREATE DOMAIN plugin.id_type UUID;
    CREATE DOMAIN plugin.link_type VARCHAR(300);
    CREATE DOMAIN plugin.user_name_type VARCHAR(100);
    CREATE DOMAIN plugin.domain_name_type VARCHAR(100);
    CREATE DOMAIN plugin.user_password_type VARCHAR(200);
    CREATE TYPE plugin.user_role_type AS ENUM ('ADMIN', 'USER');
    CREATE TYPE plugin.user_privilege_type AS ENUM (
        'USERS_READ',
        'USERS_WRITE',
        'CLICKS_READ',
        'CLICKS_WRITE',
        'DOMAINS_READ',
        'DOMAINS_WRITE',
        'FROM_ROLE'
    );
    CREATE TYPE plugin.user_with_role AS (
        user_id plugin.id_type,
        name plugin.user_name_type,
        password plugin.user_password_type,
        role plugin.user_role_type
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS plugin.users (
    user_id plugin.id_type DEFAULT uuid_generate_v4() PRIMARY KEY,
    name plugin.user_name_type NOT NULL,
    password plugin.user_password_type NOT NULL UNIQUE
);
