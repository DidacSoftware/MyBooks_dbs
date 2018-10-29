package com.didacsoftware.mybooks.Model;

public class model {

    // deben ser iguales a los de la bd
    private String author, description,publication_date, title, url_image;

    public model(String author, String description, String publication_date, String title, String url_image) {
        this.author = author;
        this.description = description;
        this.publication_date = publication_date;
        this.title = title;
        this.url_image = url_image;
    }

    // necesario este constructor por defecto
    public model(){

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    @Override
    public String toString() {
        return "model{" +
                "author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", publication_date='" + publication_date + '\'' +
                ", title='" + title + '\'' +
                ", url_image='" + url_image + '\'' +
                '}';
    }
}
