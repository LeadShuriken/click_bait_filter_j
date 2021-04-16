CREATE TABLE IF NOT EXISTS plugin.privilege (
    privilege_id plugin.id_type DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    name plugin.user_privilege_type DEFAULT 'FROM_ROLE' NOT NULL
);

CREATE TABLE IF NOT EXISTS plugin.user_privilege (
    user_id plugin.id_type NOT NULL,
    privilege_id plugin.id_type NOT NULL,
    FOREIGN KEY (user_id) REFERENCES plugin.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (privilege_id) REFERENCES plugin.privilege (privilege_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, privilege_id)
);
