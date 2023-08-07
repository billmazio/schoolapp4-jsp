package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ISpecialtyDAO;
import gr.aueb.cf.schoolapp.dao.SpecialtyDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.SpecialtyDAOException;
import gr.aueb.cf.schoolapp.dto.SpecialtyDeleteDTO;
import gr.aueb.cf.schoolapp.service.ISpecialtyService;
import gr.aueb.cf.schoolapp.service.SpecialtyServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.SpecialtyNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/schoolapp/deleteSpecialty")
public class DeleteSpecialtyController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ISpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    ISpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        SpecialtyDeleteDTO specialtyDTO = new SpecialtyDeleteDTO();
        specialtyDTO.setId(id);
        specialtyDTO.setName(name);

        try {
            specialtyService.deleteSpecialty(id);
            request.setAttribute("specialtyDTO", specialtyDTO);
            request.getRequestDispatcher("/school/static/templates/specialtyDeleted.jsp")
                    .forward(request, response);
        } catch (SpecialtyDAOException | SpecialtyNotFoundException e) {
            request.setAttribute("deleteAPIError", true);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/school/static/templates/specialties.jsp")
                    .forward(request, response);
        }
    }
}
