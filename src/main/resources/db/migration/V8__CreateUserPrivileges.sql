CREATE TABLE IF NOT EXISTS plugin.privilege (
    privilege_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    name VARCHAR(15) DEFAULT 'FROM_ROLE' NOT NULL
    CHECK (
        name = 'USERS_READ'    OR
        name = 'USERS_WRITE'   OR
        name = 'CLICKS_READ'   OR
        name = 'CLICKS_WRITE'  OR
        name = 'DOMAINS_READ'  OR
        name = 'DOMAINS_WRITE' OR
        name = 'FROM_ROLE'
    )
);

CREATE TABLE IF NOT EXISTS plugin.user_privilege (
    user_id UUID NOT NULL REFERENCES plugin.users (user_id),
    privilege_id UUID NOT NULL REFERENCES plugin.privilege (privilege_id),
    PRIMARY KEY (user_id, privilege_id)
);

