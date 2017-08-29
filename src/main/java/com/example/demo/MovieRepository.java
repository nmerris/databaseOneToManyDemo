package com.example.demo;


import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {



//    Iterable<Movie> findAllByDirectorFormInputIs(String directorName);


    Movie findMovieByIdIs(long id);

}
