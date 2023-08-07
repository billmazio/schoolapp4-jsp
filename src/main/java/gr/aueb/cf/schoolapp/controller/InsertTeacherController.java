package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ISpecialtyDAO;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.SpecialtyDAOImpl;
import gr.aueb.cf.schoolapp.dao.TeacherDAOImpl;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.model.Specialty;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ISpecialtyService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.SpecialtyServiceImpl;
import gr.aueb.cf.schoolapp.service.TeacherServiceImpl;
import gr.aueb.cf.schoolapp.validator.TeacherValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/schoolapp/teacherInsert")
public class InsertTeacherController extends HttpServlet {
    private final ITeacherDAO teacherDAO = new TeacherDAOImpl();
    private final ITeacherService teacherService = new TeacherServiceImpl(teacherDAO);

    private final ISpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    private final ISpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Specialty> specialties = specialtyService.getAllSpecialties();
            request.setAttribute("specialties", specialties);
            request.getRequestDispatcher("/school/static/templates/teachersmenu.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle the error as appropriate. For now, just forward to the menu
            request.setAttribute("error", "There was an error retrieving the list of specialties.");
            request.getRequestDispatcher("/school/static/templates/teachersmenu.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String firstname = request.getParameter("firstname").trim();
        String lastname = request.getParameter("lastname").trim();
        String specialtyIdParam = request.getParameter("specialtyId").trim();

        TeacherInsertDTO teacherInsertDTO = new TeacherInsertDTO();
        teacherInsertDTO.setFirstname(firstname);
        teacherInsertDTO.setLastname(lastname);

        // Check if any of the required fields are empty
        if (firstname.isEmpty() || lastname.isEmpty() || specialtyIdParam.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            forwardToTeachersMenu(request, response);
            return;
        }

        int specialtyId;
        try {
            specialtyId = Integer.parseInt(specialtyIdParam);
            teacherInsertDTO.setSpecialtyId(specialtyId);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid specialty ID. Please provide a valid integer value.");
            forwardToTeachersMenu(request, response);
            return;
        }

        // Use the TeacherValidator to check for errors in the form data
        Map<String, String> errors = TeacherValidator.validate(teacherInsertDTO);
        if (!errors.isEmpty()) {
            request.setAttribute("error", errors);
            forwardToTeachersMenu(request, response);
            return;
        }

        try {
            Teacher teacher = teacherService.insertTeacher(teacherInsertDTO);
            request.setAttribute("insertedTeacher", teacher);
            request.getRequestDispatcher("/school/static/templates/teacherInserted.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("sqlError", true);
            request.setAttribute("message", e.getMessage());
            forwardToTeachersMenu(request, response);
        }
    }

    private void forwardToTeachersMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Specialty> specialties = specialtyService.getAllSpecialties();
            request.setAttribute("specialties", specialties);
            request.getRequestDispatcher("/school/static/templates/teachersmenu.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "There was an error retrieving the list of specialties.");
            request.getRequestDispatcher("/school/static/templates/teachersmenu.jsp").forward(request, response);
        }
    }
}
