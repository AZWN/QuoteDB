CREATE INDEX author_firstName ON author(firstName);
CREATE INDEX author_lastName ON author(lastName);

CREATE INDEX book_title ON book(title);
CREATE INDEX book_publisher ON book(publisher);

CREATE INDEX bookquote_title ON bookquote(title);
CREATE INDEX quickquote_title ON quickquote(title);