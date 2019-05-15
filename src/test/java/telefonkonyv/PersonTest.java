package telefonkonyv;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    public void lNameTest() throws Exception {
        Person testPerson = new Person("Minta" ,"Pelda" ,"06301234567");
        assertEquals("Minta", testPerson.getLastName());
        assertNotEquals("Proba", testPerson.getLastName());
    }

    @Test
    public void fNameTest() throws Exception {
        Person testPerson = new Person("Minta" ,"Pelda" ,"06301234567");
        assertEquals("Pelda", testPerson.getFirstName());
        assertNotEquals("Proba", testPerson.getFirstName());
    }

    @Test
    public void pNumberTest() throws Exception {
        Person testPerson = new Person("Minta" ,"Pelda" ,"06301234567");
        assertEquals("06301234567", testPerson.getPhoneNumber());
        assertNotEquals("00209876543", testPerson.getPhoneNumber());
    }

}