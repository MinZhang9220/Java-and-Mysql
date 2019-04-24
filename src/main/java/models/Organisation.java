/**
 * Organisation model
 *
 */

package models;

import datahandling.OrganisationRepository;


public class Organisation {

    /**
     * Constructor for creating an organisation
     * @param organisationName the organisation's name
     */
    public Organisation(String organisationName){
        this.organisationName = organisationName;
    }

    /**
     * The name of the organisation
     */
    private String organisationName;

    /**
     * Method that gets the name of the organisation
     * @return the organisation's name
     */
    public String getOrganisationName(){
        return this.organisationName;
    }

    /**
     * Checks if the organisation's name is valid.
     * An organisation's name is valid if it isn't empty
     * and it isn't already in the database.
     * @param organisationRepository the repository used to make queries to the database
     * @return true if the organisation's name is valid, false if it's not valid
     */
    public boolean isValidOrganisationName(OrganisationRepository organisationRepository){
        if(organisationName.isEmpty()){
            return false;
        }
        else if(organisationRepository.isDuplicateOrganisation(organisationName)){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Create an organisation if the organisation name is valid.
     * @return true if the organisation was successfully inserted, false otherwise
     */
    public boolean insertOrganisation(OrganisationRepository organisationRepository){
        if(organisationRepository.insertOrganisation(organisationName)){
            return true;
        }
        else{
            return false;
        }
    }

}

