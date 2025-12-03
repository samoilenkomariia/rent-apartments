package org.example.rentapartment.repository;

import org.example.rentapartment.model.apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    // Query
    @Query("SELECT a FROM Apartment a WHERE a.address.city = :city")
    Collection<Apartment> findAllByCityCustom(String city);

    // Named query
    Collection<Apartment> findByPriceLessThan(BigDecimal price);

    // Auto
    Collection<Apartment> findByAddress_Region(String region);
    Optional<Apartment> findById(Long id);
    Apartment save(Apartment apartment);
    List<Apartment> findAll();
    void deleteById(Long id);
}
