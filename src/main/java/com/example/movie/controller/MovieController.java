package com.example.movie.controller;

import com.example.movie.data.entity.MovieEntity;
import com.example.movie.service.CloudinaryService;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired private MovieService movieService;
    @Autowired private CloudinaryService cloudinaryService;

    @GetMapping
    public List<MovieEntity> getAllMovies(){
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieEntity getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<MovieEntity> createMovie(@RequestBody MovieEntity movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

   @PostMapping("/{id}/uploadImage")
   public ResponseEntity<String> updateMovieImage(
           @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            movieService.updateMovieImage(id, imageUrl);
            return ResponseEntity.ok("Image uploaded successfully. URL: " + imageUrl);
        } catch (IOException e){
            return ResponseEntity.status(500).body("Image uploaded failed: " + e.getMessage());
        }
   }

    @PostMapping("/{id}/uploadVideo")
    public ResponseEntity<String> updateMovieVideo(
            @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String videoUrl = cloudinaryService.uploadFile(file);
            movieService.updateMovieVideo(id, videoUrl);
            return ResponseEntity.ok("Video uploaded successfully. URL: " + videoUrl);
        } catch (IOException e){
            return ResponseEntity.status(500).body("Video uploaded failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteMovieById(@PathVariable Long id) {
        movieService.deleteMovieById(id);
    }

    @DeleteMapping("/deleteAll")
    public String deleteAllMovies() {
        movieService.deleteAllMovies();
        return "Xamma kinolar muvaffaqiyatli o'chirildi!!!";
    }
}
