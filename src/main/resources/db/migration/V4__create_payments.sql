CREATE TABLE payments (
                          id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          order_id            BIGINT         NOT NULL REFERENCES orders (id),
                          provider            VARCHAR(30)    NOT NULL,
                          provider_order_id   VARCHAR(100),
                          provider_payment_id VARCHAR(100) UNIQUE,
                          amount              NUMERIC(12, 2) NOT NULL,
                          currency            CHAR(3)        NOT NULL DEFAULT 'INR',
                          status              VARCHAR(20)    NOT NULL DEFAULT 'CREATED',
                          failure_reason      VARCHAR(255),
                          created_at          TIMESTAMPTZ    NOT NULL DEFAULT now(),
                          updated_at          TIMESTAMPTZ    NOT NULL DEFAULT now(),
                          CONSTRAINT chk_payments_amount CHECK (amount > 0),
                          CONSTRAINT chk_payments_status CHECK (status IN ('CREATED', 'SUCCESS', 'FAILED'))
);