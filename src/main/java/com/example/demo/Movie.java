package com.example.demo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    private String title;

    @Min(1900)
    private long year;

    @NotEmpty
    private String description;

    @NotEmpty
    private String directorFormInput;

    // many movies may have one director

    @ManyToOne(fetch = FetchType.EAGER)
    // director_id is just the name we are choosing to call the JOIN column, you can name it anything you want, but
    // [entity-name]_id is convention
    @JoinColumn(name = "director_id")
    private Director director;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirectorFormInput() {
        return directorFormInput;
    }

    public void setDirectorFormInput(String directorFormInput) {
        this.directorFormInput = directorFormInput;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }



}
