package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        // director that was just entered was already in the db, if director already exists, update it instead of creating new
        if(directorRepository.findDirectorByNameIs(director.getName()) == null) {
            // no director exists in the db with that name, so save it
            directorRepository.save(director);

        }
        else {
            // that director already exists, so update the records instead of creating a new one
            // do this by setting the id to be same as existing records id, you might do this if you wanted to change
            // the directors genre, if you change the
            Director d = directorRepository.findDirectorByNameIs(director.getName());
            director.setId(d.getId());
            directorRepository.save(director);

        }





        // need to update all the Movies in movie repo so that they match to the currently added director, because it is
        // possible that some movies were added BEFORE their director was added, and this way the relationships in the
        // movie repo will always be up to date... seems like their should be an automagic way to do this....
        // !!! NOTE: above is wrong, this is not something that should be expected in this setup.. a MOVIE that is added
        // BEFORE it's director has been added will become an ORPHAN, and so it will not ever have a value in it's JOIN column,
        // this is because a movie depends on a director, not the other way around
//        Iterable<Movie> matchingMovies = movieRepository.findAllByDirectorFormInputIs(director.getName());
//        for (Movie m : matchingMovies) {
//            m.setDirector(director);
//        }


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
            // add a new Director back to the model, because the user must have clicked on Submit for a new movie to get here
            model.addAttribute("newDirector", new Director());

            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("movies", movieRepository.findAll());
            return "index";
        }

        // returns null if director is not already in the db, which is ok... maybe? it doesn't crash at least
        // if a movie is persisted with a NULL director, we say it is 'orphaned'
        // really, need to display an errror message to user saying that the movie they are adding does not have a recognized
        // director, so either prompt them to add one, or tell them the director will be shown as unknown\
        Director d = directorRepository.findDirectorByNameIs(movie.getDirectorFormInput());

        if(d == null) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! user tried to enter a movie with an UNKNOWN director !!!!!!!!!!!!!!!!!");

//            model.addAttribute("unknownDirectorMsg", "")
//
//            // pass the same movie back to the model so user can try again with a movie with a known director, hopefully
//            model.addAttribute("newMovie", movie);
//            // add a new Director back to the model, because the user must have clicked on Submit for a new movie to get here
//            model.addAttribute("newDirector", new Director());
//
//            // index has lists of everything, so they must be added every time index is returned
//            model.addAttribute("directors", directorRepository.findAll());
//            model.addAttribute("movies", movieRepository.findAll());

            // user tried to enter a movie with a director that does not currently exist in the Director table
            return "unknowndirector";
        }
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! user entered a movie with a KNOWN director !!!!!!!!!!!!!!!!!");

            // now persist the movie, since it now has a Director associated with it
            movie.setDirector(d);
        }


        // save the new movie to the db, because there were no validation errors, and the director was known
        movieRepository.save(movie);


        return "addmovieconfirmation";
    }


    @GetMapping("/viewmoviedetails/{id}")
    public String viewMovieDetails(@PathVariable("id") long id, Model model) {


        model.addAttribute("movie", movieRepository.findMovieByIdIs(id));

        return "viewmoviedetails";
    }


    @GetMapping("/viewdirectordetails/{id}")
    public String viewDirectorDetails(@PathVariable("id") long id, Model model) {


        model.addAttribute("director", directorRepository.findDirectorByIdIs(id));

        return "viewdirectordetails";
    }


    // directorRequestParam coming from index as normal old URL embedded param
    // movieSearchObject coming from index as thymleaf 'request param'.. should not show in URL, but still can grab like req param
    // it works both ways, but it's not hidden when using th:name, so not sure what the point is right now....
    @GetMapping("/search")
    public String search(@RequestParam("by") String repo,
                         @RequestParam(value = "directorRequestParam", required = false) String directorSearchString,
                         @RequestParam(value = "movieSearchObject", required = false) String movieSearchString,
                         Model model) {

        switch (repo) {
            case "director" :
                Director d = directorRepository.findDirectorByNameIs(directorSearchString);
                if(d == null) {
                    // no hits in the repo, so show an appropriate msg
                    System.out.println("!!!!!!!!!!!!!!!!!!!!! nothing found in director repo");
                }
                else {
                    model.addAttribute(d);
                    return "viewdirectordetails";
                }
                break;

            case "movie" :
                Movie m = movieRepository.findMovieByTitleIs(movieSearchString);
                if (m == null) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!! nothing found in movie repo");
                }
                else {
                    model.addAttribute(m);
                    return "viewmoviedetails";
                }

        }


        // needed to compile, better to have a meaninful error message page here....
        return "redirect:/";
    }

}
