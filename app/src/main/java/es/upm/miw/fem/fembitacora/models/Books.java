package es.upm.miw.fem.fembitacora.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Books {
        @SerializedName("results")
        @Expose
        private List<BookResult> books;

    public Books(List<BookResult> books) {
        this.books = books;
    }

    public List<BookResult> getBooks() {
        return books;
    }

    public void setBooks(List<BookResult> books) {
        this.books = books;
    }
}
