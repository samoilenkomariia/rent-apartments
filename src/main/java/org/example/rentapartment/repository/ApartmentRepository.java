package org.example.rentapartment.repository;

import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentSearchDTO;

import java.util.Collection;
import java.util.Optional;

public interface ApartmentRepository {

    Apartment save(Apartment apartment);
    Optional<Apartment> findById(Long id);
    Collection<Apartment> findAll();
    void deleteById(Long id);
    Collection<Apartment> findByFilters(ApartmentSearchDTO apartmentSearchDTO);
}
