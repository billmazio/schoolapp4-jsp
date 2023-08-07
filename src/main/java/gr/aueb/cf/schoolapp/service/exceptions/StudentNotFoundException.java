package gr.aueb.cf.schoolapp.service.exceptions;

import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;

public class StudentNotFoundException extends Exception {
       public StudentNotFoundException(Student student) {
        super("Student with id " +student.getId() + " does not exist");
    }



    public StudentNotFoundException(String s) {
        super(s);
    }
}
