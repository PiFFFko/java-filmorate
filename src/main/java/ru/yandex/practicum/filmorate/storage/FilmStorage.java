package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public interface FilmStorage extends BaseStorage<Film> {
    Collection<Film> getPopular(Integer count);
    Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException;
    Set<Genre> getFilmGenresFromDB(int filmId);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Film makeFilmFromComplexTable(ResultSet resultSet, int rowNum) throws SQLException;
    Collection<Film> getPopularByGenre(int genreId, int count);
    Collection<Film> getPopularByYear(int year, int count);
    Collection<Film> getPopularByGenreAndYear(int genreId, int year, int count);
    Collection<Film> getDirectorsFilms(Integer directorId, String sortBy);
}
