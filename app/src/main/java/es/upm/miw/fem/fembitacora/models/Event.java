package es.upm.miw.fem.fembitacora.models;

import java.io.Serializable;

public class Event implements Serializable {
    public String notes;

    public Event() {
    }

    public Event(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
