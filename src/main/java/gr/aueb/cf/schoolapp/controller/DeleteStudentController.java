package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dao.StudentDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.dto.StudentDeleteDTO;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.service.StudentServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.StudentNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/schoolapp/deleteStudent")
public class DeleteStudentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IStudentDAO studentDAO = new StudentDAOImpl();
    IStudentService studentService = new StudentServiceImpl(studentDAO);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String gender = request.getParameter("gender");
        String birthdateStr = request.getParameter("birthdate");
        int cityId = Integer.parseInt(request.getParameter("cityId"));

        // Convert birthdate to java.sql.Date format
        java.sql.Date birthdate = null;
        if (birthdateStr != null && !birthdateStr.isEmpty()) {
            try {
                birthdate = java.sql.Date.valueOf(birthdateStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("deleteAPIError", true);
                request.setAttribute("message", "Invalid birthdate format. Use 'YYYY-MM-DD'.");
                request.getRequestDispatcher("/school/static/templates/students.jsp")
                        .forward(request, response);
                return;
            }
        }

        StudentDeleteDTO studentDTO = new StudentDeleteDTO();
        studentDTO.setId(id);
        studentDTO.setFirstname(firstname);
        studentDTO.setLastname(lastname);
        studentDTO.setGender(gender);
        studentDTO.setBirthdate(birthdate);
        studentDTO.setCityId(cityId);
        try {
            studentService.deleteStudent(id);
            request.setAttribute("studentDTO", studentDTO);
            request.getRequestDispatcher("/school/static/templates/studentDeleted.jsp")
                    .forward(request, response);
        } catch (StudentNotFoundException | StudentDAOException e) {
            request.setAttribute("deleteAPIError", true);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/school/static/templates/students.jsp")
                    .forward(request, response);
        }
    }


}
