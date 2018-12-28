CREATE TABLE customers(
    id SERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users(
    id SERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password TEXT,
    salt TEXT,
    email VARCHAR(255) UNIQUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books(
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    author VARCHAR(255),
    publisher VARCHAR(255),
    book_language VARCHAR(255),
    issue_date date,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_book UNIQUE(name, author, book_language)
);

CREATE TABLE taken_books(
    user_id integer REFERENCES users(id),
    book_id integer REFERENCES books(id),
    customer_id integer REFERENCES customers(id),
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    return_date date DEFAULT current_date + '30 days'::interval,
    CONSTRAINT taken_book PRIMARY KEY (customer_id, book_id)
);

CREATE OR REPLACE FUNCTION update_timestamp()	
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    return NEW;	
END ;$$ 
  LANGUAGE plpgsql;
  
CREATE TRIGGER update_user_timestamp BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE update_timestamp();
CREATE TRIGGER update_book_timestamp BEFORE UPDATE ON books FOR EACH ROW EXECUTE PROCEDURE update_timestamp();
CREATE TRIGGER update_taken_book_timestamp BEFORE UPDATE ON taken_books FOR EACH ROW EXECUTE PROCEDURE update_timestamp();
CREATE TRIGGER update_customers_timestamp BEFORE UPDATE ON customers FOR EACH ROW EXECUTE PROCEDURE update_timestamp();