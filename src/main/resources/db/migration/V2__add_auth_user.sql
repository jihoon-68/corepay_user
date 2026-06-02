CREATE TABLE auth_users (
                            id BIGINT PRIMARY KEY,
                            email VARCHAR(100) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
);

CREATE UNIQUE INDEX idx_auth_users_email ON auth_users(email);