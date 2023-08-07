package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.CityDAOImpl;
import gr.aueb.cf.schoolapp.dao.ICityDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.CityDAOException;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.service.CityServiceImpl;
import gr.aueb.cf.schoolapp.service.ICityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/schoolapp/menu")
public class MenuController extends HttpServlet {

    @Override


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("studentsNotFound", false);
        request.setAttribute("teachersNotFound", false);
        request.setAttribute("citiesNotFound",false);
        request.setAttribute("specialtiesNotFound", false);
        request.setAttribute("meetingsNotFound",false);
        request.setAttribute("isError", false);
        request.setAttribute("error", "");



        request.getRequestDispatcher("/school/static/templates/controlPanel.jsp").forward(request, response);
    }
}

