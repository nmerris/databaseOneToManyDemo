package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    @Autowired DirectorRepository directorRepository;


    @RequestMapping("/")
    public String index(Model model) {
        // first create a director
        Director director = new Director();
        director.setName("Rob Reiner");
        director.setGenre("Fantasy");

        // create a movie
        Movie movie = new Movie();
        movie.setTitle("Princess Bride");
        movie.setYear(1986);
        movie.setDescription("a comedic tale of love and adventure");

        // add movie to empty list
        Set<Movie> movies = new HashSet<Movie>();
        movies.add(movie);

        // create another movie
        movie = new Movie();
        movie.setTitle("Spinal Tap");
        movie.setYear(1992);
        movie.setDescription("a funny movie about a band");

        movies.add(movie);

        // add movies to director
        director.setMovies(movies);

        // save director to repo
        directorRepository.save(director);

        // add all directors to the model
        model.addAttribute("directors", directorRepository.findAll());

        return "index";

    }


}
