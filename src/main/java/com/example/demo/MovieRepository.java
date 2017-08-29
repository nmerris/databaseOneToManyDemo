package com.example.demo;


import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {


    public Iterable<Movie> findAllByDirectorFormInputIs(String directorName);

}
