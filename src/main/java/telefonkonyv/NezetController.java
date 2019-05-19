package telefonkonyv;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import main.TelefonKonyv;
import javax.persistence.EntityManager;

@Slf4j
public class NezetController implements Initializable {

    EntityManager em;

    @FXML AnchorPane anchor;

    @FXML SplitPane mainSplit;

    @FXML StackPane menuPane;
    
    @FXML Pane contactPane;
    @FXML Pane newPane;
    @FXML Pane savePane;
    @FXML Pane alertPane;

    @FXML TableView<Person> myTable;

    @FXML TableColumn<Person, String> colFirstName;
    @FXML TableColumn<Person, String> colLastName;
    @FXML TableColumn<Person, String> colPhoneNumber;

    @FXML Button addNewContactButton;
    @FXML Button saveButton;
    @FXML Button alertButton;
    @FXML Button deleteButton;

    @FXML TextField inputLastName;
    @FXML TextField inputFirstName;
    @FXML TextField inputPhoneNumber;
    @FXML TextField inputSaveName;

    @FXML Label alertText;
    
    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_NEW = "Új";
    private final String MENU_SAVE = "Mentés";
    private final String MENU_QUIT = "Kilépés";

    private final ObservableList<Person> data = FXCollections.observableArrayList();

    @FXML
    private void handleAlertButton(ActionEvent event) {
        alertPane.setVisible(false);
        menuPane.setOpacity(1);
        savePane.setOpacity(1);
        newPane.setOpacity(1);
    }
    
    @FXML
    private void saveList(ActionEvent event) {
        String fileName = inputSaveName.getText();
        fileName = fileName.replaceAll("\\s+", "");
        if (fileName != null && !fileName.equals("")) {
            PdfGeneration pdfCreator = new PdfGeneration();
            pdfCreator.pdfGeneration(fileName, data);
        } else {
            menuPane.setOpacity(0.1);
            savePane.setOpacity(0.1);
            alertPane.setVisible(true);
            log.info("Nem lett megadva fájlnév");
        }
        inputSaveName.clear();
    }

    private  void  setTable() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<Person, String>("FirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Person, String>("LastName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Person, String>("PhoneNumber"));

        myTable.setEditable(true);
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        myTable.getItems().addAll();
    }

    public void onEdit(TableColumn.CellEditEvent<Person, String> personStringCellEditEvent) {
        Person person = myTable.getSelectionModel().getSelectedItem();
        person.setFirstName(personStringCellEditEvent.getNewValue());
        person.setLastName(personStringCellEditEvent.getNewValue());
        person.setPhoneNumber(personStringCellEditEvent.getNewValue());
    }

    public void buttonRemove(ActionEvent actionEvent) {
        ObservableList<Person> allPerson, singlePerson;
        allPerson = myTable.getItems();
        singlePerson = myTable.getSelectionModel().getSelectedItems();
        singlePerson.forEach(allPerson::remove);
    }

    private void setMenu() {
        TreeItem<String> treeItemRoot = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(treeItemRoot);
        treeView.setShowRoot(false);
        
        TreeItem<String> contactMenu = new TreeItem<>(MENU_CONTACTS);
        Node quitPicture = new ImageView(new Image(getClass().getResourceAsStream("/pictures/quit.png")));
        TreeItem<String> quitMenu = new TreeItem<>(MENU_QUIT, quitPicture);

        Node listPicture = new ImageView(new Image(getClass().getResourceAsStream("/pictures/contacts.png")));
        TreeItem<String> contactMenuList = new TreeItem<>(MENU_LIST, listPicture);
        Node newPicture = new ImageView(new Image(getClass().getResourceAsStream("/pictures/new.png")));
        TreeItem<String> contactMenuNew = new TreeItem<>(MENU_NEW, newPicture);
        Node savePicture = new ImageView(new Image(getClass().getResourceAsStream("/pictures/save.png")));
        TreeItem<String> contactMenuSave = new TreeItem<>(MENU_SAVE, savePicture);

        contactMenu.getChildren().addAll(contactMenuList, contactMenuNew, contactMenuSave);
        treeItemRoot.getChildren().addAll(contactMenu, quitMenu);
        
        menuPane.getChildren().add(treeView);
        
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu;
                selectedMenu = selectedItem.getValue();
                
                if (null != selectedMenu) {
                    switch (selectedMenu) {
                        case MENU_CONTACTS:
                            selectedItem.setExpanded(true);
                            break;
                        case MENU_LIST:
                            contactPane.setVisible(true);
                            newPane.setVisible(false);
                            savePane.setVisible(false);
                            deleteButton.setVisible(true);
                            break;
                        case MENU_NEW:
                            contactPane.setVisible(false);
                            newPane.setVisible(true);
                            savePane.setVisible(false);
                            break;
                        case MENU_SAVE:
                            contactPane.setVisible(false);
                            newPane.setVisible(false);
                            savePane.setVisible(true);
                            break;
                        case MENU_QUIT:
                            System.exit(0);
                            break;
                    }
                }
            }
        });
}

    @FXML public void addContact(ActionEvent event) {
        String firstName = inputFirstName.getText();
        String lastName = inputLastName.getText();
        String phoneNumber = inputPhoneNumber.getText();
        lastName = lastName.replaceAll("\\s+", "");
        firstName = firstName.replaceAll("\\s+", "");
        phoneNumber = phoneNumber.replaceAll("\\s+", "");
        if (lastName != null && !lastName.equals("") || firstName != null && !firstName.equals("") || phoneNumber != null && !phoneNumber.equals("")) {
          Person newPerson = new Person(firstName, lastName, phoneNumber);
          data.add(newPerson);
          myTable.getItems().add(newPerson);
          em = TelefonKonyv.emf.createEntityManager();
          em.getTransaction().begin();
          em.persist(newPerson);
          em.getTransaction().commit();
          em.close();
        } else {
            newPane.setOpacity(0.1);
            alertPane.setVisible(true);
            log.info("Nem lettek megadva az adatok");
            alertText.setText("Add meg az adatokat!");
        }
        inputLastName.clear();
        inputFirstName.clear();
        inputPhoneNumber.clear();
    }

    public void getContacts() {
        em = TelefonKonyv.emf.createEntityManager();
        List<Person> listEmployee = em.createQuery("FROM Person").getResultList();
        myTable.getItems().addAll(listEmployee);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMenu();
        setTable();
        getContacts();
    }
}
