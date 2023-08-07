package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.TeacherDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherDeleteDTO;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.TeacherServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/schoolapp/deleteTeacher")
public class DeleteTeacherController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ITeacherDAO teacherDAO = new TeacherDAOImpl();
	ITeacherService teacherService = new TeacherServiceImpl(teacherDAO);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//response.setContentType("text/html; charset=UTF-8");
		int id = Integer.parseInt(request.getParameter("id"));
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		int specialtyId = Integer.parseInt(request.getParameter("specialtyId"));
		TeacherDeleteDTO teacherDTO = new TeacherDeleteDTO();
		teacherDTO.setId(id);
		teacherDTO.setFirstname(firstname);
		teacherDTO.setLastname(lastname);
		teacherDTO.setSpecialtyId(specialtyId);
		try {
			teacherService.deleteTeacher(id);
			request.setAttribute("teacherDTO", teacherDTO);
			request.getRequestDispatcher("/school/static/templates/teacherDeleted.jsp")
					.forward(request, response);
		} catch (TeacherNotFoundException | TeacherDAOException e) {
			request.setAttribute("deleteAPIError", true);
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher("/school/static/templates/teachers.jsp")
					.forward(request, response);
		}
	}
}

