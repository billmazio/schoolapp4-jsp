package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.StudentNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherNotFoundException;

import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements IStudentService {

    private final IStudentDAO studentDAO;

    public StudentServiceImpl(IStudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

        @Override
        public Student insertStudent(StudentInsertDTO dto) throws StudentDAOException {

                if (dto == null) return null;
                Student student;

                try {
                    student = map(dto);
                    System.out.println("Service returned student: " + student.getLastname());
                    return studentDAO.insert(student);
                } catch (StudentDAOException e) {
                    e.printStackTrace();
                    throw e;
                }
        }

        @Override
        public Student updateStudent (StudentUpdateDTO dto) throws StudentDAOException, StudentNotFoundException {
            if (dto == null) return null;
            Student student;

            try {
                student = map(dto);
                if (studentDAO.getById(student.getId()) == null) {
                    throw new StudentNotFoundException(student);
                }

                return studentDAO.update(student);
            } catch (StudentDAOException | StudentNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
        }

        @Override
        public void deleteStudent ( int id) throws StudentDAOException, StudentNotFoundException {
            Student student;

            try {
                student = studentDAO.getById(id);
                if (student == null) {
                    throw new StudentNotFoundException("Student with id " + id + " not found");
                }

                studentDAO.delete(id);
            } catch (StudentDAOException | StudentNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
        }

        @Override
        public List<Student> getStudentsByLastname (String lastname) throws StudentDAOException, StudentNotFoundException {
            if (lastname == null) {
                throw new IllegalArgumentException("lastname cannot be null");
            }


            try {
                Optional<List> studentsOptional = studentDAO.getByLastname(lastname);
                if (!studentsOptional.isPresent()) {
                    throw new StudentNotFoundException("No students found with lastname: " + lastname);
                }
                return studentsOptional.get();

            }catch (StudentNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
        }

        @Override
        public Student getStudentById ( int id) throws StudentDAOException, StudentNotFoundException {
            Student student;
            try {
                student = studentDAO.getById(id);
                if (student == null) {
                    throw new StudentNotFoundException("Student with id " + id + " not found");
                }
            } catch (StudentDAOException | StudentNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
            return student;
        }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = null;
        try {
            students = studentDAO.getAllStudents();
        } catch (StudentDAOException e) {
            e.printStackTrace();
            // handle exception
        }
        return students;
    }


    private Student map(StudentInsertDTO dto) {
        return new Student(null, dto.getFirstname(), dto.getLastname(), dto.getGender(), dto.getBirthdate(),dto.getCityId());
    }

    private Student map(StudentUpdateDTO dto) {
        return new Student(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getGender(), dto.getBirthdate(),dto.getCityId());
    }


}

