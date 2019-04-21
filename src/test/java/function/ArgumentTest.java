package function;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentTest {
/*
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection = sqLiteJDBC.getConnectionToDatabase();
    Argument argumentForTest = new Argument("abc",4,0,2);

    public boolean compareTwoObject(Discourse d1, Discourse d2){

        return (d1.getId() == d2.getId() && d1.getcontent() == d2.getcontent() && d1.getDiscourse_theme() == d2.getDiscourse_theme());
    }

    @Test
    void getContent() {
        assertEquals("abc",argumentForTest.getContent());
    }

    @Test
    void getDiscourseid() {
        assertEquals(4,argumentForTest.getDiscourseid());
    }

    @Test
    void getstartIndex() {
        assertEquals(0,argumentForTest.getstartIndex());
    }

    @Test
    void getendIndex() {
        assertEquals(2,argumentForTest.getendIndex());
    }

    @Test
    void setContent() {
        Argument argument = new Argument();
        argument.setContent("test");
        assertEquals("test",argument.content);
    }

    @Test
    void setDiscourseid() {
        Argument argument = new Argument();
        argument.setDiscourseid(1);
        assertEquals(1,argument.discourseid);
    }

    @Test
    void setstartIndex() {
        Argument argument = new Argument();
        argument.setstartIndex(0);
        assertEquals(0,argument.startIndex);
    }

    @Test
    void setendIndex() {
        Argument argument = new Argument();
        argument.setendIndex(2);
        assertEquals(2,argument.endIndex);
    }


    @Test
    void getDiscourseById() {
        Argument argument = new Argument();
        Discourse discourse = argument.getDiscourseById(1);
        Discourse expectedDiscourse = Discourse.discourses.get(0);
        assertTrue(this.compareTwoObject(expectedDiscourse,discourse));
    }

    @Test
    void validIndexStartIndexIsNegative() {
        Argument argument = new Argument();
        assertEquals("false",argument.validIndex(1,-1,2)[0]);
        assertEquals("Start indices is wrong",argument.validIndex(1,-1,2)[1]);
    }

    @Test
    void validIndexEndIndexIsGreaterThanUpperboundry() {
        Argument argument = new Argument();
        String[] result = argument.validIndex(1,1,200);
        assertEquals("false",result[0]);
        assertEquals("End indices is wrong",result[1]);
    }

    @Test
    void validIndexStartIndexIsGreaterThanEndIndex() {
        Argument argument = new Argument();
        String[] result = argument.validIndex(1,10,1);
        assertEquals("false",result[0]);
        assertEquals("Indices is wrong",result[1]);
    }



    @Test
    void validExistInDatabase() {
        Argument argument = new Argument();
        assertTrue(argument.validExistInDatabase(2,0,1,connection));
    }

    @Test
    void validNotExistInDatabase() {
        Argument argument = new Argument();
        assertFalse(argument.validExistInDatabase(1,0,1,connection));
    }

    @Test
    void createArgument() {
        Argument argument = new Argument();
        String[] result = argument.createArgument(1,0,1,connection);
        assertEquals("true",result[0]);
        assertEquals("Success",result[1]);
    }

    @Test
    void createArgumentFail() {
        Argument argument = new Argument();
        String[] result = argument.createArgument(2,0,1,connection);
        assertEquals("false",result[0]);
        assertEquals("Fail",result[1]);
    }
    */
}