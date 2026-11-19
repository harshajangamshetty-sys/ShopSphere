CREATE TABLE cart_items (
                            id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            user_id    BIGINT      NOT NULL REFERENCES users (id),
                            product_id BIGINT      NOT NULL REFERENCES products (id),
                            quantity   INTEGER     NOT NULL,
                            created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                            CONSTRAINT chk_cart_items_quantity CHECK (quantity > 0),
                            CONSTRAINT uq_cart_items_user_product UNIQUE (user_id, product_id)
);

CREATE TABLE orders (
                        id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        user_id      BIGINT         NOT NULL REFERENCES users (id),
                        status       VARCHAR(20)    NOT NULL DEFAULT 'PENDING_PAYMENT',
                        total_amount NUMERIC(12, 2) NOT NULL,
                        version      BIGINT         NOT NULL DEFAULT 0,
                        created_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),
                        updated_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),
                        CONSTRAINT chk_orders_total CHECK (total_amount >= 0),
                        CONSTRAINT chk_orders_status CHECK (status IN (
                                                                       'PENDING_PAYMENT', 'PAYMENT_FAILED', 'PAID',
                                                                       'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

CREATE TABLE order_items (
                             id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             order_id     BIGINT         NOT NULL REFERENCES orders (id),
                             product_id   BIGINT         NOT NULL REFERENCES products (id),
                             seller_id    BIGINT         NOT NULL REFERENCES users (id),
                             product_name VARCHAR(200)   NOT NULL,
                             unit_price   NUMERIC(12, 2) NOT NULL,
                             quantity     INTEGER        NOT NULL,
                             CONSTRAINT chk_order_items_price CHECK (unit_price >= 0),
                             CONSTRAINT chk_order_items_quantity CHECK (quantity > 0)
);