package org.example.rentapartment.repository;

import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentSearchDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeApartmentRepository implements ApartmentRepository {
    private final TreeMap<Long,Apartment> apartments = new TreeMap<>();

    @Override
    public Apartment save(Apartment apartment) {
        if (apartment.getId() == null) {
            long nextId = apartments.isEmpty() ? 1L : apartments.lastKey() + 1;
            apartment.setId(nextId);
        }
        apartments.put(apartment.getId(), apartment);
        return apartment;
    }

    @Override
    public Optional<Apartment> findById(Long id) {
        return Optional.ofNullable(apartments.get(id));
    }

    @Override
    public Collection<Apartment> findAll() {
        return apartments.values();
    }

    @Override
    public void deleteById(Long id) {
        apartments.remove(id);
    }

    @Override
    public Collection<Apartment> findByFilters(ApartmentSearchDTO dto) {
        return apartments.values().stream()
                .filter(apt -> dto.getCountry() == null || dto.getCountry().equalsIgnoreCase((apt.getAddress().getCountry())))
                .filter(apt -> dto.getRegion() == null || dto.getRegion().equalsIgnoreCase((apt.getAddress().getRegion())))
                .filter(apt -> dto.getCity() == null || dto.getCity().equalsIgnoreCase((apt.getAddress().getCity())))
                .filter(apt -> dto.getFloor() == null || dto.getFloor().equals(apt.getParameters().getFloor()))
                .filter(apt -> dto.getRoomCount() == null || dto.getRoomCount().equals(apt.getParameters().getRoomCount()))
                .filter(apt -> dto.getTotalArea() == null || dto.getTotalArea().equals(apt.getParameters().getTotalArea()))
                .toList();
    }

}
