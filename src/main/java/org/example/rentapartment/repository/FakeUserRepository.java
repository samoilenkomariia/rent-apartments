package org.example.rentapartment.repository;

import org.example.rentapartment.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeMap;

@Repository
public class FakeUserRepository implements UserRepository {
    private final TreeMap<Long,User> users = new TreeMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            long nextId = users.isEmpty() ? 1L : users.lastKey() + 1;
            user.setId(nextId);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }
}
