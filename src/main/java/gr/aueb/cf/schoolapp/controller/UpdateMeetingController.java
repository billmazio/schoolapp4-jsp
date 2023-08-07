package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.IMeetingDAO;
import gr.aueb.cf.schoolapp.dao.MeetingDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.MeetingDAOException;
import gr.aueb.cf.schoolapp.dto.MeetingUpdateDTO;
import gr.aueb.cf.schoolapp.model.Meeting;
import gr.aueb.cf.schoolapp.service.IMeetingService;
import gr.aueb.cf.schoolapp.service.MeetingServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.MeetingNotFoundException;
import gr.aueb.cf.schoolapp.validator.MeetingValidator;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/schoolapp/updateMeeting")
public class UpdateMeetingController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IMeetingDAO meetingDAO = new MeetingDAOImpl();
    private final IMeetingService meetingService = new MeetingServiceImpl(meetingDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/school/static/templates/meetingUpdate.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        int teacherId = Integer.parseInt(request.getParameter("teacherId"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String room = request.getParameter("room");
        String meetingDateStr = request.getParameter("meetingDate");

        // Validate and convert birthdate to java.sql.Date format
        java.sql.Date meetingDate = null;
        if (meetingDateStr != null && !meetingDateStr.isEmpty()) {
            try {
                meetingDate = java.sql.Date.valueOf(meetingDateStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid birthdate format. Use 'YYYY-MM-DD'.");
                request.getRequestDispatcher("/school/static/templates/meetingsmenu.jsp")
                        .forward(request, response);
                return;
            }
        }

        MeetingUpdateDTO newMeetingDTO = new MeetingUpdateDTO();

        newMeetingDTO.setId(id);
        newMeetingDTO.setTeacherId(teacherId);
        newMeetingDTO.setStudentId(studentId);
        newMeetingDTO.setRoom(room);
        newMeetingDTO.setMeetingDate(meetingDate);
        request.setAttribute("updatedMeeting", newMeetingDTO);

        try {
            Map<String, String> errors = MeetingValidator.validate(newMeetingDTO);

            if (!errors.isEmpty()) {
                String idMessage = (errors.get("id") != null ) ? "Meeting's ID: " + errors.get("id") : "";
                String teacherIdMessage = (errors.get("teacherId") != null) ? "Teacher's ID: " + errors.get("teacherId") : "";
                String studentIdMessage = (errors.get("studentId") != null) ? "Student's ID: " + errors.get("studentId") : "";
                String roomMessage = (errors.get("room") != null) ? "Room: " + errors.get("room") : "";
                String meetingDateMessage = (errors.get("meetingDate") != null) ? "Meeting Date: " + errors.get("meetingDate") : "";

                String errorMessage = idMessage + " " + teacherIdMessage + " " + studentIdMessage + " " + roomMessage + " " + meetingDateMessage;
                request.setAttribute("error", errorMessage);

                request.getRequestDispatcher("/school/static/templates/meetingsmenu.jsp")
                        .forward(request, response);
            }

            Meeting meeting = meetingService.updateMeeting(newMeetingDTO);
            request.setAttribute("message", "");
            request.setAttribute("meetingUpdated", meeting);
            request.getRequestDispatcher("/school/static/templates/meetingUpdated.jsp")
                    .forward(request, response);
        } catch (MeetingNotFoundException | MeetingDAOException e) {
            String message = e.getMessage();
            request.setAttribute("message", message);
            request.getRequestDispatcher("/school/static/templates/meetingUpdated.jsp")
                    .forward(request, response);
        }
    }
}
