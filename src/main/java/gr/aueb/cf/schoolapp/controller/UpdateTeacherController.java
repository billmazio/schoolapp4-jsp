package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.TeacherDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.TeacherServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherNotFoundException;
import gr.aueb.cf.schoolapp.validator.TeacherValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/schoolapp/updateTeacher")
public class UpdateTeacherController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ITeacherDAO teacherDAO = new TeacherDAOImpl();
	private final ITeacherService teacherService = new TeacherServiceImpl(teacherDAO);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/school/static/templates/teacherUpdate.jsp")
				.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		int specialtyId = Integer.parseInt(request.getParameter("specialtyId"));

		TeacherUpdateDTO newTeacherDTO = new TeacherUpdateDTO();
		newTeacherDTO.setId(id);
		newTeacherDTO.setFirstname(firstname);
		newTeacherDTO.setLastname(lastname);
		newTeacherDTO.setSpecialtyId(specialtyId);
		request.setAttribute("updatedTeacher", newTeacherDTO);
		
		try {
			Map<String, String > errors = TeacherValidator.validate(newTeacherDTO);

			if (!errors.isEmpty()) {
				String firstnameMessage = (errors.get("firstname") != null) ? "Firstname: " + errors.get("firstname") : "";
				String lastnameMessage = (errors.get("lastname") != null) ? "Lastname: " + errors.get("lastname") : "";
				//String specialtyIdMessage = (errors.get("specialtyId") != null) ? "Specialty's ID: " + errors.get("specialtyId") : "";
				request.setAttribute("error", firstnameMessage + lastnameMessage);
				request.getRequestDispatcher("/school/static/templates/teachersmenu.jsp")
						.forward(request, response);
			}

			Teacher teacher =  teacherService.updateTeacher(newTeacherDTO);
			request.setAttribute("message", "");
			request.setAttribute("teacher", teacher);
			request.getRequestDispatcher("/school/static/templates/teacherUpdated.jsp")
					.forward(request, response);
		} catch (TeacherNotFoundException | TeacherDAOException e) {
			String message = e.getMessage();
			request.setAttribute("message", message);
			request.getRequestDispatcher("/schoolapp/static/templates//teacherUpdated.jsp")
					.forward(request, response);

		}
	}
}
