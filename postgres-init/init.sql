-- Catalog database and user
CREATE DATABASE catalog_db;
CREATE USER catalog_user WITH ENCRYPTED PASSWORD 'catalog_pass';
GRANT ALL PRIVILEGES ON DATABASE catalog_db TO catalog_user;

-- Make catalog_user owner of public schema
\c catalog_db
ALTER SCHEMA public OWNER TO catalog_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO catalog_user;

-- Order database and user
CREATE DATABASE order_db;
CREATE USER order_user WITH ENCRYPTED PASSWORD 'order_pass';
GRANT ALL PRIVILEGES ON DATABASE order_db TO order_user;

-- Make order_user owner of public schema
\c order_db
ALTER SCHEMA public OWNER TO order_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO order_user;

-- Inventory database and user
CREATE DATABASE inventory_db;
CREATE USER inventory_user WITH ENCRYPTED PASSWORD 'inventory_pass';
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO inventory_user;

-- Make inventory_user owner of public schema
\c inventory_db
ALTER SCHEMA public OWNER TO inventory_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO inventory_user;