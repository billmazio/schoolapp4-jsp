package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.MeetingDAOException;
import gr.aueb.cf.schoolapp.model.Meeting;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeetingDAOImpl implements  IMeetingDAO{

    @Override
    public Meeting insert(Meeting meeting) throws MeetingDAOException {
        String sql = "INSERT INTO MEETINGS (TEACHER_ID, STUDENT_ID, MEETING_ROOM, MEETING_DATE) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int teacherId = meeting.getTeacherId();
            int studentId = meeting.getStudentId();
            String room = meeting.getRoom();
            java.sql.Date meetingDate = meeting.getMeetingDate();

            ps.setInt(1, teacherId);
            ps.setInt(2, studentId);
            ps.setString(3,room);
            ps.setDate(4, meetingDate);

            DBUtil.beginTransaction();
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int generatedId = 0;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
                meeting.setId(generatedId); // Set the generated ID
            }
            generatedKeys.close();

            DBUtil.commitTransaction();

        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new MeetingDAOException("SQL Error in Meeting insertion: " + e1.getMessage());
        }
        return meeting;
    }


    @Override
    public Meeting update(Meeting meeting) throws MeetingDAOException {
        String sql = "UPDATE MEETINGS SET TEACHER_ID = ?, STUDENT_ID = ?, MEETING_ROOM = ?, MEETING_DATE = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {

            Integer id = meeting.getId();
            Integer teacherId = meeting.getTeacherId();
            Integer studentId = meeting.getStudentId();
            String room = meeting.getRoom();
            java.sql.Date meetingDate = meeting.getMeetingDate();


            ps.setInt(1, teacherId);
            ps.setInt(2, studentId);
            ps.setString(3, room);
            ps.setDate(4, meetingDate);
            ps.setInt(5, id);


            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();

            return meeting;
        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new MeetingDAOException("SQL Error in Meeting" + meeting + " update");
        }
    }

    @Override
    public void delete(int id) throws MeetingDAOException {
        String sql = "DELETE FROM MEETINGS WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();

        } catch (SQLException e) {

            //e.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new MeetingDAOException("SQL Error in Meeting with id: " + id + " deletion");
        }
    }

    @Override
    public Optional<List> getByRoom(String room) throws MeetingDAOException {
        String sql = "SELECT * FROM MEETINGS WHERE MEETING_ROOM LIKE ?";
        List<Meeting> meetings = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setString(1, room + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Meeting meeting = new Meeting(rs.getInt("ID"), rs.getInt("TEACHER_ID"), rs.getInt("STUDENT_ID"), rs.getString("MEETING_ROOM"), rs.getDate("MEETING_DATE"));
                meetings.add(meeting);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if (meetings.isEmpty()) {
           return Optional.empty();
        } else {
            return Optional.of(meetings);
        }

    }

    @Override
    public Meeting getById(int id) throws MeetingDAOException {
        String sql = "SELECT * FROM MEETINGS WHERE ID = ?";
        Meeting meeting = null;

        try (Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs;
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                meeting = new Meeting(rs.getInt("ID"), rs.getInt("TEACHER_ID"), rs.getInt("STUDENT_ID"), rs.getString("MEETING_ROOM"), rs.getDate("MEETING_DATE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meeting;
    }
}
