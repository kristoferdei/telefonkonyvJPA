package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TelefonKonyv extends Application {

    public static EntityManagerFactory emf;
    
    @Override
    public void start(Stage stage) throws Exception {

        emf = Persistence.createEntityManagerFactory("jpa-persistence");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Nezet.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.getIcons().add(new Image("/pictures/appicon.png"));
        stage.setTitle("Telefonkönyv");
        stage.setResizable(false);
        stage.setWidth(605);
        stage.setHeight(680);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            emf.close();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
