package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ISpecialtyDAO;
import gr.aueb.cf.schoolapp.dao.SpecialtyDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.SpecialtyDAOException;
import gr.aueb.cf.schoolapp.dto.SpecialtyInsertDTO;
import gr.aueb.cf.schoolapp.dto.SpecialtyUpdateDTO;
import gr.aueb.cf.schoolapp.model.Specialty;
import gr.aueb.cf.schoolapp.service.ISpecialtyService;
import gr.aueb.cf.schoolapp.service.SpecialtyServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.SpecialtyNotFoundException;
import gr.aueb.cf.schoolapp.validator.SpecialtyValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/schoolapp/updateSpecialty")
public class UpdateSpecialtyController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ISpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    private final ISpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/school/static/templates/specialtyUpdate.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        SpecialtyUpdateDTO newSpecialtyDTO = new SpecialtyUpdateDTO();
        newSpecialtyDTO.setId(id);
        newSpecialtyDTO.setName(name);
        request.setAttribute("updatedSpecialty", newSpecialtyDTO);

        try {
            Map<String, String > errors = SpecialtyValidator.validate(newSpecialtyDTO);

            if (!errors.isEmpty()) {
                String nameMessage = (errors.get("name") != null) ? "Specialty Name: " + errors.get("name") : "";
                request.setAttribute("error", nameMessage);
                request.getRequestDispatcher("/school/static/templates/specialtiesmenu.jsp")
                        .forward(request, response);
            }

            Specialty specialty =  specialtyService.updateSpecialty(newSpecialtyDTO);
            request.setAttribute("message", "");
            request.setAttribute("specialty", specialty);
            request.getRequestDispatcher("/school/static/templates/specialtyUpdated.jsp")
                    .forward(request, response);
        } catch (SpecialtyNotFoundException | SpecialtyDAOException e) {
            String message = e.getMessage();
            request.setAttribute("message", message);
            request.getRequestDispatcher("/schoolapp/static/templates//specialtyUpdated.jsp")
                    .forward(request, response);
        }
    }
}
