-- PUBLIC.GENRES definition

-- Drop table

-- DROP TABLE PUBLIC.GENRES;

CREATE TABLE IF NOT EXISTS GENRES (
	GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
	GENRE_NAME CHARACTER VARYING(50),
	CONSTRAINT GENRES_PK PRIMARY KEY (GENRE_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS  PRIMARY_KEY_7 ON PUBLIC.GENRES (GENRE_ID);


-- PUBLIC.RATINGS definition

-- Drop table

-- DROP TABLE PUBLIC.RATINGS;

CREATE TABLE IF NOT EXISTS RATINGS (
	RATING_ID INTEGER NOT NULL AUTO_INCREMENT,
	RATING_NAME CHARACTER VARYING NOT NULL,
	CONSTRAINT RATINGS_PK PRIMARY KEY (RATING_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS  PRIMARY_KEY_6 ON PUBLIC.RATINGS (RATING_ID);


-- PUBLIC.USERS definition

-- Drop table

-- DROP TABLE PUBLIC.USERS;

CREATE TABLE IF NOT EXISTS USERS (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(50) NOT NULL,
	LOGIN CHARACTER VARYING(20) NOT NULL,
	NAME CHARACTER VARYING(20),
	BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_4 ON PUBLIC.USERS (USER_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_610 ON PUBLIC.USERS (USER_ID);


-- PUBLIC.FILMS definition

-- Drop table

-- DROP TABLE PUBLIC.FILMS;

CREATE TABLE IF NOT EXISTS FILMS (
	FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
	DESCRIPTION CHARACTER VARYING(200),
	NAME CHARACTER VARYING(100) NOT NULL,
	RELEASE_DATE DATE,
	DURATION INTEGER,
	RATING_ID INTEGER,
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID),
	CONSTRAINT FILMS_FK FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.RATINGS(RATING_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS FILMS_FK_INDEX_3 ON PUBLIC.FILMS (RATING_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_3 ON PUBLIC.FILMS (FILM_ID);


-- PUBLIC.FRIEND_REQUESTS definition

-- Drop table

-- DROP TABLE PUBLIC.FRIEND_REQUESTS;

CREATE TABLE IF NOT EXISTS FRIEND_REQUESTS (
	USER_ID_FROM INTEGER NOT NULL,
	USER_ID_TO INTEGER NOT NULL,
	FRIEND_STATUS BOOLEAN NOT NULL DEFAULT false,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID_FROM,USER_ID_TO),
	CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID_FROM) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE,
	CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (USER_ID_TO) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS FRIENDS_FK_1_INDEX_7 ON PUBLIC.FRIEND_REQUESTS (USER_ID_TO);
CREATE INDEX IF NOT EXISTS FRIENDS_FK_INDEX_7 ON PUBLIC.FRIEND_REQUESTS (USER_ID_FROM);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_70 ON PUBLIC.FRIEND_REQUESTS (USER_ID_FROM,USER_ID_TO);


-- PUBLIC.LIKES definition

-- Drop table

-- DROP TABLE PUBLIC.LIKES;

CREATE TABLE IF NOT EXISTS LIKES (
	FILM_ID INTEGER NOT NULL,
	USER_ID INTEGER NOT NULL,
	CONSTRAINT LIKES_PK PRIMARY KEY (USER_ID,FILM_ID),
	CONSTRAINT LIKES_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT LIKES_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS LIKES_FK_1_INDEX_4 ON PUBLIC.LIKES (USER_ID);
CREATE INDEX IF NOT EXISTS LIKES_FK_INDEX_4 ON PUBLIC.LIKES (FILM_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_45 ON PUBLIC.LIKES (FILM_ID,USER_ID);


-- PUBLIC.FILM_CATEGORY definition

-- Drop table

-- DROP TABLE PUBLIC.FILM_CATEGORY;

CREATE TABLE IF NOT EXISTS FILM_CATEGORY (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_CATEGORY_PK PRIMARY KEY (GENRE_ID,FILM_ID),
	CONSTRAINT FILM_CATEGORY_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(GENRE_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT FILM_CATEGORY_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS FILM_CATEGORY_FK_1_INDEX_4 ON PUBLIC.FILM_CATEGORY (FILM_ID);
CREATE INDEX IF NOT EXISTS FILM_CATEGORY_FK_INDEX_4 ON PUBLIC.FILM_CATEGORY (GENRE_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_4E ON PUBLIC.FILM_CATEGORY (FILM_ID,GENRE_ID);