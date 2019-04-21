package models;

import datahandling.AffiliationRepository;
import datahandling.ArgumentRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Affiliation {

    private Actor actor;
    private Organisation organisation;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Affiliation(){}

    public boolean insertAffiliationIntoDatabase(AffiliationRepository affiliationRepository){
        return affiliationRepository.insertAffiliation(this);
    }

    public boolean isValidDate(String date){
        if(date.isEmpty()){
            return true;
        }
        else {
            try {
                LocalDate.parse(date, formatter);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }
    }

    public boolean isValidStartEndDate(String startDateString, String endDateString){
        if(startDateString.isEmpty() || endDateString.isEmpty()){
            return true;
        }
        else {
            try {
                LocalDate startDate = LocalDate.parse(startDateString, formatter);
                LocalDate endDate = LocalDate.parse(endDateString, formatter);
                return startDate.isBefore(endDate) || startDate.isEqual(endDate);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getRole() {
        if(role == null){
            return null;
        }
        else{
            return role;
        }
    }

    public boolean setRole(String role) {
        if(role.isEmpty()){
            this.role = null;
            return true;
        }
        else if(role.length() > 100){
            return false;
        }
        else{
            this.role = role;
            return true;
        }
    }

    public String getStartDateString() {
        if(startDate == null){
            return null;
        }
        else {
            String startDateString = startDate.format(formatter);
            return startDateString;
        }
    }

    public void setStartDate(String startDateString) {
        if(startDateString.isEmpty()){
            this.startDate = null;
        }
        else {
            try {
                LocalDate startDate = LocalDate.parse(startDateString, formatter);
                this.startDate = startDate;
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEndDateString() {
        if(endDate == null){
            return null;
        }
        else {
            String endDateString = endDate.format(formatter);
            return endDateString;
        }
    }

    public void setEndDate(String endDateString) {
        if(endDateString.isEmpty()){
            this.endDate = null;
        }
        else {
            try {
                LocalDate endDate = LocalDate.parse(endDateString, formatter);
                this.endDate = endDate;
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
    }
}
