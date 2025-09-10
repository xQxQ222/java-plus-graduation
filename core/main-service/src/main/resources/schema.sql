CREATE TABLE IF NOT EXISTS users
(
    user_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name  VARCHAR(250) NOT NULL,
    user_email VARCHAR(254) NOT NULL UNIQUE,
    CONSTRAINT user_email_len CHECK (LENGTH(user_email) >= 6 AND LENGTH(user_email) <= 254),
    CONSTRAINT user_name_len CHECK (LENGTH(user_name) >= 2 AND LENGTH(user_name) <= 250)
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT category_name_len CHECK (LENGTH(category_name) >= 1 AND LENGTH(category_name) <= 50)
);

CREATE TABLE IF NOT EXISTS locations
(
    event_id  BIGINT PRIMARY KEY,
    latitude  NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_title        VARCHAR(120)  NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    annotation         VARCHAR(2000) NOT NULL,
    initiator_id       BIGINT        NOT NULL,
    category_id        BIGINT        NOT NULL,
    state              INTEGER       NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INTEGER       NOT NULL,
    request_moderation BOOLEAN       NOT NULL,
    CONSTRAINT annotation_len CHECK (LENGTH(annotation) >= 20 AND LENGTH(annotation) <= 2000),
    CONSTRAINT description_len CHECK (LENGTH(description) >= 20 AND LENGTH(description) <= 7000),
    CONSTRAINT event_title_len CHECK (LENGTH(event_title) >= 3 AND LENGTH(event_title) <= 120),
    CONSTRAINT created_on_not_null CHECK (created_on <> NULL),
    CONSTRAINT event_date_not_null CHECK (event_date <> NULL)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT  NOT NULL,
    event_id     BIGINT  NOT NULL,
    status       INTEGER NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT created_not_null CHECK (created <> NULL)
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned            BOOLEAN     NULL,
    compilation_title VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT compilation_title_len CHECK (LENGTH(compilation_title) >= 1 AND LENGTH(compilation_title) <= 50)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       BIGINT,
    compilation_id BIGINT,
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    author_id       BIGINT NOT NULL,
    event_id        BIGINT NOT NULL,
    text            VARCHAR(5000) NOT NULL,
    created_date    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT text_len CHECK (LENGTH(text) >= 1 AND LENGTH(text) <= 5000),
    CONSTRAINT created_date_not_null CHECK (created_date <> NULL)
);

ALTER TABLE events
    DROP CONSTRAINT IF EXISTS fk_events_initiator_id;
ALTER TABLE events
    ADD CONSTRAINT fk_events_initiator_id FOREIGN KEY (initiator_id)
        REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE events
    DROP CONSTRAINT IF EXISTS fk_events_category_id;
ALTER TABLE events
    ADD CONSTRAINT fk_events_category_id FOREIGN KEY (category_id)
        REFERENCES categories (category_id);

ALTER TABLE locations
    DROP CONSTRAINT IF EXISTS fk_locations_event_id;
ALTER TABLE locations
    ADD CONSTRAINT fk_locations_event_id FOREIGN KEY (event_id)
        REFERENCES events (event_id) ON DELETE CASCADE;

ALTER TABLE requests
    DROP CONSTRAINT IF EXISTS fk_requests_requester_id;
ALTER TABLE requests
    ADD CONSTRAINT fk_requests_requester_id FOREIGN KEY (requester_id)
        REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE requests
    DROP CONSTRAINT IF EXISTS fk_requests_event_id;
ALTER TABLE requests
    ADD CONSTRAINT fk_requests_event_id FOREIGN KEY (event_id)
        REFERENCES events (event_id) ON DELETE CASCADE;

ALTER TABLE events_compilations
    DROP CONSTRAINT IF EXISTS fk_events_compilations_event_id;
ALTER TABLE events_compilations
    ADD CONSTRAINT fk_events_compilations_event_id FOREIGN KEY (event_id)
        REFERENCES events (event_id) ON DELETE CASCADE;

ALTER TABLE events_compilations
    DROP CONSTRAINT IF EXISTS fk_events_compilations_compilation_id;
ALTER TABLE events_compilations
    ADD CONSTRAINT fk_events_compilations_compilation_id FOREIGN KEY (compilation_id)
        REFERENCES compilations (compilation_id) ON DELETE CASCADE;

ALTER TABLE comments
    DROP CONSTRAINT IF EXISTS fk_comments_author_id;
ALTER TABLE comments
    ADD CONSTRAINT fk_comments_author_id FOREIGN KEY (author_id)
        REFERENCES  users (user_id) ON DELETE CASCADE;

ALTER TABLE comments
    DROP CONSTRAINT IF EXISTS fk_comments_event_id;
ALTER TABLE comments
    ADD CONSTRAINT fk_comments_event_id FOREIGN KEY (event_id)
        REFERENCES  events (event_id) ON DELETE CASCADE;