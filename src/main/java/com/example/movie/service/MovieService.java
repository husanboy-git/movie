package com.example.movie.service;

import com.example.movie.data.entity.MovieEntity;
import com.example.movie.repository.MovieRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired private MovieRepository movieRepository;

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieEntity getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Movie not found"));
    }

    public MovieEntity saveMovie(MovieEntity movie) {
        return movieRepository.save(movie);
    }

    public Optional<MovieEntity> updateMovieImage(Long id, String imageUrl) {
        return movieRepository.findById(id).map( movie -> {
            movie.setImageUrl(imageUrl);
            return movieRepository.save(movie);
        });
    }

    public Optional<MovieEntity> updateMovieVideo(Long id, String videoUrl) {
        return movieRepository.findById(id).map( movie -> {
            movie.setVideoUrl(videoUrl);
            return movieRepository.save(movie);
        });
    }

    public void deleteMovieById(Long id) {
        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("movie not found"));
        movieRepository.delete(movieEntity);
    }

    public void deleteAllMovies() {
        movieRepository.deleteAll();
    }
}
