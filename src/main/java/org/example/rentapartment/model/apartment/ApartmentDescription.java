package org.example.rentapartment.model.apartment;

import java.time.LocalDate;

public class ApartmentDescription {
    private LocalDate lastUpdated;
    private String text;

    public ApartmentDescription() {}

    public ApartmentDescription(String text) {
        this.text = text;
        this.lastUpdated = LocalDate.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }
}
