package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.CityDAOImpl;
import gr.aueb.cf.schoolapp.dao.ICityDAO;
import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dao.StudentDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.CityDAOException;
import gr.aueb.cf.schoolapp.dao.exceptions.StudentDAOException;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.CityServiceImpl;
import gr.aueb.cf.schoolapp.service.ICityService;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.service.StudentServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.CityNotFoundException;
import gr.aueb.cf.schoolapp.validator.StudentValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/schoolapp/studentInsert")
public class InsertStudentController extends HttpServlet {
    private final IStudentDAO studentDAO = new StudentDAOImpl();
    private final IStudentService studentService = new StudentServiceImpl(studentDAO);
    private final ICityDAO cityDAO = new CityDAOImpl();
    private final ICityService cityService = new CityServiceImpl(cityDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<City> cities = cityService.getAllCities();
            request.setAttribute("cities", cities);
            request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp").forward(request, response);
        } catch (CityDAOException | CityNotFoundException e) {
            // Handle the error as appropriate. For now, just forward to the menu
            request.setAttribute("error", "There was an error retrieving the list of cities.");
            request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String firstname = request.getParameter("firstname").trim();
        String lastname = request.getParameter("lastname").trim();
        String gender = request.getParameter("gender").trim();
        String birthdate = request.getParameter("birthdate").trim();
        String cityIdParam = request.getParameter("cityId").trim();

        // Check if any of the required fields are empty
        if (firstname.isEmpty() || lastname.isEmpty() || gender.isEmpty() || birthdate.isEmpty() || cityIdParam.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            forwardToStudentsMenu(request, response);
            return;
        }

        // Convert the birthdate to a Date object
        Date birthDateValue;
        try {
            birthDateValue = Date.valueOf(birthdate);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format for Birthdate.");
            forwardToStudentsMenu(request, response);
            return;
        }

        // Validate cityId
        int cityId;
        try {
            cityId = Integer.parseInt(cityIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid city. Please provide a valid city.");
            forwardToStudentsMenu(request, response);
            return;
        }

        StudentInsertDTO studentInsertDTO = new StudentInsertDTO();
        studentInsertDTO.setFirstname(firstname);
        studentInsertDTO.setLastname(lastname);
        studentInsertDTO.setBirthdate(birthDateValue);
        studentInsertDTO.setGender(gender);
        studentInsertDTO.setCityId(cityId);

        // Use the StudentValidator to check for errors in the form data
        Map<String, String> errors = StudentValidator.validate(studentInsertDTO);
        if (!errors.isEmpty()) {
            request.setAttribute("error", errors);
            forwardToStudentsMenu(request, response);
            return;
        }

        try {
            Student student = studentService.insertStudent(studentInsertDTO);
            request.setAttribute("insertedStudent", student);
            request.getRequestDispatcher("/school/static/templates/studentInserted.jsp").forward(request, response);
        } catch (StudentDAOException e) {
            request.setAttribute("sqlError", true);
            request.setAttribute("message", e.getMessage());
            forwardToStudentsMenu(request, response);
        }
    }

    private void forwardToStudentsMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<City> cities = cityDAO.getAllCities();
            request.setAttribute("cities", cities);
            request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp").forward(request, response);
        } catch (CityDAOException e) {
            request.setAttribute("error", "There was an error retrieving the list of cities.");
            request.getRequestDispatcher("/school/static/templates/studentsmenu.jsp").forward(request, response);
        }
    }
}
