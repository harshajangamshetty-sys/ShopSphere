CREATE TABLE categories (
                            id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            name       VARCHAR(100) NOT NULL UNIQUE,
                            created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE products (
                          id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          seller_id   BIGINT         NOT NULL REFERENCES users (id),
                          category_id BIGINT         NOT NULL REFERENCES categories (id),
                          name        VARCHAR(200)   NOT NULL,
                          description TEXT,
                          price       NUMERIC(12, 2) NOT NULL,
                          stock       INTEGER        NOT NULL DEFAULT 0,
                          active      BOOLEAN        NOT NULL DEFAULT TRUE,
                          version     BIGINT         NOT NULL DEFAULT 0,
                          created_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),
                          updated_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),
                          CONSTRAINT chk_products_price CHECK (price >= 0),
                          CONSTRAINT chk_products_stock CHECK (stock >= 0)
);