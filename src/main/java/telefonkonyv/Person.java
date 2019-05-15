package telefonkonyv;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty id;
    
    public Person() {
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.id = new SimpleStringProperty("");
    }
    
    public Person(String lName, String fName, String pNumber) {
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.phoneNumber = new SimpleStringProperty(pNumber);
        this.id = new SimpleStringProperty("");
    }
    
    public Person(Integer id, String lName, String fName, String pNumber) {
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.phoneNumber = new SimpleStringProperty(pNumber);
        this.id = new SimpleStringProperty(String.valueOf(id));
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public void setLastName(String fName) {
        lastName.set(fName);
    }
    
    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    
    public void setPhoneNumber(String fName) {
        phoneNumber.set(fName);
    } 
    
    public String getId() {
        return id.get();
    }
    
    public void setId(String fId) {
        id.set(fId);
    }
}
