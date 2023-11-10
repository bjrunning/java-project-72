DROP TABLE IF EXISTS urls;

create table urls (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP
);

DROP TABLE IF EXISTS url_checks;

create table url_checks (
                            id SERIAL PRIMARY KEY,
                            status_code INT NOT NULL,
                            title varchar(255),
                            h1 varchar(255),
                            description text,
                            url_id INT,
                            created_at TIMESTAMP
);