package org.example.rentapartment.model.apartment;

import jakarta.persistence.*;
import org.example.rentapartment.model.User;

import java.math.BigDecimal;

@Entity
@Table(name = "apartment")
@NamedQuery(
        name = "Apartment.findByPriceLessThan",
        query = "SELECT a FROM Apartment a WHERE a.price < ?1"
)
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    @Embedded
    private Address address;

    @Embedded
    private ApartmentParameters parameters;

    @Embedded
    private ApartmentDescription description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id")
    private User landlord;

    public Apartment() {}

    public Apartment(Long id, BigDecimal price, Address address, ApartmentParameters parameters, ApartmentDescription description, User landlord) {
        this.id = id;
        this.price = price;
        this.address = address;
        this.parameters = parameters;
        this.description = description;
        this.landlord = landlord;
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

    public User getLandlord() {
        return landlord;
    }

    public void setLandlord(User landlord) {
        this.landlord = landlord;
    }
}
