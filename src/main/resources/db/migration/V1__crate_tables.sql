CREATE TABLE Users (
    ID BIGSERIAL PRIMARY KEY,
    Login VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL
);

  CREATE TABLE Locations (
    ID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    userId BIGINT NOT NULL,
    Latitude DECIMAL(9,6),
    Longitude DECIMAL(9,6),
    CONSTRAINT fk_locations_user FOREIGN KEY (userId) REFERENCES Users(id)
);

CREATE TABLE Sessions (
    ID VARCHAR(36) PRIMARY KEY,
    userId BIGINT NOT NULL,
    ExpiresAt TIMESTAMP,
    CONSTRAINT fk_sessions_user FOREIGN KEY (userId) REFERENCES Users(id)
)
