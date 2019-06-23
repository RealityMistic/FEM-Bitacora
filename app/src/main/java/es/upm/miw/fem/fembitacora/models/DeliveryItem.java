package es.upm.miw.fem.fembitacora.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class DeliveryItem implements Serializable {

    public String title;

    public String author;

    public String address;

    public ArrayList<Event> events = new ArrayList<>();

    public String location;

    public String id;

    public boolean done = Boolean.FALSE;

    public DeliveryItem(String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;

    }

    public DeliveryItem(String id, String title, String address, ArrayList<Event> events, String location, boolean done) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.events = events;
        this.location = location;
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
