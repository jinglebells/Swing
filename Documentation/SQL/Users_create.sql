CREATE TABLE data.users (
    id        integer NOT NULL,
    username       varchar(200) NOT NULL,
    password         varchar(200) NOT NULL,
    first_name   varchar(200) NOT NULL,
    last_name        varchar(200) NOT NULL,
    email         varchar(200) NOT NULL,
    phone_number  varchar(200) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY(id),
    CONSTRAINT users_unique UNIQUE(username)
);

INSERT INTO data.users(
            id, username, password, first_name, last_name, email, phone_number)
    VALUES (1, 'fr', '2818895662401d02f1e19f44688360d7',  'francisco', 'rebelo', 'fr@fr.pt', '912149893');