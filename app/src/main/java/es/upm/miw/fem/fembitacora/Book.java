package es.upm.miw.fem.fembitacora;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    private String title;
    private String author;
    private String content;
    private String publisher;
    private String date;
    private String pages;
    private String languages;

    public Book(String title, String author, String content, String publisher,
                String date, String pages, String languages) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.publisher = publisher;
        this.date = date;
        this.pages = pages;
        this.languages = languages;
    }
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }


}
