CREATE TABLE tab (
    tab_id UUID PRIMARY KEY NOT NULL,
    index INTEGER CHECK (index >= 1)
);

CREATE TABLE user_tab (
    user_id UUID NOT NULL REFERENCES users (user_id),
    tab_id UUID NOT NULL REFERENCES tab (tab_id),
    UNIQUE (user_id, tab_id)
);