package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.exceptions.StudentNotFoundException;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements IStudentDAO{
//    @Override
//    public Student insert(Student student) throws StudentDAOException {
//        String sql = "INSERT INTO STUDENTS (FIRSTNAME, LASTNAME, GENDER, BIRTHDATE) VALUES (?, ?, ?, ?)";
//
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            String firstname = student.getFirstname();
//            String lastname = student.getLastname();
//            String gender = student.getGender();
//            Date birthdate = student.getBirthdate();
//
//
//            ps.setString(1, firstname);
//            ps.setString(2, lastname);
//            ps.setString(3, gender);
//            ps.setDate(4, (java.sql.Date) birthdate);
//
//            DBUtil.beginTransaction();
//            ps.executeUpdate();
//            DBUtil.commitTransaction();
//
//            ResultSet generatedKeys = ps.getGeneratedKeys();
//            int generatedId = 0;
//            if (generatedKeys.next()) {
//                generatedId = generatedKeys.getInt(1);
//            }
//
//            student.setId(generatedId);
//            generatedKeys.close();
//
//        } catch (SQLException e1) {
//            //e1.printStackTrace();
//            DBUtil.rollbackTransaction();
//            throw new StudentDAOException("SQL Error in Student" + student + " insertion");
//        }
//        return student;
//    }
//
//    @Override
//    public Student update(Student student) throws StudentDAOException {
//        String sql = "UPDATE STUDENTS SET FIRSTNAME = ?, LASTNAME = ?, GENDER = ?, BIRTHDATE = ? WHERE ID = ?";
//
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql);) {
//            int id = student.getId();
//            String firstname = student.getFirstname();
//            String lastname = student.getLastname();
//            String gender = student.getGender();
//            Date date = student.getBirthdate();
//
//            ps.setString(1, firstname);
//            ps.setString(2, lastname);
//            ps.setString(3, gender);
//            ps.setDate(4, (java.sql.Date) date);
//            ps.setInt(5, id);
//
//            DBUtil.beginTransaction();
//            ps.executeUpdate();
//            DBUtil.commitTransaction();
//            return student;
//        } catch (SQLException e1) {
//            //e1.printStackTrace();
//            DBUtil.rollbackTransaction();
//            throw new StudentDAOException("SQL Error in Student" + student + " insertion");
//        }
//    }
@Override
public Student insert(Student student) throws StudentDAOException {
    String sql = "INSERT INTO STUDENTS (FIRSTNAME, LASTNAME, GENDER, BIRTHDATE ,CITY_ID) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DBUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        String firstname = student.getFirstname();
        String lastname = student.getLastname();
        String gender = student.getGender();
        Date birthdate = student.getBirthdate();
        Integer cityId = student.getCityId();

        ps.setString(1, firstname);
        ps.setString(2, lastname);
        ps.setString(3, gender);
        ps.setDate(4, (java.sql.Date) birthdate);
        ps.setInt(5, cityId);

        DBUtil.beginTransaction();
        ps.executeUpdate();
        DBUtil.commitTransaction();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        int generatedId = 0;
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
        }

        student.setId(generatedId);
        generatedKeys.close();

    } catch (SQLException e1) {
        //e1.printStackTrace();
        DBUtil.rollbackTransaction();
        throw new StudentDAOException("SQL Error in Student" + student + " insertion");
    }
    return student;
}
    @Override
    public Student update(Student student) throws StudentDAOException {
        String sql = "UPDATE STUDENTS SET FIRSTNAME = ?, LASTNAME = ?, GENDER = ?, BIRTHDATE = ? ,CITY_ID = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            Integer id = student.getId();
            String firstname = student.getFirstname();
            String lastname = student.getLastname();
            String gender = student.getGender();
            Date date = student.getBirthdate();
            Integer cityId = student.getCityId();

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, gender);
            ps.setDate(4, (java.sql.Date) date);
            ps.setInt(5, cityId);
            ps.setInt(6, id);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            return student;
        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new StudentDAOException("SQL Error in Student" + student + " update");
        }
    }


    @Override
    public void delete(int id) throws StudentDAOException {
        String sql = "DELETE FROM STUDENTS WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
        } catch (SQLException e) {
            //e.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new StudentDAOException("SQL Error in Student with id: " + id + " deletion");
        }
    }

    @Override
    public Optional<List> getByLastname(String lastname) throws StudentDAOException  {
        String sql = "SELECT * FROM STUDENTS WHERE LASTNAME LIKE ?";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setString(1, lastname + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Student student = new Student(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("GENDER"), rs.getDate("BIRTHDATE"), rs.getInt("CITY_ID"));
                students.add(student);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if (students.isEmpty()) {
            return Optional.empty();
        } else  {
            return Optional.of(students);
        }
    }

    @Override
    public Student getById(int id) throws StudentDAOException {
        String sql = "SELECT * FROM STUDENTS WHERE ID = ?";
        Student student = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                student = new Student(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("GENDER"), rs.getDate("BIRTHDATE"), rs.getInt("CITY_ID"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return student;
    }
    @Override
    public List<Student> getAllStudents() throws StudentDAOException {
        String sql = "SELECT * FROM STUDENTS";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // Create a new Student object and add it to the list
                Student student = new Student(rs.getInt("ID"), rs.getString("FIRSTNAME"),rs.getString("LASTNAME"),rs.getString("GENDER"),rs.getDate("BIRTHDATE"), rs.getInt("CITY_ID") );
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new StudentDAOException("SQL Error in Student GetAll");
        }


        System.out.println("Number of cities fetched: " + students.size());
        return students;
    }
}