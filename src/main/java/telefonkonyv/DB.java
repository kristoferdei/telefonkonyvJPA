package telefonkonyv;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Adatbázis osztály
 */
public class DB {
    
    final String URL = "jdbc:derby:sampleDB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";

    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;

    /**
     * Az adatbázis létrehozása
     */
    @Deprecated
    public DB() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(URL);
            System.out.println("A kapcsolat létrejött");
        } catch (Exception ex) {
            System.out.println("Valami baj van a kapcsolat létrehozásakor.");
            System.out.println(""+ex);
        }

        if (conn != null){
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("createStatament error");
                System.out.println(""+ex);
            }
        }

        try {           
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("DatabaseMetaDat error");
            System.out.println(""+ex);
        }
        
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "CONTACTS", null);
            if(!rs.next())
            { 
             createStatement.execute("create table contacts(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),lastname varchar(20), firstname varchar(20), phonenumber varchar(20))");
            }
        } catch (SQLException ex) {
            System.out.println("Adattábla hiba");
            System.out.println(""+ex);
        }       
    }

    /**
     * Az összes kontakt elérése
     * @return a kontaktok listáját
     */
    public ArrayList<Person> getAllContacts(){
        String sql = "select * from contacts";
        ArrayList<Person> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<>();
            
            while (rs.next()){
                Person actualPerson = new Person(rs.getInt("id"),rs.getString("lastname"),rs.getString("firstname"),rs.getString("phonenumber"));
                users.add(actualPerson);
            }
        } catch (SQLException ex) {
            System.out.println("read contact error");
            System.out.println(""+ex);
        }
      return users;
    }

    /**
     * Új kontakt hozzáadása
     * @param person a hozzáadni kivánt kontakt
     */
    public void addContact(Person person){
      try {
        String sql = "insert into contacts (lastname, firstname, phonenumber) values (?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, person.getLastName());
        preparedStatement.setString(2, person.getFirstName());
        preparedStatement.setString(3, person.getPhoneNumber());
        preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("add contact error");
            System.out.println(""+ex);
        }
    }

    /**
     * Kontakt adatainak frissítése
     * @param person a frissíteni kívánt kontakt
     */
    public void updateContact(Person person){
      try {
            String sql = "update contacts set lastname = ?, firstname = ? , phonenumber = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getLastName());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getPhoneNumber());
            preparedStatement.setInt(4, Integer.parseInt(person.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("update contact error");
            System.out.println(""+ex);
        }
    }

    /**
     * Kontakt törlése a listából
     * @param person a törölni kívánt személy
     */
    public void removeContact(Person person){
      try {
            String sql = "delete from contacts where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(person.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("delete contact error");
            System.out.println(""+ex);
        }
    }
    
}