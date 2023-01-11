DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name  VARCHAR(255)                            NOT NULL,
    user_email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (user_email),
    CONSTRAINT UQ_USER_NAME UNIQUE (user_name)
);

CREATE UNIQUE INDEX IF NOT EXISTS users_index ON users (id);

CREATE TABLE IF NOT EXISTS categories
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name VARCHAR(255) UNIQUE                          NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (category_name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    paid               BOOLEAN,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    participant_limit  INTEGER,
    confirmed_requests INTEGER,
    state              VARCHAR(55),
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    request_moderation BOOLEAN,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users,
    CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories
);

CREATE UNIQUE INDEX IF NOT EXISTS events_index ON events (id);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(120)                                 NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS compilations_index ON compilations (id);

CREATE TABLE IF NOT EXISTS events_compilations
(
    compilations_id BIGINT,
    events_id       BIGINT,
    CONSTRAINT pk_events_compilations PRIMARY KEY (events_id, compilations_id),
    CONSTRAINT fk_compilations FOREIGN KEY (events_id) REFERENCES events (id),
    CONSTRAINT fk_events FOREIGN KEY (compilations_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id  BIGINT                                  NOT NULL,
    requester BIGINT                                  NOT NULL,
    status    VARCHAR(55),
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_events FOREIGN KEY (event_id) REFERENCES events,
    CONSTRAINT fk_requests_users FOREIGN KEY (requester) REFERENCES users,
    CONSTRAINT UQ_PARTICIPANT_PER_EVENT UNIQUE (requester, event_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS requests_index ON requests (id);