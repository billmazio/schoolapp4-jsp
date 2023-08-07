package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.SpecialtyDAOException;
import gr.aueb.cf.schoolapp.model.Specialty;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialtyDAOImpl implements ISpecialtyDAO {

    @Override
    public Optional<List<Specialty>> getSpecialtyByName(String name) throws SpecialtyDAOException {
        String sql = "SELECT * FROM SPECIALTIES WHERE NAME LIKE ?";
        List<Specialty> specialties = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setString(1, name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Specialty specialty = new Specialty(rs.getInt("ID"), rs.getString("NAME"));
                specialties.add(specialty);
            }
        } catch (SQLException e) {
            throw new SpecialtyDAOException("SQL Error while retrieving specialties by name: " + name);
        }

        if (specialties.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(specialties);
        }
    }

    @Override
    public Specialty getById(int id) throws SpecialtyDAOException {
        String sql = "SELECT * FROM SPECIALTIES WHERE ID = ?";
        Specialty specialty = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                specialty = new Specialty(rs.getInt("ID"), rs.getString("NAME"));
            }
        } catch (SQLException e) {
            throw new SpecialtyDAOException("SQL Error while retrieving specialty with ID: " + id);
        }
        return specialty;
    }

    @Override
    public Specialty insert(Specialty specialty) throws SpecialtyDAOException {
        String sql = "INSERT INTO SPECIALTIES (NAME) VALUES (?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String name = specialty.getName();

            ps.setString(1, name);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int generatedId = 0;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            specialty.setId(generatedId);
            generatedKeys.close();

        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new SpecialtyDAOException("SQL Error in Specialty insertion");
        }
        return specialty;
    }

    @Override
    public Specialty update(Specialty specialty) throws SpecialtyDAOException {
        String sql = "UPDATE SPECIALTIES SET NAME = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            int id = specialty.getId();
            String name = specialty.getName();

            ps.setString(1, name);
            ps.setInt(2, id);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            return specialty;
        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new SpecialtyDAOException("SQL Error in Specialty update");
        }
    }

    @Override
    public void delete(int id) throws SpecialtyDAOException {
        String sql = "DELETE FROM SPECIALTIES WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
        } catch (SQLException e) {
            DBUtil.rollbackTransaction();
            throw new SpecialtyDAOException("SQL Error in Specialty deletion with ID: " + id);
        }
    }

    @Override
    public List<Specialty> getAllSpecialties() throws SpecialtyDAOException {
        String sql = "SELECT * FROM SPECIALTIES";
        List<Specialty> specialties = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Specialty specialty = new Specialty(rs.getInt("ID"), rs.getString("NAME"));
                specialties.add(specialty);
            }

        } catch (SQLException e) {
            throw new SpecialtyDAOException("SQL Error while retrieving all specialties");
        }

        return specialties;
    }
}
