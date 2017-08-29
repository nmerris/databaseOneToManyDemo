package com.example.demo;


import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {



//    Iterable<Movie> findAllByDirectorFormInputIs(String directorName);


    Movie findMovieByIdIs(long id);

    // it would be nice to search by partial string here, and display a list of movies user can select from, also ignoring case
    Movie findMovieByTitleIs(String title);

}
