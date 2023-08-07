package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ISpecialtyDAO;
import gr.aueb.cf.schoolapp.dao.SpecialtyDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.SpecialtyDAOException;
import gr.aueb.cf.schoolapp.dto.SpecialtyInsertDTO;
import gr.aueb.cf.schoolapp.model.Specialty;
import gr.aueb.cf.schoolapp.service.ISpecialtyService;
import gr.aueb.cf.schoolapp.service.SpecialtyServiceImpl;
import gr.aueb.cf.schoolapp.validator.SpecialtyValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/schoolapp/specialtyInsert")
public class InsertSpecialtyController extends HttpServlet {
    private final ISpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    private final ISpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/schoolapp/menu").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the submitted form data
        String name = request.getParameter("name").trim();

        // Create the SpecialtyInsertDTO object
        SpecialtyInsertDTO specialtyInsertDTO = new SpecialtyInsertDTO();
        specialtyInsertDTO.setName(name);

        // Perform validation
        Map<String, String> errors = SpecialtyValidator.validate(specialtyInsertDTO);

        // If there are validation errors, set them in the request and forward back to the form page
        if (!errors.isEmpty()) {
            String specialtyNameMessage = errors.get("name");
            request.setAttribute("error", specialtyNameMessage);
            request.getRequestDispatcher("/school/static/templates/specialtiesmenu.jsp").forward(request, response);
        } else {
            // If no errors, proceed with the insert operation and display success message
            try {
                Specialty specialty = specialtyService.insertSpecialty(specialtyInsertDTO);
                request.setAttribute("insertedSpecialty", specialty);
                request.getRequestDispatcher("/school/static/templates/specialtyInserted.jsp").forward(request, response);
            } catch (SpecialtyDAOException e) {
                // Handle database-related errors
                request.setAttribute("sqlError", true);
                request.setAttribute("message", e.getMessage());
                request.getRequestDispatcher("/schoolapp/menu").forward(request, response);
            }
        }
    }

}
