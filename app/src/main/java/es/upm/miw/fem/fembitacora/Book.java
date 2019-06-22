package es.upm.miw.fem.fembitacora;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("pages")
    @Expose
    private String pages;
    @SerializedName("language")
    @Expose
    private String language;

    public Book(String title, String author, String publisher,
                String pages, String language) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titulo) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String autor) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


}
