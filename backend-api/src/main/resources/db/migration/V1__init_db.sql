CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';


DO $$
DECLARE
    table_rec RECORD;
BEGIN
    -- Loop over all tables in the public schema (you can modify the schema if needed)
    FOR table_rec IN
        SELECT table_schema, table_name
        FROM information_schema.columns
        WHERE column_name = 'last_modified_at'
          AND table_schema NOT IN ('pg_catalog', 'information_schema') -- Exclude system schemas
          AND table_schema = 'public' -- Adjust schema if needed
    LOOP
        -- Generate the trigger creation statement for each table
        EXECUTE format(
            'CREATE TRIGGER %I_update_timestamp IF NOT EXISTS
             BEFORE UPDATE ON %I.%I
             FOR EACH ROW
             EXECUTE FUNCTION update_timestamp();',
            table_rec.table_name, table_rec.table_schema, table_rec.table_name
        );
    END LOOP;
END $$;


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create Local_users table
CREATE TABLE IF NOT EXISTS user_details
(
    id          UUID NOT NULL PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    email       VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(1000) NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(50) NOT NULL,
    last_modified_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by  VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_profiles
(
    id          UUID NOT NULL PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    email       VARCHAR(50) NOT NULL UNIQUE,
    image       VARCHAR(1000),
    is_active   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(50) NOT NULL,
    last_modified_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by  VARCHAR(50) NOT NULL
);

