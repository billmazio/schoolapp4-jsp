package gr.aueb.cf.schoolapp.model;

public class Teacher {
    private Integer id;
    private String firstname;
    private String lastname;
    private Integer specialtyId;

    public Teacher() {}

    public Teacher(Integer id, String firstname, String lastname, Integer specialtyId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialtyId = specialtyId;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
