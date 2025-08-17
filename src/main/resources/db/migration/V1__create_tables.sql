CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE locations (
    location_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    CONSTRAINT fk_locations_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE sessions (
    session_id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users(user_id)
)
