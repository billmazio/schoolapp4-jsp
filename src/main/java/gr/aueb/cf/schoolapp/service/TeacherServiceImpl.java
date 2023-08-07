package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherNotFoundException;

import java.util.List;
import java.util.Optional;

public class TeacherServiceImpl implements ITeacherService {
    private final ITeacherDAO teacherDAO;

    public TeacherServiceImpl(ITeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws TeacherDAOException {
        if (dto == null) return null;
        Teacher teacher;

        try {
            teacher = map(dto);
            System.out.println("Service returned teacher: " + teacher.getLastname());
            return teacherDAO.insert(teacher);
        } catch (TeacherDAOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws TeacherDAOException, TeacherNotFoundException {
        if (dto == null) return null;
        Teacher teacher;

        try {
            teacher = map(dto);
            if (teacherDAO.getById(teacher.getId()) == null) {
                throw new TeacherNotFoundException(teacher);
            }

            return teacherDAO.update(teacher);
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteTeacher(int id) throws TeacherDAOException, TeacherNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherDAO.getById(id);
            if (teacher == null) {
                throw new TeacherNotFoundException("Teacher with id " + id + " not found");
            }

            teacherDAO.delete(id);
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname) throws TeacherDAOException , TeacherNotFoundException {
       if (lastname == null) {
           throw new IllegalArgumentException("lastname cannot be null");
       }

        try {
            Optional<List> teachersOptional = teacherDAO.getByLastname(lastname);
        if (!teachersOptional.isPresent()) {
            throw new TeacherNotFoundException("No teachers found with lastname: " + lastname);
        }
            return teachersOptional.get();

        }catch (TeacherNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherDAOException, TeacherNotFoundException {
        Teacher teacher;
        try {
            teacher = teacherDAO.getById(id);
            if (teacher == null) {
                throw new TeacherNotFoundException("Teacher with id " + id + " not found");
            }
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = null;
        try {
            teachers = teacherDAO.getAllTeachers();
        } catch (TeacherDAOException e) {
            e.printStackTrace();
            // handle exception
        }
        return teachers;
    }


    private Teacher map(TeacherInsertDTO dto) {
        return new Teacher(null,dto.getFirstname(), dto.getLastname(), dto.getSpecialtyId());
    }

    private Teacher map(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getSpecialtyId());
    }


}
