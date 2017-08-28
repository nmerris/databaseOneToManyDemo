package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    @Autowired DirectorRepository directorRepository;
    @Autowired MovieRepository movieRepository;


    @RequestMapping("/")
    public String index(Model model) {
        System.out.println("*************************** default route RequestMapping");


        // first create a director
        Director director = new Director();
        Movie movie = new Movie();

        model.addAttribute("newDirector", director);
        model.addAttribute("newMovie", movie);












        //        Set<Movie> movies = new HashSet<Movie>();
//
//        director.setName("Rob Reiner");
//        director.setGenre("Fantasy");
//
//        // create a movie
//        Movie movieRR = new Movie();
//        movieRR.setTitle("Princess Bride");
//        movieRR.setYear(1986);
//        movieRR.setDescription("a comedic tale of love and adventure");
//        movies.add(movieRR);
//
//        // create another movie
//        movieRR = new Movie();
//        movieRR.setTitle("Spinal Tap");
//        movieRR.setYear(1992);
//        movieRR.setDescription("a funny movie about a band");
//        movies.add(movieRR);
//
//        // add movies to director
//        director.setMovies(movies);
//
//        // save director to repo
//        directorRepository.save(director);
//
//
//
//        // add all directors to the model
//        model.addAttribute("directors", directorRepository.findAll());

        return "index";

    }


    @PostMapping("/adddirector")
    public String adddirectorPost(@Valid @ModelAttribute("newDirector") Director director,
                                  BindingResult bindingResult, Model model) {
        System.out.println("*************************** adddirector POST");

        if(bindingResult.hasErrors()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bindingResult had ERRORS !!!!!!!!!!!!!!!!!");
            // add the incoming (and invalid) Director back to the model
            model.addAttribute("newDirector", director);
            // add a new Movie back to the model, because the user must have clicked on Submit for a new director to get here
            model.addAttribute("newMovie", new Movie());
            return "index";
        }

        // save the new director to the db, because there were no validation errors
        directorRepository.save(director);


        return "adddirectorconfirmation";
    }


    @PostMapping("/admovie")
    public String addmoviePost(@Valid @ModelAttribute("newMovie") Movie movie,
                                  BindingResult bindingResult, Model model) {
        System.out.println("*************************** addmovie POST");

        if(bindingResult.hasErrors()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bindingResult had ERRORS !!!!!!!!!!!!!!!!!");
            // add the incoming (and invalid) Movie back to the model
            model.addAttribute("newMovie", movie);
            // add a new Movie back to the model, because the user must have clicked on Submit for a new director to get here
            model.addAttribute("newDirector", new Director());
            return "index";
        }

        // save the new movie to the db, because there were no validation errors
        directorRepository.save(movie);


        return "addmovieconfirmation";
    }


}
