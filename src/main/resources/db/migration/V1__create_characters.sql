CREATE TABLE characters (
    id           UUID         PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    level        INT          NOT NULL CHECK (level BETWEEN 1 AND 20),
    strength     INT          NOT NULL CHECK (strength BETWEEN 1 AND 30),
    dexterity    INT          NOT NULL CHECK (dexterity BETWEEN 1 AND 30),
    constitution INT          NOT NULL CHECK (constitution BETWEEN 1 AND 30),
    intelligence INT          NOT NULL CHECK (intelligence BETWEEN 1 AND 30),
    wisdom       INT          NOT NULL CHECK (wisdom BETWEEN 1 AND 30),
    charisma     INT          NOT NULL CHECK (charisma BETWEEN 1 AND 30)
);
