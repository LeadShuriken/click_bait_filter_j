CREATE TABLE IF NOT EXISTS plugin.click_domain (
    click_id UUID NOT NULL REFERENCES plugin.click (click_id),
    domain_id UUID NOT NULL REFERENCES plugin.domain (domain_id),
    UNIQUE (click_id, domain_id)
);