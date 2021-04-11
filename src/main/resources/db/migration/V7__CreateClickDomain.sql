CREATE TABLE click_domain (
    click_id UUID NOT NULL REFERENCES click (click_id),
    domain_id UUID NOT NULL REFERENCES domain (domain_id),
    UNIQUE (click_id, domain_id)
);