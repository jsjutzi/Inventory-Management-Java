package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class AddModifyPartController {
    private BooleanProperty showInHousePart = new SimpleBooleanProperty(true);
    private BooleanProperty showOutsourcedPart = new SimpleBooleanProperty(false);
    private boolean isModifyScreen = false;
    private int modifyIndex = -1;

    @FXML
    Label partTitle;

    @FXML
    RadioButton inHouseRadio;

    @FXML
    RadioButton outsourcedRadio;

    @FXML
    Label companyNameLabel;

    @FXML
    TextField companyNameTextField;

    @FXML
    Label machineIdLabel;

    @FXML
    TextField machineIdTextField;

    @FXML
    TextField partIdTextField;

    @FXML
    TextField partNameTextField;

    @FXML
    TextField partStockTextField;

    @FXML
    TextField partPriceTextField;

    @FXML
    TextField partMaxTextField;

    @FXML
    TextField partMinTextField;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public void loadSelectedInHousePart(InHouse part, int index) {
        // Indicate Modify Screen
        isModifyScreen = true;
        modifyIndex = index;

        // Update Title
        partTitle.setText("Modify Part");
        inHouseRadio.setSelected(true);

        // Initialize all values to selected part values
        partIdTextField.setText(Integer.toString(part.getId()));
        partNameTextField.setText(part.getName());
        partStockTextField.setText(Integer.toString(part.getStock()));
        partPriceTextField.setText(currencyFormat.format((part.getPrice())));
        partMaxTextField.setText(Integer.toString(part.getMax()));
        partMinTextField.setText(Integer.toString(part.getMin()));
        machineIdTextField.setText(Integer.toString(part.getMachineId()));
    }

    public void loadSelectedOutsourcedPart(Outsourced part, int index) {
        // Indicate Modify Screen
        isModifyScreen = true;
        modifyIndex = index;

        // Update Title & Part Type
        partTitle.setText("Modify Part");
        showOutsourcedPart.setValue(true);
        showInHousePart.setValue(false);
        outsourcedRadio.setSelected(true);

        // Initialize all values to selected part values
        partIdTextField.setText(Integer.toString(part.getId()));
        partNameTextField.setText(part.getName());
        partStockTextField.setText(Integer.toString(part.getStock()));
        partPriceTextField.setText(currencyFormat.format((part.getPrice())));
        partMaxTextField.setText(Integer.toString(part.getMax()));
        partMinTextField.setText(Integer.toString(part.getMin()));
        companyNameTextField.setText(part.getCompanyName());
    }

    @FXML
    public void returnToHomeScreen(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void changePartType() {
            showInHousePart.set(!showInHousePart.get());
            showOutsourcedPart.set(!showOutsourcedPart.get());
    }


    public void initialize() {
        // Bind boolean values for conditional Rendering
        machineIdLabel.visibleProperty().bind(showInHousePart);
        machineIdTextField.visibleProperty().bind(showInHousePart);

        companyNameLabel.visibleProperty().bind(showOutsourcedPart);
        companyNameTextField.visibleProperty().bind(showOutsourcedPart);

        // Generate Id for new part and disable field

        partIdTextField.setText(Inventory.generateNewId("part"));
        partIdTextField.setEditable(false);

    }

    public void savePart(ActionEvent event) throws IOException {
        int id = Integer.parseInt(partIdTextField.getText());
        String name = partNameTextField.getText();
        double price = Double.parseDouble(partPriceTextField.getText().replace("$", ""));
        int stock = Integer.parseInt(partStockTextField.getText());
        int min = Integer.parseInt(partMinTextField.getText());
        int max = Integer.parseInt(partMaxTextField.getText());

        // Add Form Validation Here




        if (showInHousePart.get()) {
            // Add or Modify in house part
            int machineId = Integer.parseInt(machineIdTextField.getText());
            InHouse newPart = new InHouse(id, name, price, stock, min, max, machineId);
            if (isModifyScreen) {
                Inventory.updatePart(modifyIndex, newPart);
            } else {
                Inventory.addPart(newPart);
            }
        } else {
            // Add or Modify outsourced part
            String companyName = companyNameTextField.getText();
            Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            if (isModifyScreen) {
                Inventory.updatePart(modifyIndex, newPart);
            } else {
                Inventory.addPart(newPart);
            }
        }

        this.returnToHomeScreen(event);
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
