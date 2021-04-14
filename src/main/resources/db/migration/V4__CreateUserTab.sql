CREATE TABLE IF NOT EXISTS plugin.tab (
    tab_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    index INTEGER CHECK (index >= 1)
);

CREATE TABLE IF NOT EXISTS plugin.user_tab (
    user_id UUID NOT NULL REFERENCES plugin.users (user_id),
    tab_id UUID NOT NULL REFERENCES plugin.tab (tab_id),
    PRIMARY KEY (user_id, tab_id)
);