package es.upm.miw.fem.fembitacora;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("pages")
    @Expose
    private String pages;
    @SerializedName("language")
    @Expose
    private String language;
}
