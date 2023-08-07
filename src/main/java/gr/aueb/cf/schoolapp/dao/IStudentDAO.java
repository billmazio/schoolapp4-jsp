package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.exceptions.StudentNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IStudentDAO {

    Student insert(Student student) throws StudentDAOException;
    Student update(Student student) throws StudentDAOException;
    void delete(int id) throws StudentDAOException;
    Optional<List> getByLastname(String lastname) throws StudentDAOException;
    Student getById(int id) throws StudentDAOException;
    List<Student> getAllStudents() throws StudentDAOException;
}
