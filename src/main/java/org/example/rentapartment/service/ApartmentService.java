package org.example.rentapartment.service;

import org.example.rentapartment.model.User;
import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentDTO;
import org.example.rentapartment.model.apartment.ApartmentSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.rentapartment.repository.ApartmentRepository;
import org.example.rentapartment.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository,  UserRepository userRepository) {
        this.apartmentRepository = apartmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Apartment create(ApartmentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Apartment cannot be null");
        }
        User landLord = userRepository.findById(dto.getLandlordId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Apartment apartment = new Apartment(dto.getPrice(), dto.getAddress(), dto.getParameters(), dto.getDescription(), landLord);

        return apartmentRepository.save(apartment);
    }

    @Transactional
    public Apartment update(Long id, ApartmentDTO dto) {
        Apartment existing = apartmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));
        if (!Objects.equals(id, dto.getId())) throw new IllegalArgumentException("Invalid apartment id");
        existing.setPrice(dto.getPrice());
        existing.setAddress(dto.getAddress());
        existing.setParameters(dto.getParameters());
        existing.setDescription(dto.getDescription());
        if (!existing.getLandlord().getId().equals(dto.getLandlordId())) {
            User newLandlord = userRepository.findById(dto.getLandlordId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existing.setLandlord(newLandlord);
        }
        return apartmentRepository.save(existing);
    }

    public Optional<Apartment> findById(Long id) {
        return apartmentRepository.findById(id);
    }

    public Collection<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    public void deleteById(Long id) {
        apartmentRepository.deleteById(id);
    }

    public Collection<Apartment> findByFilters(ApartmentSearchDTO apartmentSearchDTO) {
        if (apartmentSearchDTO == null) {
            return Collections.emptyList();
        }
        return apartmentRepository.findByFilters(apartmentSearchDTO);
    }
}
