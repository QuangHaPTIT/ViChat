CREATE TABLE IF NOT EXISTS otp_code (
            user_id VARCHAR(255) PRIMARY KEY,
            verify_code VARCHAR(10) NOT NULL,            -- Mã xác thực
            expires_at DATETIME NOT NULL,
            created_at DATETIME
);
ALTER TABLE otp_code add constraint fk_otp_code_user_id foreign key(user_id) REFERENCES users(id);