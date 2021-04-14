CREATE TABLE IF NOT EXISTS plugin.link (
    link_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    link VARCHAR(300) NOT NULL,
    count BIGINT CHECK (count >= 1)
);

CREATE TABLE IF NOT EXISTS plugin.domain_link (
    domain_id UUID NOT NULL REFERENCES plugin.domain (domain_id),
    link_id UUID NOT NULL REFERENCES plugin.link (link_id),
    PRIMARY KEY (domain_id, link_id)
);