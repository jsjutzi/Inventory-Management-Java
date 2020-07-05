package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
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

public class AddModifyProductController {
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    private boolean isModifyScreen = false;
    private Product selectedProduct;

    @FXML
    TableView allPartsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    TableView productPartsTableView;

    @FXML
    private TableColumn<Part, Integer> productPartIdCol;

    @FXML
    private TableColumn<Part, String> productPartNameCol;

    @FXML
    private TableColumn<Part, Integer> productPartStockCol;

    @FXML
    private TableColumn<Part, Double> productPartPriceCol;

    @FXML
    private TextField productId;

    @FXML
    private TextField productName;

    @FXML
    private TextField productStock;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField searchTermParts;

    @FXML
    private Label productTitle;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public void returnToHomeScreen(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void loadSelectedProduct(Product product, int index) {
        productTitle.setText("Modify Product");
        isModifyScreen = true;
        selectedProduct = product;

        //Pre-Load product values
        productId.setText(Integer.toString(product.getId()));
        productName.setText(product.getName());
        productStock.setText(Integer.toString(product.getStock()));
        productPrice.setText(currencyFormat.format((product.getPrice())));
        productMin.setText(Integer.toString(product.getMin()));
        productMax.setText(Integer.toString(product.getMin()));

        productPartsTableView.setItems(product.getAllAssociatedParts());
        productParts.addAll(product.getAllAssociatedParts());
        System.out.println(productParts);


    }

    public void initialize() {
        allPartsTableView.setItems(Inventory.getAllParts());

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

        productId.setText(Inventory.generateNewId("product"));
        productId.setEditable(false);


        productPartsTableView.setItems(productParts);

        productPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPartPriceCol.setCellFactory(price -> new TableCell<Part, Double>() {
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

    public void onAddPartToProduct() {
        Part newPart = (Part) allPartsTableView.getSelectionModel().getSelectedItem();
        productParts.add(newPart);
        productPartsTableView.setItems(productParts);
    }

    public void onDeletePartFromProduct() {
        Part selectedPart = (Part) productPartsTableView.getSelectionModel().getSelectedItem();

        // Confirm Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Part Deletion");
        alert.setContentText("Are you sure you want to delete " + selectedPart.getName()  + " now ?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> productParts.remove(selectedPart));

        productPartsTableView.setItems(productParts);
    }

    public void onSaveProduct(ActionEvent event) throws IOException {

        if (productParts.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SAVE ERROR");
            alert.setHeaderText("Product has no parts");
            alert.setContentText("A product must have at least one part associated with it.");
            alert.showAndWait();
        } else {
            int id = Integer.parseInt(productId.getText());
            String name = productName.getText();
            int stock = Integer.parseInt(productStock.getText());
            double price = Double.parseDouble(productPrice.getText().replace("$", ""));
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());

            if (isModifyScreen) {
                selectedProduct.setName(name);
                selectedProduct.setStock(stock);
                selectedProduct.setPrice(price);
                selectedProduct.setMin(min);
                selectedProduct.setMax(max);
                selectedProduct.setAssociatedParts(productParts);
            } else {
                Product newProduct = new Product(productParts, id, name, price, stock, min, max);
                Inventory.addProduct(newProduct);
            }
            returnToHomeScreen(event);
        }
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

        if (results.size() < 1 || term.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Results");
            alert.setHeaderText("No Results Found");
            alert.setContentText("Either no results were found for the search term or a blank search term was entered, please try again. List is being reset.");
            alert.showAndWait();
            allPartsTableView.setItems(Inventory.getAllParts());
        } else {
            allPartsTableView.setItems(results);
        }

    }

    public void onCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setContentText("Are you sure you want to return to home screen? All unsaved data will be lost.");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        returnToHomeScreen(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
