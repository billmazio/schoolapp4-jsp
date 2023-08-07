package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.IMeetingDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.MeetingDAOException;
import gr.aueb.cf.schoolapp.dto.MeetingInsertDTO;
import gr.aueb.cf.schoolapp.dto.MeetingUpdateDTO;
import gr.aueb.cf.schoolapp.model.Meeting;
import gr.aueb.cf.schoolapp.service.exceptions.MeetingNotFoundException;


import java.util.List;
import java.util.Optional;

public class MeetingServiceImpl implements IMeetingService {
    private final IMeetingDAO meetingDAO;
    public MeetingServiceImpl(IMeetingDAO meetingDAO) {this.meetingDAO = meetingDAO; }

    @Override
    public Meeting insertMeeting(MeetingInsertDTO dto) throws MeetingDAOException {
        if (dto == null) return null;
        Meeting meeting;

        try {
            meeting = map(dto);
            System.out.println("Service returned Meeting: " + meeting.getRoom());
            return meetingDAO.insert(meeting);
        } catch (MeetingDAOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Meeting updateMeeting(MeetingUpdateDTO dto) throws MeetingDAOException, MeetingNotFoundException {
        if (dto == null) return null;
        Meeting meeting;

        try {
            meeting = map(dto);
            if (meetingDAO.getById(meeting.getId()) == null) {
                throw new MeetingNotFoundException(meeting);
            }

            return meetingDAO.update(meeting);
        } catch (MeetingDAOException | MeetingNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void deleteMeeting(int id) throws MeetingDAOException, MeetingNotFoundException {
        Meeting meeting;

        try {
            meeting = meetingDAO.getById(id);
            if (meeting == null) {
                throw new MeetingNotFoundException("Meeting with id " + id + " not found");
            }

            meetingDAO.delete(id);
        } catch (MeetingDAOException | MeetingNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Meeting> getMeetingByRoom(String room) throws MeetingDAOException, MeetingNotFoundException {
        List<Meeting> meetings;

        try {
          Optional<List> meetingsOptional = meetingDAO.getByRoom(room);
            if (!meetingsOptional.isPresent()) {
                throw new MeetingNotFoundException("No meetings found with room: " +room);
            }
            return meetingsOptional.get();
        } catch (MeetingNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Meeting getMeetingById(int id) throws MeetingDAOException, MeetingNotFoundException {
        Meeting meeting;
        try {
            meeting = meetingDAO.getById(id);
            if (meeting == null) {
                throw new MeetingNotFoundException("Meeting with id " + id + " not found");
            }
        } catch (MeetingDAOException | MeetingNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        return meeting;
    }

    private Meeting map(MeetingInsertDTO dto) {
        return new Meeting(null,dto.getTeacherId(), dto.getStudentId(), dto.getRoom(), dto.getMeetingDate());
    }

    private Meeting map(MeetingUpdateDTO dto) {
        return new Meeting(dto.getId(), dto.getTeacherId(), dto.getStudentId(), dto.getRoom(), dto.getMeetingDate());
    }
}
