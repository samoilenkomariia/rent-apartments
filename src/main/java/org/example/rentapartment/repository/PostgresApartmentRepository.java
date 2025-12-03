package org.example.rentapartment.repository;

import org.example.rentapartment.model.User;
import org.example.rentapartment.model.apartment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class PostgresApartmentRepository implements ApartmentRepository {
    private final JdbcClient jdbcClient;
    private final UserRepository userRepository;

    @Autowired
    public PostgresApartmentRepository(JdbcClient jdbcClient, UserRepository userRepository) {
        this.jdbcClient = jdbcClient;
        this.userRepository = userRepository;
    }

    private final RowMapper<Apartment> ROW_MAPPER = (rs, rowNum) -> {
        User landlord = new User(
                rs.getLong("u_id"),
                rs.getString("u_name"),
                rs.getString("u_email"),
                rs.getString("u_password")
        );

        Address address = new Address(
                rs.getString("country"), rs.getString("region"), rs.getString("city"),
                rs.getString("district"), rs.getString("street"),
                rs.getString("building_number"), rs.getString("apartment_number")
        );

        ApartmentParameters params = new ApartmentParameters(
                rs.getObject("total_area", Double.class),
                rs.getObject("living_area", Double.class),
                rs.getObject("floor", Integer.class),
                rs.getObject("total_floors", Integer.class),
                rs.getObject("room_count", Integer.class),
                rs.getObject("build_year", Integer.class)
        );
        ApartmentDescription desc = new ApartmentDescription(rs.getString("description_text"));
        return new Apartment(
                rs.getLong("id"), rs.getBigDecimal("price"), address, params, desc, landlord
        );
    };

    private final String FIND_ALL_SQL = """
        SELECT a.*, u.id AS u_id, u.name AS u_name, u.email AS u_email, u.password AS u_password
        FROM apartment a
        JOIN users u ON a.landlord_id = u.id
    """;
    @Override
    public Collection<Apartment> findAll() {
        return jdbcClient.sql(FIND_ALL_SQL)
                .query(ROW_MAPPER)
                .list();
    }

    @Override
    public Optional<Apartment> findById(Long id) {
        return jdbcClient.sql(FIND_ALL_SQL + "WHERE a.id = ?")
                .param(id)
                .query(ROW_MAPPER)
                .optional();
    }

    private static final String UPDATE_SQL = """
            UPDATE apartment SET
                price = :price,
                country = :country, region = :region, city = :city,
                district = :district, street = :street,
                building_number = :b_num, apartment_number = :apt_num,
                total_area = :area, living_area = :living,
                floor = :floor, total_floors = :total_floors,
                room_count = :rooms, build_year = :year,
                description_text = :desc, landlord_id = :l_id
            WHERE id = :id
            """;

    private int update(Apartment apt) {
        if (apt.getId() == null) {
            return 0;
        }
        return jdbcClient.sql(UPDATE_SQL)
                .param("id", apt.getId())
                .param("price", apt.getPrice())
                .param("country", apt.getAddress().getCountry())
                .param("region", apt.getAddress().getRegion())
                .param("city", apt.getAddress().getCity())
                .param("district", apt.getAddress().getDistrict())
                .param("street", apt.getAddress().getStreet())
                .param("b_num", apt.getAddress().getBuildingNumber())
                .param("apt_num", apt.getAddress().getApartmentNumber())
                .param("area", apt.getParameters().getTotalArea())
                .param("living", apt.getParameters().getLivingArea())
                .param("floor", apt.getParameters().getFloor())
                .param("total_floors", apt.getParameters().getTotalFloors())
                .param("rooms", apt.getParameters().getRoomCount())
                .param("year", apt.getParameters().getBuildYear())
                .param("desc", apt.getDescription().getText())
                .param("l_id", apt.getLandlord().getId())
                .update();
    }

    private static final String INSERT_SQL = """
            INSERT INTO apartment (
                price, country, region, city, district, street,
                building_number, apartment_number,
                total_area, living_area, floor, total_floors,
                room_count, build_year, description_text, landlord_id
            ) VALUES (
                :price, :country, :region, :city, :district, :street,
                :b_num, :apt_num,
                :area, :living, :floor, :total_floors,
                :rooms, :year, :desc, :l_id
            ) RETURNING id
            """;

    private Apartment create(Apartment apt) {
        Long newId = jdbcClient.sql(INSERT_SQL)
                .param("price", apt.getPrice())
                .param("country", apt.getAddress().getCountry())
                .param("region", apt.getAddress().getRegion())
                .param("city", apt.getAddress().getCity())
                .param("district", apt.getAddress().getDistrict())
                .param("street", apt.getAddress().getStreet())
                .param("b_num", apt.getAddress().getBuildingNumber())
                .param("apt_num", apt.getAddress().getApartmentNumber())
                .param("area", apt.getParameters().getTotalArea())
                .param("living", apt.getParameters().getLivingArea())
                .param("floor", apt.getParameters().getFloor())
                .param("total_floors", apt.getParameters().getTotalFloors())
                .param("rooms", apt.getParameters().getRoomCount())
                .param("year", apt.getParameters().getBuildYear())
                .param("desc", apt.getDescription().getText())
                .param("l_id", apt.getLandlord().getId())
                .query(Long.class)
                .single();

        apt.setId(newId);
        return apt;
    }
    @Override
    public Apartment save(Apartment apartment) {
        if (update(apartment) == 1) return apartment;
        return create(apartment);
    }

    private static final String DELETE_BY_ID_SQL = "DELETE FROM apartment WHERE id=?";
    @Override
    public void deleteById(Long id) {
        jdbcClient.sql(DELETE_BY_ID_SQL)
                .param(id)
                .update();
    }

    @Override
    public Collection<Apartment> findByFilters(ApartmentSearchDTO dto) {
        StringBuilder sql = new StringBuilder(FIND_ALL_SQL);
        sql.append(" WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (dto.getCity() != null) {
            sql.append(" AND a.city = ?");
            params.add(dto.getCity());
        }
        if (dto.getRoomCount() != null) {
            sql.append(" AND a.room_count = ?");
            params.add(dto.getRoomCount());
        }

        return jdbcClient.sql(sql.toString())
                .params(params)
                .query(ROW_MAPPER)
                .list();
    }
}
