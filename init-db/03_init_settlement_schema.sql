CREATE SCHEMA IF NOT EXISTS settlement;

CREATE TABLE IF NOT EXISTS settlement.settlement_records (
                                                             id                  BIGSERIAL PRIMARY KEY,
                                                             group_id            BIGINT          NOT NULL,
                                                             group_name          VARCHAR(100)    NOT NULL,
    total_expenses      DECIMAL(15, 2)  NOT NULL,
    participant_count   INT             NOT NULL,
    transaction_count   INT             NOT NULL,
    settlement_json     JSONB,
    created_at       	TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at			TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_settlement_group_id       ON settlement.settlement_records(group_id);
CREATE INDEX IF NOT EXISTS idx_settlement_created_at  ON settlement.settlement_records(created_at);
CREATE INDEX IF NOT EXISTS idx_settlement_json_data ON settlement.settlement_records USING GIN (settlement_json);
