package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.CityDAOException;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityDAOImpl implements ICityDAO {

    @Override
    public Optional<List<City>> getByCityName(String name) throws CityDAOException {
        String sql = "SELECT * FROM CITIES WHERE NAME LIKE ?";
        List<City> cities = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setString(1, name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                City city = new City(rs.getInt("ID"), rs.getString("NAME"));
                cities.add(city);
            }
        } catch (SQLException e) {
            throw new CityDAOException("SQL Error while retrieving cities by name: " + name, e);
        }

        if (cities.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(cities);
        }
    }

    @Override
    public City getById(int id) throws CityDAOException {
        String sql = "SELECT * FROM CITIES WHERE ID = ?";
        City city = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                city = new City(rs.getInt("ID"), rs.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new CityDAOException("SQL Error while retrieving city with ID: " + id,e);
        }
        return city;
    }

    @Override
    public City insert(City city) throws CityDAOException {
        String sql = "INSERT INTO CITIES (NAME) VALUES (?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String name = city.getName();

            ps.setString(1, name);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int generatedId = 0;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            city.setId(generatedId);
            generatedKeys.close();

        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new CityDAOException("SQL Error in City insertion",e);
        }
        return city;
    }

    @Override
    public City update(City city) throws CityDAOException {
        String sql = "UPDATE CITIES SET NAME = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            int id = city.getId();
            String name = city.getName();

            ps.setString(1, name);
            ps.setInt(2, id);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            return city;
        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new CityDAOException("SQL Error in City update",e);
        }
    }

    @Override
    public void delete(int id) throws CityDAOException {
        String sql = "DELETE FROM CITIES WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new CityDAOException("SQL Error in City deletion with ID: " + id,e);
        }
    }

    public List<City> getAllCities() throws CityDAOException {
        String sql = "SELECT * FROM CITIES";
        List<City> cities = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                City city = new City(rs.getInt("ID"), rs.getString("NAME"));
                cities.add(city);
            }

        } catch (SQLException e) {
            throw new CityDAOException("SQL Error while retrieving all cities", e);
        }

        System.out.println("Number of cities fetched: " + cities.size());
        return cities;
    }

//@Override
//public List<City> getAllCities() throws CityDAOException {
//   String sql = "SELECT * FROM CITIES";
//   // String sql = "SELECT * FROM CITIES ORDER BY NAME";
//    List<City> cities = new ArrayList<>();
//
//    try (Connection connection = DBUtil.getConnection();
//         PreparedStatement ps = connection.prepareStatement(sql);
//         ResultSet rs = ps.executeQuery()) {
//
//        while (rs.next()) {
//            City city = new City(rs.getInt("ID"), rs.getString("NAME"));
//            System.out.println(city); // Added this line
//            cities.add(city);
//        }
//
//        System.out.println(cities); // Added this line
//
//    } catch (SQLException e) {
//        e.printStackTrace(); // Added this line
//        throw new CityDAOException("SQL Error while retrieving all cities",e); // Modified this line
//    }
//
//    return cities;
//}


}
