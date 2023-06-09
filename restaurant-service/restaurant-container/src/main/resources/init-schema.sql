DROP SCHEMA IF EXISTS "restaurant" CASCADE;

CREATE SCHEMA "restaurant";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "restaurant".restaurants
(
    restaurant_id uuid NOT NULL,
    product_id uuid NOT NULL,
    restaurant_name character varying COLLATE pg_catalog."default" NOT NULL,
    restaurant_active boolean  default true,
    product_name character varying COLLATE pg_catalog."default" NOT NULL,
    product_price numeric(10,2) NOT NULL,
    CONSTRAINT restaurants_pkey PRIMARY KEY (restaurant_id)
);

DROP MATERIALIZED VIEW IF EXISTS restaurant.order_restaurant_m_view;

CREATE MATERIALIZED VIEW restaurant.order_restaurant_m_view
TABLESPACE pg_default
AS
SELECT restaurant_id,
       product_id,
       restaurant_name,
       restaurant_active,
       product_name,
       product_price
FROM restaurant.restaurants
    WITH DATA;

refresh materialized VIEW restaurant.order_restaurant_m_view;

DROP function IF EXISTS restaurant.order_restaurant_m_view;

CREATE OR replace function restaurant.refresh_order_restaurant_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW restaurant.order_restaurant_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_restaurant_m_view ON restaurant.restaurants;

CREATE trigger refresh_refresh_order_restaurant_m_view
    after INSERT OR UPDATE OR DELETE OR truncate
                    ON restaurant.restaurants FOR each statement
                        EXECUTE PROCEDURE restaurant.refresh_order_restaurant_m_view();