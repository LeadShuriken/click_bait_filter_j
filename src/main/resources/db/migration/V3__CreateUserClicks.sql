CREATE TABLE IF NOT EXISTS plugin.click (
    click_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    atTime DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS plugin.user_click (
    user_id UUID NOT NULL REFERENCES plugin.users (user_id),
    click_id UUID NOT NULL REFERENCES plugin.click (click_id),
    PRIMARY KEY (user_id, click_id)
);


