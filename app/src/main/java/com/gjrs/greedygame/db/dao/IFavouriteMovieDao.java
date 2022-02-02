package com.gjrs.greedygame.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.gjrs.greedygame.model.Movie;

@Dao
public interface IFavouriteMovieDao {

    @Query("SELECT * FROM favourite_movie")
    LiveData<List<Movie>> loadAll();

    @Insert
    void addToFavourites(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void removeFromFavourites(Movie movie);

    @Query("SELECT * FROM favourite_movie WHERE movie_id = :id")
    LiveData<Movie> loadById(int id);

    @Query("SELECT * FROM favourite_movie WHERE movie_id = :id AND movie_title = :title")
    Movie checkForFavs(int id, String title);

}
