CREATE TABLE link (
    link_id UUID PRIMARY KEY NOT NULL,
    link VARCHAR(300) NOT NULL,
    count BIGINT CHECK (count >= 1)
);

CREATE TABLE domain_link (
    domain_id UUID NOT NULL REFERENCES domain (domain_id),
    link_id UUID NOT NULL REFERENCES link (link_id),
    UNIQUE (domain_id, link_id)
);