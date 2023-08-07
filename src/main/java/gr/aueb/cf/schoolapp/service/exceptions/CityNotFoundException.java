package gr.aueb.cf.schoolapp.service.exceptions;

import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.model.Teacher;

public class CityNotFoundException extends Exception{
    public CityNotFoundException(City city) {
        super("City with id " + city.getId() + " does not exist");
    }

    public CityNotFoundException(String s) {
        super(s);
    }
}
