CREATE TABLE IF NOT EXISTS plugin.link (
    link_id plugin.id_type PRIMARY KEY,
    domain_id plugin.id_type NOT NULL,
    link plugin.link_type UNIQUE NOT NULL,
    count BIGINT CHECK (count >= 1) DEFAULT 1,
    last_clicked TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (domain_id) REFERENCES plugin.domain (domain_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS plugin.click (
    click_id plugin.id_type DEFAULT uuid_generate_v4() PRIMARY KEY,
    link_id plugin.id_type NOT NULL,
    user_id plugin.id_type NOT NULL,
    at_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES plugin.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES plugin.link (link_id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION plugin.add_click(
    user_id_p plugin.id_type,
    domain_p plugin.domain_name_type,
    link_p plugin.link_type
)
RETURNS plugin.id_type 
LANGUAGE plpgsql
AS $$
    DECLARE ident CONSTANT plugin.id_type := uuid_generate_v4();
BEGIN
    WITH ROWS AS (
        INSERT INTO plugin.link ( link_id, link, domain_id ) VALUES (ident, link_p, 
            (SELECT domain_id FROM plugin.domain WHERE name = domain_p))
        ON CONFLICT (link) DO UPDATE SET count = plugin.link.count + 1 RETURNING plugin.link.link_id
    )
    INSERT INTO plugin.click ( link_id, user_id ) VALUES ((SELECT link_id FROM ROWS), user_id_p);
    RETURN ident;
    COMMIT;
EXCEPTION 
  WHEN OTHERS THEN 
    ROLLBACK;
END;
$$;

CREATE OR REPLACE FUNCTION plugin.get_clicks()
RETURNS TABLE (
    user_id plugin.id_type,
    domain plugin.domain_name_type,
    link plugin.link_type,
    at_time TIMESTAMPTZ
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u_click.user_id,
        u_domain.name as domain,
        u_link.link,
        u_click.at_time
    FROM plugin.click AS u_click 
        INNER JOIN plugin.link AS u_link USING (link_id) 
        INNER JOIN plugin.domain AS u_domain 
        ON u_domain.domain_id = u_link.domain_id;
END;
$$;