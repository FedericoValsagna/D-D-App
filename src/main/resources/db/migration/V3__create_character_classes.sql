-- Clases del personaje (multiclase). position 0 es la clase inicial, la que da las salvaciones.
-- El nivel del personaje pasa a ser la suma de los niveles de clase.
CREATE TABLE character_classes (
    character_id    UUID        NOT NULL REFERENCES characters (id) ON DELETE CASCADE,
    position        INT         NOT NULL CHECK (position >= 0),
    character_class VARCHAR(20) NOT NULL,
    level           INT         NOT NULL CHECK (level BETWEEN 1 AND 20),
    PRIMARY KEY (character_id, position)
);

-- HP máximo cargado a mano.
ALTER TABLE characters ADD COLUMN max_hit_points INT CHECK (max_hit_points BETWEEN 1 AND 999);

-- Los personajes que ya existían no tienen clase: quedan como FIGHTER con el HP promedio,
-- para corregir después con PUT .../classes y PUT .../hit-points.
INSERT INTO character_classes (character_id, position, character_class, level)
SELECT id, 0, 'FIGHTER', level FROM characters;

UPDATE characters
SET max_hit_points = GREATEST(1, 10 + (level - 1) * 6 + FLOOR((constitution - 10) / 2.0)::INT * level);

ALTER TABLE characters ALTER COLUMN max_hit_points SET NOT NULL;
ALTER TABLE characters DROP COLUMN level;
