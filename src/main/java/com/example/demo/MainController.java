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
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("movies", movieRepository.findAll());











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

            // index also needs all the repo contents because many many things happen in index!
            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("movies", movieRepository.findAll());
            return "index";
        }




        // save the new director to the db, because there were no validation errors, but first check to see if the
        // director that was just entered was already in the db

        if(directorRepository.findDirectorByNameIs(director.getName()) == null) {
            // no director exists in the db with that name, so save it
            directorRepository.save(director);

        }
        else {
            // that director already exists, so update the records instead of creating a new one
            // do this by setting the id to be same as existing records id
            Director d = directorRepository.findDirectorByNameIs(director.getName());
            director.setId(d.getId());
            directorRepository.save(director);

        }





        // need to update all the Movies in movie repo so that they match to the currently added director, because it is
        // possible that some movies were added BEFORE their director was added, and this way the relationships in the
        // movie repo will always be up to date... seems like their should be an automagic way to do this....
        Iterable<Movie> matchingMovies = movieRepository.findAllByDirectorFormInputIs(director.getName());
        for (Movie m : matchingMovies) {
            m.setDirector(director);
        }


        return "adddirectorconfirmation";
    }


    @PostMapping("/addmovie")
    public String addmoviePost(@Valid @ModelAttribute("newMovie") Movie movie,
                                  BindingResult bindingResult, Model model) {
        System.out.println("*************************** addmovie POST");

        if(bindingResult.hasErrors()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bindingResult had ERRORS !!!!!!!!!!!!!!!!!");
            // add the incoming (and invalid) Movie back to the model
            model.addAttribute("newMovie", movie);
            // add a new Movie back to the model, because the user must have clicked on Submit for a new director to get here
            model.addAttribute("newDirector", new Director());

            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("movies", movieRepository.findAll());
            return "index";
        }

        // returns null if director is not already in the db, which is ok... maybe? it doesn't crash at least
        Director d = directorRepository.findDirectorByNameIs(movie.getDirectorFormInput());



//        d.setName(movie.getDirectorFormInput());
        movie.setDirector(d);





        // save the new movie to the db, because there were no validation errors
        movieRepository.save(movie);


        return "addmovieconfirmation";
    }


}
