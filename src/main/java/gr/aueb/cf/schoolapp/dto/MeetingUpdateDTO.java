package gr.aueb.cf.schoolapp.dto;

import java.sql.Date;

public class MeetingUpdateDTO extends Base{

    private Integer teacherId;
    private Integer studentId;
    private String room;
    private java.sql.Date meetingDate;

    public MeetingUpdateDTO() {}

    public MeetingUpdateDTO( Integer teacherId, Integer studentId, String room, java.sql.Date meetingDate) {
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.room = room;
        this.meetingDate = meetingDate;
    }


    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }
}
