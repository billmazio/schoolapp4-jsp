//package gr.aueb.cf.schoolapp.dto;

//import java.sql.Date;

//public class StudentDeleteDTO extends Base{
//    private Integer id;
//    private String firstname;
//    private String lastname;
//    private String gender;
//    private java.sql.Date birthdate;
//
//
//    public StudentDeleteDTO() {}
//
//
//    @Override
//    public Integer getId() {
//        return id;
//    }
//
//
//    @Override
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public Date getBirthdate() {
//        return birthdate;
//    }
//
//    public void setBirthdate(Date birthdate) {
//        this.birthdate = birthdate;
//    }
//}
//
package gr.aueb.cf.schoolapp.dto;

        import java.sql.Date;

public class StudentDeleteDTO extends Base {

    private String firstname;
    private String lastname;
    private String gender;
    private java.sql.Date birthdate;
    private Integer cityId;

    public StudentDeleteDTO() {}

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}
