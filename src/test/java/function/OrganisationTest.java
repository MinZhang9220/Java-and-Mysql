package function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
class OrganisationTest {

    static SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    static  Connection connection;
    static Organisation organisation = new Organisation();

    @BeforeAll
    static void initConnection(){
        connection = sqLiteJDBC.getConnectionToDatabase();
    }

    @Test
    @Order(1)
    void setOrganisationName() {
        organisation.setOrganisationName("Digitwin");
        assertEquals("Digitwin",organisation.organisationName);
    }

    @Test
    @Order(2)
    void getOrganisationName() {
        Organisation organisation = new Organisation("Pecking university");
        assertEquals("Pecking university",organisation.getOrganisationName());
    }


    @Test
    void notValidOrganisationName() {

        String ExistedOrganisationName = "University of canterbury";
        assertFalse(organisation.validOrganisationName(ExistedOrganisationName,connection));
    }

    @Test
    void validOrganisationName() {

        String NotExistedOrganisationName = "University of Tesihua";
        assertTrue(organisation.validOrganisationName(NotExistedOrganisationName,connection));
    }

    @Test
    @Order(4)
    void createOrganisation() {
        String NotExistedOrganisationName = "University of Beijing";
        String[] resultWithNotExistedOrganisationName = organisation.createOrganisation(NotExistedOrganisationName,connection);
        assertEquals("true",resultWithNotExistedOrganisationName[0]);
        assertEquals("Success",resultWithNotExistedOrganisationName[1]);
    }

    @Test

    void createOrganisationFail() {

        String ExistedOrganisationName = "University of canterbury";
        String[] resultWithExistedOrganisationName = organisation.createOrganisation(ExistedOrganisationName,connection);
        assertEquals("false",resultWithExistedOrganisationName[0]);
        assertEquals("Fail",resultWithExistedOrganisationName[1]);
    }
}