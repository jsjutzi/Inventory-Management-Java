import Model.InHousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Product;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View_Controller/MainScreen.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        // Add all dummy data here
        Inventory.addPart(new InHousePart(0, "washer", 2.00, 2, 0, 5, 1));
        Inventory.addPart(new InHousePart(1, "bolt", 1.21, 1, 0, 99, 2));
        Inventory.addPart(new OutsourcedPart(2, "screw", 0.50, 2, 0, 22, "Michigan Inc"));

        Inventory.addProduct(new Product(Inventory.getAllParts(), 0, "Toolkit", 2.11, 3, 1, 99));
        Inventory.addProduct(new Product(Inventory.getAllParts(), 1, "Ceiling Fan", 1.00, 9, 1, 50));
        Inventory.addProduct(new Product(Inventory.getAllParts(), 2, "Fitting", 0.80, 4, 1, 60));

        launch(args);
    }
}