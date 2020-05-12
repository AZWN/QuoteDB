-- Script to create all models
-- Create users table
CREATE TABLE QuoteDBUser (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- User fields
    userName VARCHAR (31) NOT NULL,
    password VARCHAR (60) NOT NULL,
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BOOLEAN NOT NULL,
);

CREATE UNIQUE INDEX uidx_QuoteDBUser_userName ON QuoteDBUser (userName);

-- Create labels table
CREATE TABLE Label (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- Owner reference
    owner bigint NOT NULL,
    FOREIGN KEY (owner) REFERENCES QuoteDBUser(id) ON DELETE CASCADE,
    -- table_name fields
    labelName VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL DEFAULT 'white',
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BOOLEAN NOT NULL,
);

CREATE INDEX idx_Label_labelName ON Label (labelName);

-- Create author table
CREATE TABLE Author (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- Owner reference
    owner bigint NOT NULL,
    FOREIGN KEY (owner) REFERENCES QuoteDBUser(id) ON DELETE CASCADE,
    -- Books fields
    biography TEXT,
    dateOfBirth date NOT NULL,
    firstName varchar(255) NOT NULL,
    initials varchar(255) NOT NULL,
    middleName varchar(255),
    lastName varchar(255) NOT NULL,
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BIT NOT NULL,
);

CREATE INDEX idx_Author_firstName ON Author (firstName);
CREATE INDEX idx_Author_lastName ON Author (lastName);

-- Create books table
CREATE TABLE Book (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- Owner reference
    owner bigint NOT NULL,
    FOREIGN KEY (owner) REFERENCES QuoteDBUser(id) ON DELETE CASCADE,
    -- Books fields
    author bigint,
    FOREIGN KEY (author) REFERENCES Author(id) ON DELETE CASCADE,
    title varchar(255) NOT NULL,
    publisher varchar(255) NOT NULL,
    publicationYear INT NOT NULL,
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BIT NOT NULL,
);

CREATE INDEX idx_Author_publisher ON Book (publisher);
CREATE INDEX idx_Author_title ON Book (title);

-- Create Quick quote table
CREATE TABLE QuickQuote (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- Owner reference
    owner bigint NOT NULL,
    FOREIGN KEY (owner) REFERENCES QuoteDBUser(id) ON DELETE CASCADE,
    -- Base Quote fields
    title VARCHAR (255) NOT NULL,
    text VARCHAR (8000) NOT NULL,
    note VARCHAR (2000),
    -- table_name fields
    ---- No additional table fields fro quick quote
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BIT NOT NULL,
);

CREATE INDEX idx_QuickQuote_text ON QuickQuote (text);
CREATE INDEX idx_QuickQuote_title ON QuickQuote (title);

-- Create QuickQuote_Label junction table
CREATE TABLE QuickQuote_Label(
    QuickQuoteId bigint NOT NULL,
    FOREIGN KEY (QuickQuoteId) REFERENCES QuickQuote(id) ON DELETE CASCADE,
    LabelId bigint NOT NULL,
    FOREIGN KEY (LabelId) REFERENCES Label(id) ON DELETE CASCADE,
    PRIMARY KEY (QuickQuoteId, LabelId)
);
-- Create Book quote table
CREATE TABLE BookQuote (
    -- Key
    id bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    -- Owner reference
    owner bigint NOT NULL,
    FOREIGN KEY (owner) REFERENCES QuoteDBUser(id) ON DELETE CASCADE,
    -- Base Quote fields
    title VARCHAR (255) NOT NULL,
    text VARCHAR (8000) NOT NULL,
    note VARCHAR (2000),
    -- BookQuote fields
    book bigint,
    FOREIGN KEY (book) REFERENCES Book(id) ON DELETE CASCADE,
    pageRange varchar(255),
    -- Metadata
    generationDate DATE NOT NULL,
    lastModifiedDate DATE NOT NULL,
    deleted BIT NOT NULL,
);

CREATE INDEX idx_BookQuote_text ON BookQuote (text);
CREATE INDEX idx_BookQuote_title ON BookQuote (title);

-- Create BookQuote_Label junction table
CREATE TABLE BookQuote_Label(
    BookQuoteId bigint NOT NULL,
    FOREIGN KEY (BookQuoteId) REFERENCES BookQuote(id) ON DELETE CASCADE,
    LabelId bigint NOT NULL,
    FOREIGN KEY (LabelId) REFERENCES Label(id) ON DELETE CASCADE,
    PRIMARY KEY (BookQuoteId, LabelId)
);