-- Solo las skills con competencia: las que no tienen fila son NONE.
CREATE TABLE character_skills (
    character_id UUID        NOT NULL REFERENCES characters (id) ON DELETE CASCADE,
    skill        VARCHAR(30) NOT NULL,
    proficiency  VARCHAR(20) NOT NULL CHECK (proficiency IN ('PROFICIENT', 'EXPERTISE')),
    PRIMARY KEY (character_id, skill)
);
