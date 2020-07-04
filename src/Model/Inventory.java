package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Inventory {
    // Lists for all parts and products
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }

    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }

    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> filteredParts = allParts.stream().filter(part -> part.getName().equals(partName)).collect(Collector.of(
                FXCollections::observableArrayList,
                ObservableList::add,
                (l1, l2) -> { l1.addAll(l2); return l1; })
        );

        return filteredParts;
    }

    public static ObservableList<Part> lookupPart(int partId) {
        ObservableList<Part> filteredParts = allParts.stream().filter(part -> part.getId() == partId).collect(Collector.of(
                FXCollections::observableArrayList,
                ObservableList::add,
                (l1, l2) -> { l1.addAll(l2); return l1; })
        );

        return filteredParts;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> filteredProducts = allProducts.stream().filter(product -> product.getName().equals(productName)).collect(Collector.of(
                FXCollections::observableArrayList,
                ObservableList::add,
                (l1, l2) -> { l1.addAll(l2); return l1; })
        );

        return filteredProducts;
    }

    public static ObservableList<Product> lookupProduct(int productId) {
        ObservableList<Product> filteredProducts = allProducts.stream().filter(product -> product.getId() == productId).collect(Collector.of(
                FXCollections::observableArrayList,
                ObservableList::add,
                (l1, l2) -> { l1.addAll(l2); return l1; })
        );

        return filteredProducts;
    }

    public static String generateNewId(String type) {
        int newId;
        if (type == "part") {
            newId = allParts.get(allParts.size() - 1).getId() + 1;
        } else {
            newId = allProducts.get(allProducts.size() - 1).getId() + 1;
        }
        return Integer.toString(newId);
    }

    public static boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

}
