ALTER TABLE restaurants
    ADD COLUMN delivery_fee NUMERIC(10,2) NOT NULL DEFAULT 0
        CHECK (delivery_fee >= 0);
