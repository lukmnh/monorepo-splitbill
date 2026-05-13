CREATE TABLE IF NOT EXISTS expenses.expenses (
                                        id                      BIGSERIAL PRIMARY KEY,
                                        group_id                BIGINT          NOT NULL,
                                        title                   VARCHAR(150)    NOT NULL,
    description             VARCHAR(500),

    amount                  DECIMAL(15,2)
    NOT NULL
    CHECK (amount > 0),

    paid_by_participant_id  BIGINT          NOT NULL,
    paid_by_name            VARCHAR(100)    NOT NULL,

    category                VARCHAR(30)     NOT NULL,
    split_type              VARCHAR(20)     NOT NULL,

    expense_date            DATE            NOT NULL,

    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP       DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS expenses.expense_splits (
                                              id                  BIGSERIAL PRIMARY KEY,

                                              expense_id          BIGINT          NOT NULL,
                                              participant_id      BIGINT          NOT NULL,

                                              participant_name    VARCHAR(100)    NOT NULL,

    owed_amount         DECIMAL(15,2)
    NOT NULL
    CHECK (owed_amount >= 0),

    share_value         DECIMAL(15,4),

    CONSTRAINT fk_split_expense
    FOREIGN KEY (expense_id)
    REFERENCES expenses(id)
    ON DELETE CASCADE
    );



CREATE INDEX IF NOT EXISTS idx_expenses_group_id
    ON expenses.expenses(group_id);

CREATE INDEX IF NOT EXISTS idx_expenses_paid_by
    ON expenses.expenses(paid_by_participant_id);

CREATE INDEX IF NOT EXISTS idx_expenses_category
    ON expenses.expenses(category);

CREATE INDEX IF NOT EXISTS idx_expenses_date
    ON expenses.expenses(expense_date);

CREATE INDEX IF NOT EXISTS idx_splits_expense_id
    ON expenses.expense_splits(expense_id);

CREATE INDEX IF NOT EXISTS idx_splits_participant_id
    ON expenses.expense_splits(participant_id);
