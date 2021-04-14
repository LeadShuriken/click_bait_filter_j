CREATE TABLE IF NOT EXISTS plugin.domain (
    domain_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL
);