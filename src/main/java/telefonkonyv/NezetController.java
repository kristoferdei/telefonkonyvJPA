package telefonkonyv;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
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
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NezetController implements Initializable {

    @FXML
    AnchorPane anchor;

    @FXML
    SplitPane mainSplit;

    @FXML
    StackPane menuPane;
    
    @FXML
    Pane contactPane;
    @FXML
    Pane newPane;
    @FXML
    Pane savePane;
    @FXML
    Pane alertPane;

    @FXML
    TableView table;

    @FXML
    Button addNewContactButton;
    @FXML
    Button saveButton;
    @FXML
    Button alertButton;

    @FXML
    TextField inputLastName;
    @FXML
    TextField inputFirstName;
    @FXML
    TextField inputPhoneNumber;
    @FXML
    TextField inputSaveName;

    @FXML
    Label alertText;
    
    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_NEW = "Új";
    private final String MENU_SAVE = "Mentés";
    private final String MENU_QUIT = "Kilépés";

    @Deprecated
    DB db = new DB();

    private final ObservableList<Person> data = FXCollections.observableArrayList();

    @FXML
    private void addContact(ActionEvent event) {
        String lastname = inputLastName.getText();
        String firstname = inputFirstName.getText();
        String phonenumber = inputPhoneNumber.getText();
        lastname = lastname.replaceAll("\\s+", "");
        firstname = firstname.replaceAll("\\s+", "");
        phonenumber = phonenumber.replaceAll("\\s+", "");
        if (lastname != null && !lastname.equals("") || firstname != null && !firstname.equals("") || phonenumber != null && !phonenumber.equals("")) {
            Person newPerson = new Person(inputLastName.getText(), inputFirstName.getText(), inputPhoneNumber.getText());
            data.add(newPerson);
            db.addContact(newPerson);
        } else {
            menuPane.setOpacity(0.1);
            newPane.setOpacity(0.1);
            alertPane.setVisible(true);
            log.info("Nem lettek megadva az adatok");
            alertText.setText("Add meg az adatokat!");
        }
        inputLastName.clear();
        inputFirstName.clear();
        inputPhoneNumber.clear();
    }

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
    
    public void setTable() {
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        lastNameCol.setMinWidth(120);
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        
        lastNameCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setLastName(t.getNewValue());
                    db.updateContact(actualPerson);
                }
            }
        );
        
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(120);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        
        firstNameCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setFirstName(t.getNewValue());
                    db.updateContact(actualPerson);
                }
            }
        );
        
        TableColumn phoneNumberCol = new TableColumn("Telefonszám");
        phoneNumberCol.setMinWidth(130);
        phoneNumberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
        
        phoneNumberCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    actualPerson.setPhoneNumber(t.getNewValue());
                    db.updateContact(actualPerson);
                }
            }
        );
        
        TableColumn removeCol = new TableColumn("Törlés");
        phoneNumberCol.setMinWidth(100);

        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = 
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Person, String> param)
                    {
                        final TableCell<Person, String> cell = new TableCell<Person, String>()
                        {   
                            final Button btn = new Button("Töröl");

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty)
                                {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else
                                {
                                    btn.setOnAction((ActionEvent event) ->
                                            {
                                                Person person = getTableView().getItems().get(getIndex());
                                                data.remove(person);
                                                db.removeContact(person);
                                       } );
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        removeCol.setCellFactory(cellFactory);
        table.getColumns().addAll(lastNameCol, firstNameCol, phoneNumberCol, removeCol);
        data.addAll(db.getAllContacts());
        table.setItems(data);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTable();
        setMenu();
    }
}
