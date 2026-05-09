CREATE TABLE groups.bill_groups (
                                    id          BIGSERIAL       PRIMARY KEY,
                                    name        VARCHAR(100)    NOT NULL,
                                    description VARCHAR(255),
                                    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
                                    updated_at  TIMESTAMP
);

CREATE TABLE groups.participants (
                                     id          BIGSERIAL       PRIMARY KEY,
                                     group_id    BIGINT          NOT NULL,
                                     name        VARCHAR(100)    NOT NULL,
                                     email       VARCHAR(150)    NOT NULL,
                                     joined_at   TIMESTAMP       NOT NULL DEFAULT NOW(),
                                     CONSTRAINT fk_participant_group FOREIGN KEY (group_id) REFERENCES groups.bill_groups(id) ON DELETE CASCADE,
                                     CONSTRAINT uq_group_email      UNIQUE (group_id, email)
);

CREATE INDEX IF NOT EXISTS idx_participants_group_id ON groups.participants(group_id);
CREATE INDEX IF NOT EXISTS idx_participants_email     ON groups.participants(email);
CREATE INDEX IF NOT EXISTS idx_bill_groups_name       ON groups.bill_groups(name);
CREATE INDEX IF NOT EXISTS idx_bill_groups_desc       ON groups.bill_groups(description);