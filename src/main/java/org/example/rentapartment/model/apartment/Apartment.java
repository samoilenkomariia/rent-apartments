package org.example.rentapartment.model.apartment;

import org.example.rentapartment.model.User;

import java.math.BigDecimal;

public class Apartment {
    private Long id;
    private BigDecimal price;
    private Address address;
    private ApartmentParameters parameters;
    private ApartmentDescription description;
    private User landLord;

    public Apartment() {}

    public Apartment(Long id, BigDecimal price, Address address, ApartmentParameters parameters, ApartmentDescription description, User landLord) {
        this.id = id;
        this.price = price;
        this.address = address;
        this.parameters = parameters;
        this.description = description;
        this.landLord = landLord;
    }

    public Apartment(BigDecimal price, Address address, ApartmentParameters parameters, ApartmentDescription description, User landLord) {
        this(null, price, address, parameters, description, landLord);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ApartmentParameters getParameters() {
        return parameters;
    }

    public void setParameters(ApartmentParameters parameters) {
        this.parameters = parameters;
    }

    public ApartmentDescription getDescription() {
        return description;
    }

    public void setDescription(ApartmentDescription description) {
        this.description = description;
    }

    public User getLandLord() {
        return landLord;
    }

    public void setLandLord(User landLord) {
        this.landLord = landLord;
    }
}
