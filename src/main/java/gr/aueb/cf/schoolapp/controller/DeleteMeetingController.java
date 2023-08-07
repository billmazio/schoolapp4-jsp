package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dao.IMeetingDAO;
import gr.aueb.cf.schoolapp.dao.MeetingDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.MeetingDAOException;
import gr.aueb.cf.schoolapp.dto.MeetingDeleteDTO;
import gr.aueb.cf.schoolapp.service.IMeetingService;
import gr.aueb.cf.schoolapp.service.MeetingServiceImpl;
import gr.aueb.cf.schoolapp.service.exceptions.MeetingNotFoundException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/schoolapp/deleteMeeting")
public class DeleteMeetingController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    IMeetingDAO meetingDAO = new MeetingDAOImpl();
    IMeetingService meetingService = new MeetingServiceImpl(meetingDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int teacherId = Integer.parseInt(request.getParameter("teacherId"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String room = request.getParameter("room");
        String meetingDateStr = request.getParameter("meetingDate");

        // Convert meetingDate to java.sql.Date format
        java.sql.Date meetingDate = null;
        if (meetingDateStr != null && !meetingDateStr.isEmpty()) {
            try {
                meetingDate = java.sql.Date.valueOf(meetingDateStr);
            } catch (IllegalArgumentException e ) {
                request.setAttribute("deleteAPIError", true);
                request.setAttribute("message", "Invalid meetingDate format. USE 'YYYY-MM-DD'.");
                request.getRequestDispatcher("/school/static/templates/meetings.jsp")
                        .forward(request, response);
                return;
            }
        }

        MeetingDeleteDTO meetingDTO = new MeetingDeleteDTO();
        meetingDTO.setId(id);
        meetingDTO.setTeacherId(teacherId);
        meetingDTO.setStudentId(studentId);
        meetingDTO.setRoom(room);
        meetingDTO.setMeetingDate(meetingDate);

        try {
            meetingService.deleteMeeting(id);
            request.setAttribute("meetingDTO", meetingDTO);
            request.getRequestDispatcher("/school/static/templates/meetingDeleted.jsp")
                    .forward(request, response);
        } catch (MeetingNotFoundException | MeetingDAOException e) {
            request.setAttribute("deleteAPIError", true);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/school/static/templates/meetingDeleted.jsp")
                    .forward(request, response);
        }
    }
}
