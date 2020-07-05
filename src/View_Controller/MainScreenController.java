package View_Controller;
import Model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class MainScreenController {
    // Set up for Parts Table
    @FXML
    TextField searchTermParts;

    @FXML
    TextField searchTermProducts;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> partTableView;

    // Set up for Products Table

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productStockCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableView<Product> productsTableView;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public void initialize() {
        // Populate parts table
        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPriceCol.setCellFactory(price -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        // Populate products table
        productsTableView.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPriceCol.setCellFactory(price -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    @FXML
    public void addModifyPartScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddModifyPart.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        AddModifyPartController controller = loader.getController();
        // Check to see if modify screen is needed
        String buttonClicked = ((Button)event.getSource()).getText();

        if (buttonClicked.equals("Modify")) {
            String classType =  partTableView.getSelectionModel().getSelectedItem().getClass().getSimpleName();
            int index = partTableView.getSelectionModel().getSelectedIndex();

            if (classType.equals("OutsourcedPart")) {
                Outsourced part = (Outsourced) partTableView.getSelectionModel().getSelectedItem();
                controller.loadSelectedOutsourcedPart(part, index);
            } else {
                InHouse part = (InHouse) partTableView.getSelectionModel().getSelectedItem();
                controller.loadSelectedInHousePart(part, index);
            }
        }

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void onDeletePart() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        //Confirm Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Part Deletion");
        alert.setContentText("Are you sure you want to delete " + selectedPart.getName()  + " now ?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> Inventory.deletePart(selectedPart) );
    }

    public void onDeleteProduct() {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

        //Confirm Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Product Deletion");
        alert.setContentText("Are you sure you want to delete " + selectedProduct.getName()  + " now ?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> Inventory.deleteProduct(selectedProduct));
    }

    public void onSearchParts() {
        String term = searchTermParts.getText();
        boolean isNumeric = Inventory.isInteger(term);
        ObservableList<Part> results;

        if (isNumeric) {
            results = Inventory.lookupPart(Integer.parseInt(term));
        } else {
            results = Inventory.lookupPart(term);
        }

        if (results.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Results");
            alert.setHeaderText("No Results Found");
            alert.setContentText("Either no results were found for the search term or you have entered a blank search term, please try again. List is being reset.");
            alert.showAndWait();
            partTableView.setItems(Inventory.getAllParts());
        } else {
            partTableView.setItems(results);
        }
    }

    public void onSearchProducts() {
        String term = searchTermProducts.getText();
        boolean isNumeric = Inventory.isInteger(term);
        ObservableList<Product> results;

        if (isNumeric) {
            results = Inventory.lookupProduct(Integer.parseInt(term));
        } else {
            results = Inventory.lookupProduct(term);
        }

        if (results.size() < 1 || term.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Results");
            alert.setHeaderText("No Results Found");
            alert.setContentText("Either no results were found for the search term or you have entered a blank search term, please try again. List is being reset.");
            alert.showAndWait();
            productsTableView.setItems(Inventory.getAllProducts());
        } else {
            productsTableView.setItems(results);
        }
    }

    public void addModifyProductScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddModifyProduct.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        AddModifyProductController controller = loader.getController();

        // Check to see if modify screen is needed
        String buttonClicked = ((Button)event.getSource()).getText();

        if (buttonClicked.equals("Modify")) {
            int index = productsTableView.getSelectionModel().getSelectedIndex();
            Product product = productsTableView.getSelectionModel().getSelectedItem();

            controller.loadSelectedProduct(product, index);
        }

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    // Exit Program
    public void exitProgram() {
        System.exit(0);
    }
}
