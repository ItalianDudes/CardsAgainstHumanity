-- TABLE: Key Parameters
CREATE TABLE IF NOT EXISTS key_parameters (
    param_key VARCHAR(32) NOT NULL PRIMARY KEY,
    param_value TEXT
);

-- TABLE: White Cards
CREATE TABLE IF NOT EXISTS white_cards (
    id INTEGER NOT NULL PRIMARY KEY,
    content TEXT,
    is_blank INTEGER NOT NULL DEFAULT 0
);

-- TABLE: Nigger Cards
CREATE TABLE IF NOT EXISTS black_cards (
    id INTEGER NOT NULL PRIMARY KEY,
    content TEXT NOT NULL,
    empty_fields INTEGER NOT NULL,
    CHECK (empty_fields > 0)
);