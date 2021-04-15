CREATE TABLE IF NOT EXISTS plugin.click (
    click_id plugin.id_type DEFAULT uuid_generate_v4() PRIMARY KEY,
    atTime DATE NOT NULL,
    FOREIGN KEY (click_id) REFERENCES plugin.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (click_id) REFERENCES plugin.link (link_id) ON DELETE CASCADE
);
