package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.CityDAOImpl;
import gr.aueb.cf.schoolapp.dao.ICityDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.CityDAOException;
import gr.aueb.cf.schoolapp.dto.CityDeleteDTO;
import gr.aueb.cf.schoolapp.service.CityServiceImpl;
import gr.aueb.cf.schoolapp.service.ICityService;
import gr.aueb.cf.schoolapp.service.exceptions.CityNotFoundException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/schoolapp/deleteCity")
public class DeleteCityController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ICityDAO cityDAO = new CityDAOImpl();
    ICityService cityService = new CityServiceImpl(cityDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        CityDeleteDTO cityDTO = new CityDeleteDTO();
        cityDTO.setId(id);
        cityDTO.setName(name);

        try {
            cityService.deleteCity(id);
            request.setAttribute("cityDTO", cityDTO);
            request.getRequestDispatcher("/school/static/templates/cityDeleted.jsp")
                    .forward(request, response);
        } catch (CityDAOException | CityNotFoundException e) {
            request.setAttribute("deleteAPIError", true);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/school/static/templates/cities.jsp")
                    .forward(request, response);
        }
    }
}
