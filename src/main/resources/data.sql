--INSERT INTO PUBLIC.USERS
--(EMAIL, LOGIN, NAME, BIRTHDAY)
--VALUES('piwen.roman@yandex.ru', 'PiFFFko', 'Roman', '1998-01-13');

MERGE INTO genres(genre_id, genre_name)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

MERGE INTO RATINGS(rating_id, rating_name)
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');
