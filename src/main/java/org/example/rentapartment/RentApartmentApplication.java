package org.example.rentapartment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition (
        info = @Info(
                title = "Rent Apartments",
                description = "RESTful service for Renting Apartments by Samoilenko Mariia IM-32"
        ),
        servers = @Server(url = "http://localhost:8080", description = "local server")
)

@SpringBootApplication
public class RentApartmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentApartmentApplication.class, args);
    }

}
