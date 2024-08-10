CREATE TABLE IF NOT EXISTS refresh_tokens
(
    user_id       VARCHAR(255) PRIMARY KEY,
    refresh_token VARCHAR(350) NOT NULL UNIQUE,
    expires_at    DATETIME     NOT NULL,
    created_at    DATETIME
);
ALTER TABLE refresh_tokens add constraint fk_refresh_tokens_user_id foreign key(user_id) REFERENCES users(id);