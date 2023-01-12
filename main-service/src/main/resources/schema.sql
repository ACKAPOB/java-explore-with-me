DROP TABLE IF EXISTS users,categories,locations,events,compilations,event_compilation,requests,comments;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat float not null,
    lon float not null
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(1024) NOT NULL,
    category_id        BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    description        VARCHAR(1024) NOT NULL,
    create_on          TIMESTAMP WITHOUT TIME ZONE,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    location_id        BIGINT REFERENCES locations (id) ON DELETE CASCADE,
    initiator_id       BIGINT REFERENCES users (id) ON DELETE CASCADE,
    paid               BOOLEAN,
    participant_limit  INTEGER,
    request_moderation BOOLEAN,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    title              VARCHAR(1024) NOT NULL,
    state              VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title  VARCHAR(512)
);

CREATE TABLE IF NOT EXISTS event_compilation
(
    event_id       BIGINT REFERENCES events (id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status       VARCHAR(32)
);
