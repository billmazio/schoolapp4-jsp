package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDAOImpl implements ITeacherDAO {
    @Override
    public Teacher insert(Teacher teacher) throws TeacherDAOException {
        String sql = "INSERT INTO TEACHERS (FIRSTNAME, LASTNAME, SPECIALTY_ID) VALUES (?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String firstname = teacher.getFirstname();
            String lastname = teacher.getLastname();
            Integer specialtyId = teacher.getSpecialtyId();

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setInt(3,specialtyId);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int generatedId = 0;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            teacher.setId(generatedId);
            generatedKeys.close();

        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher",e1);
        }
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher) throws TeacherDAOException {
        String sql = "UPDATE TEACHERS SET FIRSTNAME = ?, LASTNAME = ?, SPECIALTY_ID = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            int id = teacher.getId();
            String firstname = teacher.getFirstname();
            String lastname = teacher.getLastname();
            Integer specialtyId = teacher.getSpecialtyId();

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setInt(3 ,specialtyId);
            ps.setInt(4, id);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            return teacher;
        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher update", e1);
        }
    }

    @Override
    public void delete(int id) throws TeacherDAOException {
        String sql = "DELETE FROM TEACHERS WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
        } catch (SQLException e) {
                //e.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher with id: " + id ,e);
        }
    }

    @Override
    public Optional<List> getByLastname(String lastname) throws TeacherDAOException {
        String sql = "SELECT * FROM TEACHERS WHERE LASTNAME LIKE ?";
        List<Teacher> teachers = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ) {
            ResultSet rs;
            ps.setString(1, lastname + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Teacher teacher = new Teacher(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getInt("SPECIALTY_ID"));
                teachers.add(teacher);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        if (teachers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(teachers);
        }
    }

    @Override
    public Teacher getById(int id) throws TeacherDAOException {
        String sql = "SELECT * FROM TEACHERS WHERE ID = ?";
        Teacher teacher = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                teacher = new Teacher(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getInt("SPECIALTY_ID"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() throws TeacherDAOException {
        String sql = "SELECT * FROM TEACHERS";
        List<Teacher> teachers = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String firstname = rs.getString("FIRSTNAME");
                String lastname = rs.getString("LASTNAME");
                int specialtyId = rs.getInt("SPECIALTY_ID");

                // Create a new Teacher object and add it to the list
                Teacher teacher = new Teacher(id, firstname, lastname, specialtyId);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TeacherDAOException("SQL Error in Teacher GetAll");
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return teachers;
    }
}
