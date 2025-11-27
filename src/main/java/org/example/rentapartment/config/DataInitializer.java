package org.example.rentapartment.config;

import org.example.rentapartment.model.Role;
import org.example.rentapartment.model.User;
import org.example.rentapartment.model.apartment.Address;
import org.example.rentapartment.model.apartment.ApartmentDTO;
import org.example.rentapartment.model.apartment.ApartmentDescription;
import org.example.rentapartment.model.apartment.ApartmentParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.example.rentapartment.repository.UserRepository;
import org.example.rentapartment.service.ApartmentService;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private ApartmentService apartmentService;

    @Autowired
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @Override
    public void run(String... args) throws Exception {
        User landlord = new User(1L, "Vania Landlord", "ivan@example.com", "password", Role.LANDLORD);
        userRepository.save(landlord);
        Address addr = new Address("Ukraine", "Kyiv City", "Kyiv", "Shevchenkivskyi", "Khreshchatyk", "12/A");
        ApartmentParameters params = new ApartmentParameters(55.0, 30.0, 3, 5, 2, 2000);
        ApartmentDescription desc = new ApartmentDescription("Great view");
        ApartmentDTO dto = new ApartmentDTO(null, new BigDecimal("15000"), addr, params, desc, 1L);
        apartmentService.create(dto);
    }
}