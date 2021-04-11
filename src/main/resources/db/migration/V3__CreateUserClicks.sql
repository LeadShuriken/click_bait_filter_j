CREATE TABLE click (
    click_id UUID PRIMARY KEY NOT NULL,
    atTime DATE NOT NULL
);

CREATE TABLE user_click (
    user_id UUID NOT NULL REFERENCES users (user_id),
    click_id UUID NOT NULL REFERENCES click (click_id),
    UNIQUE (user_id, click_id)
);