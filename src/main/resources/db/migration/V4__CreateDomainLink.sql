CREATE TABLE IF NOT EXISTS plugin.link (
    link_id plugin.id_type DEFAULT uuid_generate_v4() PRIMARY KEY,
    link plugin.link_type NOT NULL,
    count BIGINT CHECK (count >= 1),
    FOREIGN KEY (link_id) REFERENCES plugin.domain (domain_id) ON DELETE CASCADE
);
