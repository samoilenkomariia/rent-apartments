package org.example.rentapartment.repository;

import org.example.rentapartment.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Collection<User> findAll();
    void deleteById(Long id);
}
