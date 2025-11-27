package org.example.rentapartment.controller;

import org.example.rentapartment.model.apartment.Address;
import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentDTO;
import org.example.rentapartment.model.apartment.ApartmentParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.rentapartment.service.ApartmentService;

import java.util.Optional;

@Controller
@RequestMapping("/apartments")
public class ApartmentController {

    private ApartmentService apartmentService;
    private final String requestTracker;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, String requestTracker) {
        this.apartmentService = apartmentService;
        this.requestTracker = requestTracker;
    }

    @GetMapping
    public String listApartments(Model model) {
        model.addAttribute("apartments", apartmentService.findAll());
        model.addAttribute("tracker", requestTracker);
        return "apartments-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        ApartmentDTO dto = new ApartmentDTO();
        dto.setAddress(new Address());
        dto.setParameters(new ApartmentParameters());
        model.addAttribute("apartmentDTO", dto);
        return "apartment-create";
    }

    @GetMapping("/{id}")
    public String viewApartment(@PathVariable Long id, Model model) {
        Optional<Apartment> apt = apartmentService.findById(id);
        if (apt.isPresent()) {
            model.addAttribute("apartment", apt.get());
            return "apartment-details";
        } else {
            return "redirect:/apartments";
        }
    }

    @PostMapping("/create")
    public String createApartment(@ModelAttribute ApartmentDTO dto) {
        dto.setLandlordId(1L);
        apartmentService.create(dto);
        return "redirect:/apartments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        apartmentService.deleteById(id);
        return "redirect:/apartments";
    }
}