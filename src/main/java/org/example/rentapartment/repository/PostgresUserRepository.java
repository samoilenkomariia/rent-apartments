package org.example.rentapartment.repository;

import org.example.rentapartment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class PostgresUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PostgresUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final RowMapper<User> ROW_MAPPER =
            new BeanPropertyRowMapper<>(User.class);

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, ROW_MAPPER);
    }

    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id=?";

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static final String UPDATE_SQL = "UPDATE users SET name=?, email=? WHERE id=?";

    private int update(User user) {
        return jdbcTemplate.update(UPDATE_SQL, user.getName(), user.getEmail(), user.getId());
    }

    private static final String INSERT_SQL = "INSERT INTO users (name, email, password) VALUES (?,?,?)";

    private User create(User user) {
        PreparedStatementCreatorFactory pscf =  new PreparedStatementCreatorFactory(INSERT_SQL,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
        pscf.setGeneratedKeysColumnNames("id");
        PreparedStatementCreator preparedStatementCreator = pscf.newPreparedStatementCreator(
                new Object[] {user.getName(), user.getEmail(), user.getPassword()}
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        Long newId = keyHolder.getKey().longValue();
        return new User(newId, user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public User save(User user) {
        if (update(user) == 1) return user;
        return create(user);
    }

    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id=?";

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }
}
