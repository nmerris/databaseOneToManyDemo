package com.example.demo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String genre;

    // one Director may have many movies
    // will look for a field called director in another entity
    // CascadeType.ALL: basically will update everything in both tables at once, also if you delete a director, it
    // will delete all the movies associated with that director
    // FetchType.EAGER: will get ALL movies and ALSO ALL entities that are associated with each movie
    // so maybe a movie would also have 'stage hand' entity data associated with it, which will be available to a Director
    // as long as EAGER is used for FetchType
    // LAZY: would ONLY get the movies associated with this director, would NOT get any other entities data 'down the line'
    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Movie> movies;


    // so we can reuse the same hashset
    public Director() {
        setMovies(new HashSet<Movie>());
    }


    public void addMovie(Movie m) {
        m.setDirector(this);
        this.movies.add(m);
    }


    // overriding toString here allows us to simply output each movie's director as text in the html
    // like [movie-object].director  ... it just works!
    @Override
    public String toString() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
