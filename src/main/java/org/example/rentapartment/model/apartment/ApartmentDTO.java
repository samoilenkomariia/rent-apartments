package org.example.rentapartment.model.apartment;

import java.math.BigDecimal;

public class ApartmentDTO {
    private Long id;
    private BigDecimal price;
    private Address address;
    private ApartmentParameters parameters;
    private ApartmentDescription description;
    private Long landlordId;

    public ApartmentDTO() {}

    public ApartmentDTO(Long id, BigDecimal price, Address address, ApartmentParameters parameters, ApartmentDescription description, Long landlordId) {
        this.id = id;
        this.price = price;
        this.address = address;
        this.parameters = parameters;
        this.description = description;
        this.landlordId = landlordId;
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

    public Long getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(Long landlordId) {
        this.landlordId = landlordId;
    }
}
