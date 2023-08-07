package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import jdk.dynalink.beans.StaticClass;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StudentValidator {
    private StudentValidator() {}

    public static Map<String, String> validate(StudentInsertDTO dto) {
        Map<String, String> errors = new HashMap<>();

        // Firtsname validations
        if (dto.getFirstname().length() < 3 || dto.getFirstname().length() > 32) {
            errors.put("firstname", "size");
        }

        if (dto.getFirstname().matches("^.*\\s+.*$")) {
            errors.put("firstname", "whitespaces");
        }

        // Lastname Validations
        if (dto.getLastname().length() < 3 || dto.getLastname().length() > 32) {
            errors.put("lastname", "size");
        }

        if (dto.getLastname().matches("^.*\\s+.*$")) {
            errors.put("lastname", "whitespaces");
        }

        // Gender Validations
        if (dto.getGender() == null || dto.getGender().length() != 1 || (!dto.getGender().equalsIgnoreCase("M") && !dto.getGender().equalsIgnoreCase("F"))) {
            errors.put("gender", "Gender must be either 'M' or 'F'.");
        }


        // Birthdate validation
        Date birthdate = dto.getBirthdate();
        if (birthdate == null) {
            errors.put("birthdate", "Birthdate must be provided.");
        } else {
            // Calculate age in years
            LocalDate today = LocalDate.of(2023, 7, 31);
            LocalDate birthdateLocal = birthdate.toLocalDate();
            int age = today.getYear() - birthdateLocal.getYear();

            if (age < 6 || age > 99) {
                errors.put("birthdate", "Age must be between 6 and 99 years old.");
            }
        }
         //CityId validation
        Integer cityId = dto.getCityId();
        if (cityId == null) {
            errors.put("cityId", "CityId must be provided.");
        } else if (cityId < 1) { // assuming cityId starts from 1
            errors.put("cityId", "CityId must be valid.");
        }

        return errors;
    }

    public static Map<String, String> validate(StudentUpdateDTO dto) {
        Map<String, String> errors = new HashMap<>();

        // Firtsname validations
        if (dto.getFirstname() != null && (dto.getFirstname().length() < 3 || dto.getFirstname().length() > 32)) {
            errors.put("firstname", "size");
        }

        if (dto.getFirstname() != null && dto.getFirstname().matches("^.*\\s+.*$")) {
            errors.put("firstname", "whitespaces");
        }

        // Lastname Validations
        if (dto.getLastname() != null && (dto.getLastname().length() < 3 || dto.getLastname().length() > 32)) {
            errors.put("lastname", "size");
        }

        if (dto.getLastname() != null && dto.getLastname().matches("^.*\\s+.*$")) {
            errors.put("lastname", "whitespaces");
        }

        // Gender Validations
        if (dto.getGender() != null && (dto.getGender().length() != 1 || (!dto.getGender().equalsIgnoreCase("M") && !dto.getGender().equalsIgnoreCase("F")))) {
            errors.put("gender", "Gender must be either 'M' or 'F'.");
        }

        // Birthdate validation
        Date birthdate = dto.getBirthdate();
        if (birthdate != null) {
            // Calculate age in years
            LocalDate today = LocalDate.of(2023, 7, 31);
            LocalDate birthdateLocal = birthdate.toLocalDate();
            int age = today.getYear() - birthdateLocal.getYear();

            if (age < 6 || age > 99) {
                errors.put("birthdate", "Age must be between 6 and 99 years old.");
            }
        }
        // CityId validation
        Integer cityId = dto.getCityId();
        if (cityId != null) {
            if (cityId < 1) { // assuming cityId starts from 1
                errors.put("cityId", "CityId must be valid.");
            }
        }

        return errors;
    }
}