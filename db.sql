CREATE TABLE users(
    id integer NOT NULL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password TEXT,
    salt TEXT,
    created_at timestamp,
    updated_at timestamp,
    email VARCHAR(255) UNIQUE
);

CREATE TABLE books(
    id integer NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    author VARCHAR(255),
    publisher VARCHAR(255),
    book_language VARCHAR(255),
    issue_date date,
    created_at timestamp,
    updated_at timestamp,
    CONSTRAINT unique_book UNIQUE(name, author, book_language)
);

CREATE TABLE taken_books(
    user_id integer REFERENCES users(id),
    book_id integer REFERENCES books(id),
    created_at timestamp,
    updated_at timestamp,
    return_date date DEFAULT current_date + '30 days'::interval,
    CONSTRAINT taken_book PRIMARY KEY (user_id, book_id)
);