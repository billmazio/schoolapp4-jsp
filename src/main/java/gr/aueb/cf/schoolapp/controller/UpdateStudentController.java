package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dao.StudentDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.service.StudentServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.StudentNotFoundException;
import gr.aueb.cf.schoolapp.validator.StudentValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/schoolapp/updateStudent")
public class UpdateStudentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IStudentDAO studentDAO = new StudentDAOImpl();
    private final IStudentService studentService = new StudentServiceImpl(studentDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/school/static/templates/studentUpdate.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        int cityId = Integer.parseInt(request.getParameter("cityId"));
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String gender = request.getParameter("gender");
        String birthdateStr = request.getParameter("birthdate");


        // Validate and convert birthdate to java.sql.Date format
        java.sql.Date birthdate = null;
        if (birthdateStr != null && !birthdateStr.isEmpty()) {
            try {
                birthdate = java.sql.Date.valueOf(birthdateStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid birthdate format. Use 'YYYY-MM-DD'.");
                request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp")
                        .forward(request, response);
                return;
            }
        }

        StudentUpdateDTO newStudentDTO = new StudentUpdateDTO();

        newStudentDTO.setId(id);
        newStudentDTO.setFirstname(firstname);
        newStudentDTO.setLastname(lastname);
        newStudentDTO.setGender(gender);
        newStudentDTO.setBirthdate(birthdate);
        newStudentDTO.setCityId(cityId);
        request.setAttribute("insertedStudent", newStudentDTO);

        try {
            Map<String, String> errors = StudentValidator.validate(newStudentDTO);

            if (!errors.isEmpty()) {
                String firstnameMessage = (errors.get("firstname") != null) ? "Firstname: " + errors.get("firstname") : "";
                String lastnameMessage = (errors.get("lastname") != null) ? "Lastname: " + errors.get("lastname") : "";
                String genderMessage = (errors.get("gender") != null) ? "Gender: " + errors.get("gender") : "";
                String birthdateMessage = (errors.get("birthdate") != null) ? "Birthdate: " + errors.get("birthdate") : "";

                String errorMessage = firstnameMessage + " " + lastnameMessage + " " + genderMessage + " " + birthdateMessage;
                request.setAttribute("error", errorMessage);

                request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp")
                        .forward(request, response);
            }

            Student student = studentService.updateStudent(newStudentDTO);
            request.setAttribute("message", "");
            request.setAttribute("updatedStudent", student);
            request.getRequestDispatcher("/school/static/templates/studentUpdated.jsp")
                    .forward(request, response);
        } catch (StudentNotFoundException | StudentDAOException e) {
            String message = e.getMessage();
            request.setAttribute("message", message);
            request.getRequestDispatcher("/school/static/templates/studentUpdated.jsp")
                    .forward(request, response);
        }
    }



}
