CREATE TABLE role (
    role_id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(5) NOT NULL
    CHECK (
        name = 'ADMIN' OR
        name = 'admin' OR
        name = 'USER'  OR
        name = 'user'
    )
);

CREATE TABLE user_role (
    user_id UUID NOT NULL REFERENCES users (user_id),
    role_id UUID NOT NULL REFERENCES role (role_id),
    UNIQUE (user_id, role_id)
);