package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface DirectorRepository extends CrudRepository<Director, Long> {

    // need this so that, when you are about to persist a movie in MainController /addmovie post route, you can
    // easily search this repo for the director that the user just entered in the form when they were adding a new movie
    // because you need to 'attach' the entire Director object to the new movie before you save it.  this is how the
    // director_id column will know which director ID to use
    Director findDirectorByNameIs(String name);

    Director findDirectorByIdIs(long id);

//    Iterable<Director> findDirectorsByNameLike(String name);

}
