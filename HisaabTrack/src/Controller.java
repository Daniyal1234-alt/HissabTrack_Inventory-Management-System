import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class Controller {
    //main layout
    private VBox mainLayout;
    // Title box
    private HBox titleBox;

    // sign up box
    private HBox signUpBox;

    //log in 
    private HBox loginBox;

    //dashboard
    private HBox dashBoard;

    // root
    private StackPane root;

    // content
    private HBox contentArea;

    // system
    private HisaabTrack system;

    // type
    private String userType;
    
    // logged user
    private Admin loggedAdmin;
    private Supplier loggedSupplier;
    private InventoryManager loggedManager;

    Controller(HisaabTrack sys){
        this.system = sys;
    }

    public Scene openApp() {
        Scene scene = createLoginPage();
        return scene;
    }

    private Scene createLoginPage() {
        Image gifBackground = new Image("forklift.gif", 1280, 720, false, true);
        ImageView gifBackgroundView = new ImageView(gifBackground);

        // Background configuration
        gifBackgroundView.setFitWidth(1280);
        gifBackgroundView.setFitHeight(720);
        gifBackgroundView.setPreserveRatio(true);
        gifBackgroundView.setEffect(new GaussianBlur(15));

        // Title box
        titleBox = createTitleBox();

        // Login box
        loginBox = createLoginBox();

        // Circle groups
        Group circleGroup = createCircleGroup();
        Group mirroredCircleGroup = createMirroredCircleGroup();

        // Main layout
        mainLayout = new VBox(25);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(10, 20, 10, 20));
        mainLayout.getChildren().addAll(titleBox, loginBox);

        // StackPane to combine all elements
        root = new StackPane(gifBackgroundView, mainLayout, circleGroup, mirroredCircleGroup);
        StackPane.setAlignment(circleGroup, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(mirroredCircleGroup, Pos.BOTTOM_LEFT);
        StackPane.setMargin(circleGroup,new Insets(20));
        StackPane.setMargin(mirroredCircleGroup,new Insets(20));

        return new Scene(root, 1280, 720);
    }

    private void navigateToBox(HBox prev,HBox next) {

        // Create TranslateTransition for moving the login box to the left
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), prev);
        transition.setByX(-1280); // Move left by the width of the scene
        transition.setInterpolator(Interpolator.EASE_IN);
    
        // After the animation, clear the center of the scene
        transition.setOnFinished(event -> {
            mainLayout.getChildren().remove(prev); // Remove the loginBox
            mainLayout.getChildren().add(next); // Add the sign-up box
        });
    
        // Start the animation
        transition.play();       
        // Animation to slide the sign-up box in from the right
        TranslateTransition signUpBoxIn = new TranslateTransition(Duration.seconds(0.4), next);
        signUpBoxIn.setFromX(1280); // Start from outside the right of the screen
        signUpBoxIn.setToX(0);     // End at the center
        signUpBoxIn.setInterpolator(Interpolator.EASE_IN);

        // Play both animations sequentially
        signUpBoxIn.play();
    }
    private HBox createAdminDashboard() {
        // Left panel with buttons
        VBox buttonPanel = createAdminButtonPanel();
        //titleBox = createTitleBoxLoggedIn();

        // Central content area
        HBox contentArea = createContentArea();
        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        Rectangle leftSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        Rectangle rightSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        // Combine the button panel and content area into the main dashboard layout
        dashBoard = new HBox();
        dashBoard.setSpacing(20);
        dashBoard.setPadding(new Insets(20));
        dashBoard.getChildren().addAll(leftSpacer,buttonPanel, contentArea,rightSpacer);
        dashBoard.setEffect(shadow);
        dashBoard.setAlignment(Pos.TOP_CENTER);
        buttonPanel.setOnMouseEntered(event -> {
            buttonPanel.setScaleX(1.02);
            buttonPanel.setScaleY(1.02);
        });

        buttonPanel.setOnMouseExited(event -> {
            buttonPanel.setScaleX(1.0);
            buttonPanel.setScaleY(1.0);
        });
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
        return dashBoard;
    }
    private HBox createSupplierDashboard() {
        // Left panel with buttons
        VBox buttonPanel = createSupplierButtonPanel();
        //titleBox = createTitleBoxLoggedIn();

        // Central content area
        HBox contentArea = createContentArea();
        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        Rectangle leftSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        Rectangle rightSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        // Combine the button panel and content area into the main dashboard layout
        dashBoard = new HBox();
        dashBoard.setSpacing(20);
        dashBoard.setPadding(new Insets(20));
        dashBoard.getChildren().addAll(leftSpacer,buttonPanel, contentArea,rightSpacer);
        dashBoard.setEffect(shadow);
        dashBoard.setAlignment(Pos.TOP_CENTER);
        buttonPanel.setOnMouseEntered(event -> {
            buttonPanel.setScaleX(1.02);
            buttonPanel.setScaleY(1.02);
        });

        buttonPanel.setOnMouseExited(event -> {
            buttonPanel.setScaleX(1.0);
            buttonPanel.setScaleY(1.0);
        });
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
        return dashBoard;
    }
    private HBox createInventoryManagerDashboard() {
        // Left panel with buttons
        VBox buttonPanel = createInventoryManagerButtonPanel();
        //titleBox = createTitleBoxLoggedIn();

        // Central content area
        HBox contentArea = createContentArea();
        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        Rectangle leftSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        Rectangle rightSpacer = new Rectangle(10, 0, Color.TRANSPARENT);
        // Combine the button panel and content area into the main dashboard layout
        dashBoard = new HBox();
        dashBoard.setSpacing(20);
        dashBoard.setPadding(new Insets(20));
        dashBoard.getChildren().addAll(leftSpacer,buttonPanel, contentArea,rightSpacer);
        dashBoard.setEffect(shadow);
        dashBoard.setAlignment(Pos.TOP_CENTER);
        buttonPanel.setOnMouseEntered(event -> {
            buttonPanel.setScaleX(1.02);
            buttonPanel.setScaleY(1.02);
        });

        buttonPanel.setOnMouseExited(event -> {
            buttonPanel.setScaleX(1.0);
            buttonPanel.setScaleY(1.0);
        });
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
        return dashBoard;
    }

    private void addInventoryManagerContent(){
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        //
        // Create a VBox to hold the input fields and the button
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);

        // Create input fields for manager information
        Text nameText = createText("Name:");
        TextField nameField = createTextField("Enter Manager name");
        Text cnicText = createText("CNIC:");
        TextField cnicField = createTextField("Enter Manager CNIC");
        Text addressText = createText("Address:");
        TextField addressField = createTextField("Enter Manager Address");
        PasswordField passwordField = createPasswordField("Password");
        Text storeText = createText("Store:");
        ComboBox<String> storeComboBox = new ComboBox<>();
        storeComboBox.setPromptText("Select a Store");
        // Populate the ComboBox with stores from the system
        List<Store> storeList = system.getStores();
        for (Store store : storeList) {
            if(store.getManagerID() == -1){
                String storeEntry = store.getStoreID() + " - " + store.getLocation();
                storeComboBox.getItems().add(storeEntry);
            }
        }
        // Add Manager Button
        Button addButton = createStyledButton("Add Manager", "#e41837", "#541801");

        addButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String cnic = cnicField.getText().trim();
            String address = addressField.getText().trim();
            String password = passwordField.getText().trim();
            String selectedStore = storeComboBox.getValue();
            if (name.isEmpty() || cnic.isEmpty() || address.isEmpty() || selectedStore == null || passwordField == null) {
                // Display error if fields are empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in all fields and select a store.");
                alert.showAndWait();
            } else {
                // Assume a method `getStoreByName` to fetch the store object
                int spaceIndex = selectedStore.indexOf(" "); // Find the index of the first space
                Store store = system.getStore(Integer.parseInt(selectedStore.substring(0, spaceIndex)));
                system.addManager(loggedAdmin.getAdminID(), 0, name, cnic, address, password, store, false);

                // Display success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Manager added successfully!");
                alert.showAndWait();
                storeComboBox.getItems().remove(selectedStore);
                // Clear fields after adding the manager
                nameField.clear();
                cnicField.clear();
                addressField.clear();
                storeComboBox.getSelectionModel().clearSelection();
            }
        });

        // Add all fields and button to the form area
        formArea.getChildren().addAll(nameText, nameField, cnicText, cnicField, addressText, addressField, storeText, storeComboBox, passwordField, addButton);
        contentArea.getChildren().add(formArea);
        //
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2,contentArea);
    }
    private void removeInventoryManagerContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );

        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);

        // Dropdown for selecting managers
        Text selectManagerText = createText("Select Manager:");

        ComboBox<String> managerComboBox = new ComboBox<>();
        managerComboBox.setPromptText("Select a Manager");
        managerComboBox.setPrefWidth(300);

        // Populate the ComboBox with manager details
        List<InventoryManager> managerList = system.getAdminsInventoryManagers(loggedAdmin.getAdminID());
        if (managerList.isEmpty()) {
            managerComboBox.setPromptText("No Managers Found");
            managerComboBox.setDisable(true);
        } else {
            for (InventoryManager manager : managerList) {
                String managerEntry = manager.getManagerID() + " - " + manager.getName();
                managerComboBox.getItems().add(managerEntry);
            }
        }

        // Remove Button
        Button removeButton = createStyledButton("Remove Manager", "#e41837", "#541801");

        removeButton.setOnAction(event -> {
            String selectedManager = managerComboBox.getValue();
            if (selectedManager == null) {
                // Display error if no manager is selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Manager Selected");
                alert.setContentText("Please select a manager to remove.");
                alert.showAndWait();
            } 
            else {
                int spaceIndex = selectedManager.indexOf(" "); // Find the index of the first space
                InventoryManager selectedMan = system.getManagerByID(Integer.parseInt(selectedManager.substring(0,spaceIndex)));
                boolean isRemoved = system.removeManager(loggedAdmin.getAdminID(), selectedMan.getManagerID());
                if (isRemoved) {
                    // Display success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Manager removed successfully!");
                    alert.showAndWait();

                    // Update the ComboBox after removing the manager
                    managerComboBox.getItems().remove(selectedManager);
                    if (managerComboBox.getItems().isEmpty()) {
                        managerComboBox.setPromptText("No Managers Found");
                        managerComboBox.setDisable(true);
                    }
                } else {
                    // Display failure message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Removal Failed");
                    alert.setContentText("Failed to remove the manager. Please ensure the selection is valid.");
                    alert.showAndWait();
                }
            }
        });

        // Add all components to the form area
        formArea.getChildren().addAll(selectManagerText, managerComboBox, removeButton);
        contentArea.getChildren().add(formArea);

        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });

        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void updateInventoryManagerContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Dropdown for selecting managers
        Text selectManagerText = createText("Select Manager:");
    
        ComboBox<String> managerComboBox = new ComboBox<>();
        managerComboBox.setPromptText("Select a Manager");
        managerComboBox.setPrefWidth(300);
    
        // Populate the ComboBox with manager details
        List<InventoryManager> managerList = system.getAdminsInventoryManagers(loggedAdmin.getAdminID());
        if (managerList.isEmpty()) {
            managerComboBox.setPromptText("No Managers Found");
            managerComboBox.setDisable(true);
        } else {
            for (InventoryManager manager : managerList) {
                String managerEntry = manager.getManagerID() + " - " + manager.getName();
                managerComboBox.getItems().add(managerEntry);
            }
        }
    
        // Input fields for updating details
        TextField nameField = createTextField("Enter New Name");
    
        TextField cnicField = createTextField("Enter New CNIC");
    
        TextField addressField = createTextField("Enter New Address");
        
        // Update Button
        Button updateButton = createStyledButton("Update Manager", "#e41837", "#541801");
        HBox selectBox = new HBox();
        Button selectButton = createStyledButton("Select", "#e41837", "#541801");
        selectButton.setOnAction(event ->{
            String selectedManager = managerComboBox.getValue();
            int spaceIndex = selectedManager.indexOf(" "); // Extract Manager ID
            int managerID = Integer.parseInt(selectedManager.substring(0, spaceIndex));
            InventoryManager selectedMan = system.getManagerByID(managerID);
            nameField.setPromptText(selectedMan.getName() + " (Enter New Name)");
            cnicField.setPromptText(selectedMan.getCNIC() + " (Enter New CNIC)");
            addressField.setPromptText(selectedMan.getAddress() + " (Enter New Address)");
        });
        selectBox.getChildren().addAll(managerComboBox,selectButton);

        updateButton.setOnAction(event -> {
            String selectedManager = managerComboBox.getValue();
            if (selectedManager == null) {
                // Display error if no manager is selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Manager Selected");
                alert.setContentText("Please select a manager to update.");
                alert.showAndWait();
            } else {
                int spaceIndex = selectedManager.indexOf(" "); // Extract Manager ID
                int managerID = Integer.parseInt(selectedManager.substring(0, spaceIndex));
                InventoryManager selectedMan = system.getManagerByID(managerID);

                String newName = nameField.getText().trim();
                String newCnic = cnicField.getText().trim();
                String newAddress = addressField.getText().trim();
    
                // Update manager details in the system
                boolean isUpdated = system.updateManager(
                    loggedAdmin.getAdminID(),
                    managerID,
                    newName.isBlank() ? "" : newName,
                    newCnic.isBlank() ? "" : newCnic,
                    newAddress.isBlank() ? "" : newAddress
                );
    
                if (isUpdated) {
                    // Display success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Manager details updated successfully!");
                    alert.showAndWait();
    
                    // Clear fields after updating
                    nameField.clear();
                    cnicField.clear();
                    addressField.clear();
                } else {
                    // Display failure message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update Failed");
                    alert.setContentText("Failed to update the manager. Please check the details and try again.");
                    alert.showAndWait();
                }
            }
        });
    
        // Add all components to the form area
        formArea.getChildren().addAll(selectManagerText, selectBox, nameField, cnicField, addressField, updateButton);
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void updateSupplierContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Dropdown for selecting suppliers
        Text selectSupplierText = createText("Select Supplier:");
    
        ComboBox<String> supplierComboBox = new ComboBox<>();
        supplierComboBox.setPromptText("Select a Supplier");
        supplierComboBox.setPrefWidth(300);
    
        // Populate the ComboBox with supplier details
        List<Supplier> supplierList = system.getSuppliers();
        if (supplierList.isEmpty()) {
            supplierComboBox.setPromptText("No Suppliers Found");
            supplierComboBox.setDisable(true);
        } else {
            for (Supplier supplier : supplierList) {
                String supplierEntry = supplier.getSupplierID() + " - " + supplier.getCompany();
                supplierComboBox.getItems().add(supplierEntry);
            }
        }
    
        // Input fields for updating details
        TextField companyField = createTextField("Enter New Company");
        TextField locationField = createTextField("Enter New Location");
        TextField regNoField = createTextField("Enter New Registration No");
    
        // Update Button
        Button updateButton = createStyledButton("Update Supplier", "#e41837", "#541801");
        HBox selectBox = new HBox();
        Button selectButton = createStyledButton("Select", "#e41837", "#541801");
    
        selectButton.setOnAction(event -> {
            String selectedSupplier = supplierComboBox.getValue();
            if (selectedSupplier != null) {
                int spaceIndex = selectedSupplier.indexOf(" "); // Extract Supplier ID
                int supplierID = Integer.parseInt(selectedSupplier.substring(0, spaceIndex));
                Supplier selectedSup = system.getSupplierByID(supplierID);
                companyField.setPromptText(selectedSup.getCompany() + " (Enter New Company)");
                locationField.setPromptText(selectedSup.getLocation() + " (Enter New Location)");
                regNoField.setPromptText(selectedSup.getRegNo() + " (Enter New Registration No)");
            }
        });
    
        selectBox.getChildren().addAll(supplierComboBox, selectButton);
    
        updateButton.setOnAction(event -> {
            String selectedSupplier = supplierComboBox.getValue();
            if (selectedSupplier == null) {
                // Display error if no supplier is selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Supplier Selected");
                alert.setContentText("Please select a supplier to update.");
                alert.showAndWait();
            } else {
                int spaceIndex = selectedSupplier.indexOf(" "); // Extract Supplier ID
                int supplierID = Integer.parseInt(selectedSupplier.substring(0, spaceIndex));
                Supplier selectedSup = system.getSupplierByID(supplierID);
    
                String newCompany = companyField.getText().trim();
                String newLocation = locationField.getText().trim();
                int newRegNo = regNoField.getText().trim().isEmpty() ? -1 : Integer.parseInt(regNoField.getText().trim());
    
                // Update supplier details in the system
                boolean isUpdated = system.updateSupplier(
                    loggedAdmin.getAdminID(),
                    supplierID,
                    newCompany.isBlank() ? "" : newCompany,
                    newLocation.isBlank() ? "" : newLocation,
                    newRegNo
                );
    
                if (isUpdated) {
                    // Display success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Supplier details updated successfully!");
                    alert.showAndWait();
    
                    // Clear fields after updating
                    companyField.clear();
                    locationField.clear();
                    regNoField.clear();
                } else {
                    // Display failure message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update Failed");
                    alert.setContentText("Failed to update the supplier. Please check the details and try again.");
                    alert.showAndWait();
                }
            }
        });
    
        // Add all components to the form area
        formArea.getChildren().addAll(selectSupplierText, selectBox, companyField, locationField, regNoField, updateButton);
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }    

    private void addSupplierContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Input fields for supplier details
        Text companyText = createText("Company Name:");
        TextField companyField = createTextField("Enter Company Name");
    
        Text locationText = createText("Location:");
        TextField locationField = createTextField("Enter Location");
    
        Text passwordText = createText("Password:");
        PasswordField passwordField = createPasswordField("Enter Password");
    
        Text regNoText = createText("Registration Number:");
        TextField regNoField = createTextField("Enter registration number");
    
        // Add Button
        Button addButton = createStyledButton("Add Supplier", "#e41837", "#541801");
    
        addButton.setOnAction(event -> {
            // Gather input values
            String company = companyField.getText().trim();
            String location = locationField.getText().trim();
            String password = passwordField.getText().trim();
            String regNoInput = regNoField.getText().trim();
    
            // Validation
            if (company.isEmpty() || location.isEmpty() || password.isEmpty() || regNoInput.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
            }
            else {
                int regNo = Integer.parseInt(regNoInput); // Convert registration number to integer
                // Add supplier using the system
                system.addSupplier(loggedAdmin.getAdminID(), 0 , company, location, regNo, password, false);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Supplier added successfully!");
                alert.showAndWait();

                // Clear fields after adding
                companyField.clear();
                locationField.clear();
                passwordField.clear();
                regNoField.clear();
            }
        });
    
        // Add all components to the form area
        formArea.getChildren().addAll(companyText, companyField, locationText, locationField, passwordText, passwordField, regNoText, regNoField, addButton);
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }        

    private void removeSupplierContent() {
        contentArea = new HBox(); // Create a vertical layout with spacing
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);
        // Title
        Text title = createText("Remove Supplier:");
    
        // ComboBox for Supplier List
        ComboBox<String> supplierComboBox = new ComboBox<>();
        supplierComboBox.setPromptText("Select a Supplier to Remove");
        supplierComboBox.setPrefWidth(300);
    
        // Fetch supplier list
        List<Supplier> supplierList = system.getSuppliers();
    
        if (supplierList.isEmpty()) {
            Text noSuppliersText = new Text("No Suppliers found in the system.");
            noSuppliersText.setFont(Font.font("Gilroy-Medium", 20));
            noSuppliersText.setFill(Color.RED);
            contentArea.getChildren().addAll(title, noSuppliersText);
        } else {
            // Populate ComboBox with supplier details
            for (Supplier supplier : supplierList) {
                supplierComboBox.getItems().add(supplier.getSupplierID() + " - " + supplier.getCompany());
            }
    
            // Remove Button
            Button removeButton = createStyledButton("Remove Supplier", "#e41837", "#541801");
            
            removeButton.setOnAction(event -> {
                String selectedSupplier = supplierComboBox.getValue().trim();
    
                if (selectedSupplier == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No Supplier Selected");
                    alert.setContentText("Please select a supplier to remove.");
                    alert.showAndWait();
                } else {
                    // Remove the selected supplier
                    int spaceIndex = selectedSupplier.indexOf(" "); // Extract Manager ID
                    int supplierID = Integer.parseInt(selectedSupplier.substring(0, spaceIndex));
                    boolean isRemoved = system.removeSupplier(loggedAdmin.getAdminID(), supplierID);
    
                    if (isRemoved) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Supplier with ID " + supplierID + " has been successfully removed.");
                        alert.showAndWait();
    
                        // Refresh the content area
                        supplierComboBox.getItems().remove(selectedSupplier);
                        if (supplierComboBox.getItems().isEmpty()) {
                            supplierComboBox.setPromptText("No Managers Found");
                            supplierComboBox.setDisable(true);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Failed to Remove Supplier");
                        alert.setContentText("Could not remove the supplier. Please try again.");
                        alert.showAndWait();
                    }
                }
            });
    
            // Add components to the content area
            formArea.getChildren().addAll(title, supplierComboBox, removeButton);
            contentArea.getChildren().addAll(formArea);
        }
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace the content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }    

    private void sendOrderContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        // Create a VBox to hold the form
        VBox formArea = new VBox(10); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Dropdown for selecting orders
        Text selectOrderText = createText("Select Order:");
    
        ComboBox<String> orderComboBox = new ComboBox<>();
        orderComboBox.setPromptText("Select an Order");
        orderComboBox.setPrefWidth(300);
    
        // Fetch orders for the supplier
        List<Invoice> orders = system.viewRecievedOrders(loggedSupplier.getSupplierID());
        if (orders.isEmpty()) {
            orderComboBox.setPromptText("No Orders Found");
            orderComboBox.setDisable(true);
        } else {
            for (Invoice order : orders) {
                String orderEntry = order.getInvoiceID() + " - " + order.getCreatedOn();
                orderComboBox.getItems().add(orderEntry);
            }
        }
        
        // Create TableView for displaying order and product details
        TableView<Product> tableView = new TableView<>();
        tableView.setPrefHeight(100); // Set height for scrollable area
        tableView.setPrefWidth(400); // Adjust width as needed

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productNameColumn.setPrefWidth(100);

        TableColumn<Product, Integer> productAmountColumn = new TableColumn<>("Amount");
        productAmountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Product, Integer> param) {
                // Get the index of the current row
                int index = tableView.getItems().indexOf(param.getValue());
                // Return the corresponding amount from the amounts list
                return new SimpleIntegerProperty(orders.get(index).getQuantity().get(index)).asObject();
            }
        });
        productAmountColumn.setPrefWidth(50);

        TableColumn<Product, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalPriceColumn.setPrefWidth(75);

        tableView.getColumns().addAll(productNameColumn, productAmountColumn, totalPriceColumn);

        // Display order details when selected
        VBox orderDetailsBox = new VBox(5);
        orderDetailsBox.setAlignment(Pos.TOP_LEFT);
    
        Button viewDetailsButton = createStyledButton("View Order Details", "#e41837", "#541801");
        viewDetailsButton.setOnAction(event -> {
            String selectedOrder = orderComboBox.getValue();
            if (selectedOrder != null) {
                int spaceIndex = selectedOrder.indexOf(" "); // Extract Manager ID
                int invoiceID = Integer.parseInt(selectedOrder.substring(0, spaceIndex)); // Extract Invoice ID
                Invoice order = loggedSupplier.getRecievedOrdersByID(invoiceID);
    
                orderDetailsBox.getChildren().clear();
                orderDetailsBox.getChildren().add(createText("Order Details:"));
                orderDetailsBox.getChildren().add(createText("Invoice ID: " + order.getInvoiceID()));
                orderDetailsBox.getChildren().add(createText("Customer (Manager) ID: " + order.getCreatedBy()));
                orderDetailsBox.getChildren().add(createText("Order Date: " + order.getCreatedOn()));
                orderDetailsBox.getChildren().add(createText("Total Amount: " + order.getTotalAmount()));
    
                // Populate the TableView with product details
                tableView.getItems().clear();
                for (Product product : order.getProducts()) {
                    tableView.getItems().add(product);
                }
            }
        });
    
        // Send Order Button
        Button sendOrderButton = createStyledButton("Send Products", "#e41837", "#541801");
        sendOrderButton.setOnAction(event -> {
            String selectedOrder = orderComboBox.getValue();
            if (selectedOrder == null) {
                // Display error if no order is selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Order Selected");
                alert.setContentText("Please select an order to send.");
                alert.showAndWait();
            } else {
                int spaceIndex = selectedOrder.indexOf(" "); // Extract Manager ID
                int invoiceID = Integer.parseInt(selectedOrder.substring(0, spaceIndex)); // Extract Invoice ID
                system.sendProducts(loggedSupplier.getSupplierID(), invoiceID);
    
                // Display success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Products for Invoice ID " + invoiceID + " have been successfully sent.");
                alert.showAndWait();
    
                // Clear order details and refresh UI
                orderComboBox.getSelectionModel().clearSelection();
                orderDetailsBox.getChildren().clear();
            }
        });
    
        // Add all components to the form area
        formArea.getChildren().addAll(selectOrderText, orderComboBox, viewDetailsButton, orderDetailsBox, tableView, sendOrderButton);
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }        

    private void addProductContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );

        // Create a VBox for the form
        VBox formArea = new VBox(5); // Spacing between elements
        formArea.setAlignment(Pos.CENTER_LEFT);

        // Input fields for product details
        TextField nameField = createTextField("Enter Product Name");

        TextField descriptionField = createTextField("Enter Product Description");

        TextField priceField = createTextField("Enter Product Price");

        TextField quantityField = createTextField("Enter Quantity");

        DatePicker mfgDatePicker = new DatePicker();
        mfgDatePicker.setPromptText("Select Manufacturing Date");

        DatePicker expDatePicker = new DatePicker();
        expDatePicker.setPromptText("Select Expiry Date");

        // Submit button
        Button submitButton = createStyledButton("Add Product", "#e41837", "#541801");
        submitButton.setOnAction(event -> {
            try {
                // Validate and parse input
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                LocalDate mfgDate = mfgDatePicker.getValue();
                LocalDate expDate = expDatePicker.getValue();

                if (name.isEmpty() || description.isEmpty() || mfgDate == null || expDate == null) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                if (mfgDate.isAfter(expDate)) {
                    throw new IllegalArgumentException("Manufacturing date cannot be after expiry date.");
                }

                // Add item to the system
                system.addItem(
                    loggedSupplier.getSupplierID(),
                    -1,
                    name,
                    description,
                    price,
                    Date.from(mfgDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(expDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    quantity,
                    false
                );

                // Display success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Product added successfully!");
                successAlert.showAndWait();

                // Clear form
                nameField.clear();
                descriptionField.clear();
                priceField.clear();
                quantityField.clear();
                mfgDatePicker.setValue(null);
                expDatePicker.setValue(null);
            } catch (NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid Input");
                errorAlert.setContentText("Price and Quantity must be valid numbers.");
                errorAlert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid Data");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Unexpected Error");
                errorAlert.setContentText("An error occurred while adding the product.");
                errorAlert.showAndWait();
            }
        });

        // Add components to form area
        formArea.getChildren().addAll(
            createText("Product Name:"), nameField,
            createText("Description:"), descriptionField,
            createText("Price:"), priceField,
            createText("Quantity:"), quantityField,
            createText("Manufacturing Date:"), mfgDatePicker,
            createText("Expiry Date:"), expDatePicker,
            submitButton
        );

        contentArea.getChildren().add(formArea);

        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });

        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void removeProductContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        // Create a VBox for the content
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Form Title
        Text formTitle = createText("Remove Product");
    
        // Fetch the product catalog for the supplier
        ProductCatalog catalog = system.getProductCatalog(loggedSupplier.getSupplierID());
        List<Product> products = catalog.getProduct();
        ComboBox<String> productComboBox = new ComboBox<>();
        if (products.isEmpty()) {
            // Show a message if no products are available
            productComboBox.setPromptText("No Orders Found");
            productComboBox.setDisable(true);
        } else {
            // Create a ComboBox for product selection
            productComboBox.setPromptText("Select Product to Remove");
            productComboBox.setPrefWidth(300);
    
            // Populate ComboBox with product names
            for (Product product : products) {
                String productEntry = product.getProductID() + " - " + product.getName();
                productComboBox.getItems().add(productEntry);
            }
    
            // Remove product button
            Button removeButton = createStyledButton("Remove Selected Product", "#e41837", "#541801");
            removeButton.setOnAction(event -> {
                String selectedProductEntry = productComboBox.getValue();
                if (selectedProductEntry == null) {
                    // Show an alert if no product is selected
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No Product Selected");
                    alert.setContentText("Please select a product to remove.");
                    alert.showAndWait();
                } else {
                    // Extract product ID from the ComboBox entry
                    int spaceIndex = selectedProductEntry.indexOf(" "); // Extract Manager ID
                    int invoiceID = Integer.parseInt(selectedProductEntry.substring(0, spaceIndex)); // Extract Invoice ID
    
                    // Remove the selected product
                    system.removeItem(loggedSupplier.getSupplierID(), invoiceID);
    
                    // Update the ComboBox by removing the selected item
                    productComboBox.getItems().remove(selectedProductEntry);
    
                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Product removed successfully.");
                    successAlert.showAndWait();
                }
            });
    
            // Add components to the VBox
            formArea.getChildren().addAll(formTitle, productComboBox, removeButton);
        }
    
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void updateProductQuantityContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        // Create a VBox for the content
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.CENTER_LEFT);
    
        // Form Title
        Text formTitle = createText("Update Product Quantity");

        // Fetch the product catalog for the supplier
        ProductCatalog catalog = system.getProductCatalog(loggedSupplier.getSupplierID());
        List<Product> products = catalog.getProduct();
        ComboBox<String> productComboBox = new ComboBox<>();

        if (products.isEmpty()) {
            // Show a message if no products are available
            productComboBox.setPromptText("No Orders Found");
            productComboBox.setDisable(true);
        } else {
            // Create a ComboBox for product selection
            productComboBox.setPromptText("Select Product to Update Quantity");
            productComboBox.setPrefWidth(300);
    
            // Populate ComboBox with product names and their IDs
            for (Product product : products) {
                String productEntry = product.getProductID() + " - " + product.getName();
                productComboBox.getItems().add(productEntry);
            }
    
            // Create TextField for entering new quantity
            TextField quantityTextField = createTextField("Enter New Quantity");
    
            // Update quantity button
            Button updateButton = createStyledButton("Update Quantity", "#e41837", "#541801");
            updateButton.setOnAction(event -> {
                String selectedProductEntry = productComboBox.getValue();
                String quantityInput = quantityTextField.getText().trim();
    
                if (selectedProductEntry == null || quantityInput.isEmpty()) {
                    // Show an alert if no product or quantity is selected
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Missing Information");
                    alert.setContentText("Please select a product and enter a valid quantity.");
                    alert.showAndWait();
                } else {
                    try {
                        // Extract product ID from the ComboBox entry
                        int spaceIndex = selectedProductEntry.indexOf(" "); // Extract Manager ID
                        int productID = Integer.parseInt(selectedProductEntry.substring(0, spaceIndex)); // Extract Invoice ID
                        int newQuantity = Integer.parseInt(quantityInput);
    
                        // Update the product quantity in the system
                        system.updateItem(loggedSupplier.getSupplierID(), productID, newQuantity);
    
                        // Show success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Product quantity updated successfully.");
                        successAlert.showAndWait();
    
                        // Clear input fields
                        productComboBox.getSelectionModel().clearSelection();
                        quantityTextField.clear();
                    } catch (NumberFormatException e) {
                        // Handle invalid number format exception
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Invalid Input");
                        errorAlert.setContentText("Please enter a valid quantity.");
                        errorAlert.showAndWait();
                    }
                }
            });
    
            // Add components to the VBox
            formArea.getChildren().addAll(formTitle, productComboBox, quantityTextField, updateButton);
        }
    
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void displayCatalogContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        // Create a VBox for the catalog display
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_LEFT);
    
        // Fetch product catalog for the supplier
        ProductCatalog catalog = system.getProductCatalog(loggedSupplier.getSupplierID());
    
        if (catalog.getProduct().isEmpty()) {
            // Display a message if the catalog is empty
            Text emptyCatalogMessage = createText("Empty Product Catalog.");
            emptyCatalogMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            formArea.getChildren().add(emptyCatalogMessage);
        } else {
            // Add a title for the catalog
            Text catalogTitle = createText("Product Catalog");
            formArea.getChildren().add(catalogTitle);
    
            // Create TableView to display product details
            TableView<Product> tableView = new TableView<>();
            tableView.setPrefWidth(600); // Adjust table width as needed
    
            // Product Name column
            TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameColumn.setPrefWidth(200);
    
            // Description column
            TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            descriptionColumn.setPrefWidth(250);
    
            // Amount column (Quantity)
            TableColumn<Product, Integer> amountColumn = new TableColumn<>("Amount");
            amountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Product, Integer> param) {
                    // Get the index of the current row
                    int index = tableView.getItems().indexOf(param.getValue());
                    // Return the corresponding amount from the amounts list
                    return new SimpleIntegerProperty(catalog.getAmount().get(index)).asObject();
                }
            });
            amountColumn.setPrefWidth(100);
    
            // Add columns to the table
            tableView.getColumns().addAll(nameColumn, descriptionColumn, amountColumn);
    
            // Populate the TableView with products from the catalog
            tableView.getItems().addAll(catalog.getProduct());
    
            // Add TableView to the VBox
            formArea.getChildren().add(tableView);
        }
    
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }    
    
    private void displayOrderHistoryContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );

        // Create a VBox for the order history display
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_LEFT);

        // Fetch completed orders for the supplier
        List<Invoice> orders = system.viewCompletedOrders(loggedSupplier.getSupplierID());

        if (orders.isEmpty()) {
            // Display a message if no completed orders are found
            Text emptyOrdersMessage = createText("No Completed Orders Found.");
            emptyOrdersMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            formArea.getChildren().add(emptyOrdersMessage);
        } else {
            // Add a title for the orders
            Text ordersTitle = createText("Completed Orders");
            formArea.getChildren().add(ordersTitle);

            // Create TableView to display order details
            TableView<Invoice> tableView = new TableView<>();
            tableView.setPrefWidth(700); // Adjust table width as needed

            // Invoice ID column
            TableColumn<Invoice, Integer> invoiceIDColumn = new TableColumn<>("Invoice ID");
            invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
            invoiceIDColumn.setPrefWidth(100);

            // Customer ID column
            TableColumn<Invoice, Integer> customerIDColumn = new TableColumn<>("Customer (Manager) ID");
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            customerIDColumn.setPrefWidth(150);

            // Order Date column
            TableColumn<Invoice, String> orderDateColumn = new TableColumn<>("Order Date");
            orderDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoice, String> param) {
                    return new SimpleStringProperty(param.getValue().getCreatedOn().toString());
                }
            });
            orderDateColumn.setPrefWidth(150);

            // Total Amount column
            TableColumn<Invoice, Double> totalAmountColumn = new TableColumn<>("Total Amount");
            totalAmountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, Double>, ObservableValue<Double>>() {
                @Override
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Invoice, Double> param) {
                    int index = tableView.getItems().indexOf(param.getValue());
                    if(orders.get(index).getProducts() != null){
                        return new SimpleDoubleProperty(orders.get(index).getTotalAmount()).asObject();
                    }
                    else   
                    return new SimpleDoubleProperty(0).asObject();
                }   
            });
            totalAmountColumn.setPrefWidth(150);

            // Product List column
            TableColumn<Invoice, String> productListColumn = new TableColumn<>("Product List");
            productListColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoice, String> param) {
                    Invoice order = param.getValue();
                    StringBuilder productList = new StringBuilder();
                    int i = 0;
                    if(order.getProducts() != null){
                        for (Product product : order.getProducts()) {
                            productList.append(product.getName())
                            .append(" - Amount: ");
            
                            // Check if the amount is null
                            Integer amount = order.getAmount().get(i++);
                            if (amount == null) {
                                productList.append("Amount not available");
                            } else {
                                productList.append(amount);
                            }
                            
                            productList.append("\n");
                        }
                    
                        return new SimpleStringProperty(productList.toString());
                    }
                    return new SimpleStringProperty(" Null List ");
                }
            });
            productListColumn.setPrefWidth(200);

            // Add columns to the table
            tableView.getColumns().addAll(invoiceIDColumn, customerIDColumn, orderDateColumn, totalAmountColumn, productListColumn);

            // Populate the TableView with orders
            tableView.getItems().setAll(orders);

            // Add TableView to the VBox
            formArea.getChildren().add(tableView);
        }

        contentArea.getChildren().add(formArea);

        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });

        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }

    private void generateReportContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        VBox reportArea = new VBox(20);
        reportArea.setAlignment(Pos.TOP_CENTER);
    
        // Title for the report section
        Text reportTitle = createText("Generate Reports");
        reportArea.getChildren().add(reportTitle);
    
        // ComboBox for selecting a manager
        ComboBox<String> managerComboBox = new ComboBox<>();
        managerComboBox.setPromptText("Select Manager");
        managerComboBox.setPrefWidth(200);
    
        // Populate ComboBox with a list of managers
        List<InventoryManager> managers = system.getAdminsInventoryManagers(loggedAdmin.getAdminID());
        //managerComboBox.getItems().addAll(managers);
    
        if (managers.isEmpty()) {
            // Show a message if no products are available
            managerComboBox.setPromptText("No Orders Found");
            managerComboBox.setDisable(true);
        } else {
            // Create a ComboBox for product selection
            managerComboBox.setPromptText("Select Manager");
            managerComboBox.setPrefWidth(300);
    
            // Populate ComboBox with product names and their IDs
            for (InventoryManager manager : managers) {
                String managerEntry = manager.getManagerID() + " - " + manager.getName();
                managerComboBox.getItems().add(managerEntry);
            }
        }
        // Buttons for reports
        Button storesReportButton = createStyledButton("Stores Report", "#e41837", "#541801");
    
        Button invoicesReportButton = createStyledButton("Invoice Report", "#e41837", "#541801");

        HBox buttonsArea = new HBox(20, storesReportButton, invoicesReportButton);
        buttonsArea.setAlignment(Pos.CENTER);
    
        reportArea.getChildren().addAll(managerComboBox, buttonsArea);
    
        // Event handlers for buttons
        storesReportButton.setOnAction(event -> {
            String selectedManagerEntry = managerComboBox.getValue();
            if (selectedManagerEntry == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("No Inventory Manager");
                errorAlert.setContentText("Select a Inventory Manager");
                errorAlert.showAndWait();
            } else {
                reportArea.getChildren().remove(2);
                int spaceIndex = selectedManagerEntry.indexOf(" "); // Extract Manager ID
                int managerID = Integer.parseInt(selectedManagerEntry.substring(0, spaceIndex)); // Extract Invoice ID
                VBox storesReport = generateStoresReport(managerID);
                reportArea.getChildren().add(storesReport);
            }
        });
    
        invoicesReportButton.setOnAction(event -> {
            String selectedManagerEntry = managerComboBox.getValue();
            if (selectedManagerEntry == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("No Inventory Manager");
                errorAlert.setContentText("Select a Inventory Manager");
                errorAlert.showAndWait();
            } else {
                reportArea.getChildren().remove(2);
                int spaceIndex = selectedManagerEntry.indexOf(" "); // Extract Manager ID
                int managerID = Integer.parseInt(selectedManagerEntry.substring(0, spaceIndex)); // Extract Invoice ID
                VBox invoicesReport = generateInvoiceReport(managerID);
                reportArea.getChildren().add(invoicesReport);                
            }
        });
    
        contentArea.getChildren().add(reportArea);
        contentArea.setPrefSize(900, 500);
    
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }
    
    // Function to generate Stores Report based on Manager
    private VBox generateStoresReport(int managerID) {
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_LEFT);
    
        // Title
        Text title = createText("Stores Report for Manager ID: " + managerID);
        formArea.getChildren().add(title);
    
        // TableView for Stores
        TableView<Store> tableView = new TableView<>();
        tableView.setPrefWidth(600);
    
        TableColumn<Store, Integer> idColumn = new TableColumn<>("Store ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("storeID"));
    
        TableColumn<Store, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    
        TableColumn<Store, Integer> managerIdColumn = new TableColumn<>("Manager ID");
        managerIdColumn.setCellValueFactory(new PropertyValueFactory<>("managerID"));
    
        tableView.getColumns().addAll(idColumn, locationColumn, managerIdColumn);
    
        // Populate TableView with store data for the selected manager
        Store stores = system.generateManagerReport(managerID);
        tableView.getItems().addAll(stores);
    
        formArea.getChildren().add(tableView);
        return formArea;
    }
    
    // Function to generate Invoice Report based on Manager
    private VBox generateInvoiceReport(int managerID) {
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_LEFT);
    
        // Title
        Text title = createText("Invoice Report for Manager ID: " + managerID);
        formArea.getChildren().add(title);
    
        // TableView for Invoices
        TableView<Invoice> tableView = new TableView<>();
        tableView.setPrefWidth(600);
    
        TableColumn<Invoice, Integer> invoiceIdColumn = new TableColumn<>("Invoice ID");
        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
    
        TableColumn<Invoice, Integer> createdByIdColumn = new TableColumn<>("Created By ID");
        createdByIdColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    
        TableColumn<Invoice, String> dateColumn = new TableColumn<>("Created On");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
    
        TableColumn<Invoice, Double> totalAmountColumn = new TableColumn<>("Total Amount");
        totalAmountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Invoice, Double> param) {
                return new SimpleDoubleProperty(param.getValue().getTotalAmount()).asObject();
            }
        });
    
        tableView.getColumns().addAll(invoiceIdColumn, createdByIdColumn, dateColumn, totalAmountColumn);
    
        // Populate TableView with invoice data for the selected manager
        List<Invoice> invoices = system.getInvoiceReport(managerID);
        tableView.getItems().addAll(invoices);
    
        formArea.getChildren().add(tableView);
        return formArea;
    }

    private void updateAdminProfileContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        // Create a VBox for the form
        VBox formArea = new VBox(15);
        formArea.setAlignment(Pos.TOP_CENTER);
    
        // Title
        Text formTitle = createText("Update Admin Profile");
    
        // Fields for Name, CNIC, Address, and Password
        TextField nameField = createTextField(loggedAdmin.getName() + " (Enter Name)");
        
        TextField cnicField = createTextField(loggedAdmin.getCNIC() + " (Enter CNIC)");
    
        TextField addressField = createTextField(loggedAdmin.getAddress() + " (Enter Address)");
    
        PasswordField passwordField = createPasswordField(" (Enter Password)");
    
        // Save Button
        Button saveButton = createStyledButton("Save Changes", "#e41837", "#541801");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String cnic = cnicField.getText();
            String address = addressField.getText();
            String password = passwordField.getText();
    
            if (name.isEmpty() || cnic.isEmpty() || address.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled!", ButtonType.OK);
                alert.showAndWait();
            } else {
                boolean isUpdated = system.updateAdminProfile(loggedAdmin.getAdminID(), name, cnic, address, password);
                if (isUpdated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully!", ButtonType.OK);
                    alert.showAndWait();
                    // Update loggedAdmin details locally if needed
                    loggedAdmin.setName(name);
                    loggedAdmin.setCNIC(cnic);
                    loggedAdmin.setAddress(address);
                    loggedAdmin.setPassword(password);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update profile. Please try again.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    
        // Add fields and button to the form
        formArea.getChildren().addAll(formTitle, nameField, cnicField, addressField, passwordField, saveButton);
    
        // Add formArea to the content area
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2); // Assuming index 2 is where content is displayed
        dashBoard.getChildren().add(2, contentArea);
    }      

    private void makeSaleContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_CENTER);
    
        // Title
        Text formTitle = createText("Make a Sale");
    
        // Product ID and Quantity Fields
        ComboBox<String> productComboBox = new ComboBox<>();
        ComboBox<String> quantityComboBox = new ComboBox<>();

        List<Stock> stockList = system.getStoreStock(loggedManager.getManagerID());
        
        if (stockList.isEmpty()) {
            // Show a message if no products are available
            productComboBox.setPromptText("No Products Found");
            quantityComboBox.setPromptText("Select a Product");
            productComboBox.setDisable(true);
        } else {
	        // Create a ComboBox for product selection
            productComboBox.setPromptText("Select Product ID");
            quantityComboBox.setPromptText("Select a Quantity");
            productComboBox.setPrefWidth(300);
		    // Populate ComboBox with product names and their IDs
            for (Stock stock : stockList) {
                String productEntry = stock.getProduct().getProductID() + " - " + stock.getProduct().getName();
                productComboBox.getItems().add(productEntry);
            }
        }
        productComboBox.setOnAction(event -> {
            // Extract product ID from the ComboBox entry
            String selectedProductEntry = productComboBox.getValue();
            int spaceIndex = selectedProductEntry.indexOf(" "); // Extract Manager ID
            int productID = Integer.parseInt(selectedProductEntry.substring(0, spaceIndex)); // Extract Invoice ID
            Stock selectedProduct = null;
            for (Stock stock : stockList) {
                if(stock.getProduct().getProductID() == productID){
                    selectedProduct = stock;
                }
            }
            if (selectedProduct != null) {
                quantityComboBox.getItems().clear();
                int availableStock = selectedProduct.getQuantity();
                for (int i = 1; i <= availableStock; i++) {
                    quantityComboBox.getItems().add(Integer.toString(i));
                }
            }
        });
        Button addProductButton = createStyledButton("Add Product", "#e41837", "#541801");
        List<Integer> productIDs = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        addProductButton.setOnAction(e -> {
            if(productComboBox.getValue() != null){
                String selectedProductEntry = productComboBox.getValue();
                String selectedQuantityEntry = productComboBox.getValue();
                int spaceIndex = selectedProductEntry.indexOf(" "); // Extract Manager ID
                int productID = Integer.parseInt(selectedQuantityEntry.substring(0, spaceIndex)); // Extract Invoice ID
                spaceIndex = selectedQuantityEntry.indexOf(" "); // Extract Manager ID
                int quantity = Integer.parseInt(selectedQuantityEntry.substring(0, spaceIndex)); // Extract Invoice ID
                productIDs.add(productID);
                quantities.add(quantity);
            }
    
        });
        
        // Submit Button
        Button submitButton = createStyledButton("Complete Sale", "#e41837", "#541801");
        submitButton.setOnAction(e -> {

            boolean isValid = true;
            for (int i = 0; i < productIDs.size(); i++) {
                try {
                    int productID = productIDs.get(i);
                    int quantity = quantities.get(i);
    
                    if (system.isValidProduct(loggedManager.getManagerID(), productID)) {
                        
                    	//productIDs.add(productID);
                        //quantities.add(quantity);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Product ID: " + productID, ButtonType.OK);
                        alert.showAndWait();
                        isValid = false;
                        break;
                    }
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for Product ID and Quantity.", ButtonType.OK);
                    alert.showAndWait();
                    isValid = false;
                    break;
                }
            }
    
            if (isValid) {
                system.makeSale(loggedManager.getManagerID(), productIDs, quantities);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sale completed successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        });
    
        formArea.getChildren().addAll(formTitle,productComboBox,quantityComboBox,addProductButton,submitButton);
    
        // Add formArea to the content area
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2); // Assuming index 2 is where content is displayed
        dashBoard.getChildren().add(2, contentArea);
    }      

    private void placeOrderContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_CENTER);
    
        // Title
        Text formTitle = createText("Place an Order");
    
        // Supplier ComboBox
        ComboBox<String> supplierComboBox = new ComboBox<>();
        supplierComboBox.setPromptText("Select Supplier");
        supplierComboBox.setPrefWidth(300);
    
        // Populate ComboBox with supplier details
        List<Supplier> supplierList = system.getSuppliers(); // Fetch the list of suppliers
        if (supplierList.isEmpty()) {
            supplierComboBox.setPromptText("No Suppliers Found");
            supplierComboBox.setDisable(true);
        } else {
            for (Supplier supplier : supplierList) {
                String supplierEntry = supplier.getSupplierID() + " - " + supplier.getCompany();
                supplierComboBox.getItems().add(supplierEntry);
            }
        }
    
        // Product and Quantity Fields
        ComboBox<String> productComboBox = new ComboBox<>();
        ComboBox<String> quantityComboBox = new ComboBox<>();
        productComboBox.setPromptText("Select a Supplier");
        quantityComboBox.setPromptText("Select a Supplier");
        supplierComboBox.setOnAction(e ->{
            //productComboBox.getItems().clear();
            quantityComboBox.getItems().clear();
            productComboBox.setPromptText("Select a Supplier");
            quantityComboBox.setPromptText("Select a Supplier");
            String selectedSupplierEntry = supplierComboBox.getValue();
            int spaceIndex = selectedSupplierEntry.indexOf(" "); // Extract Manager ID
            int supplierID = Integer.parseInt(selectedSupplierEntry.substring(0, spaceIndex)); // Extract Invoice ID
            ProductCatalog productList = system.getSupplierCatalog(supplierID);

            if (productList == null) {
                productComboBox.setPromptText("No Products Found");
                quantityComboBox.setPromptText("Select a Product");
                productComboBox.setDisable(true);
            } else {
                productComboBox.setPromptText("Select Product");
                quantityComboBox.setPromptText("Select a Quantity");
                productComboBox.setPrefWidth(300);
                for (Product stock : productList.getProduct()) {
                    String productEntry = stock.getProductID() + " - " + stock.getName();
                    productComboBox.getItems().add(productEntry);
                }
            }
            productComboBox.setOnAction(event -> {
                // Extract product ID from the ComboBox entry
                if(productComboBox.getValue() != null){
                    String selectedProductEntry = productComboBox.getValue();
                    int spaceIndex2 = selectedProductEntry.indexOf(" "); // Extract Manager ID
                    int productID = Integer.parseInt(selectedProductEntry.substring(0, spaceIndex2)); // Extract Invoice ID
                    Product selectedProduct = null;
                    int quantity = 0;
                    int j = 0;
                    for (Product stock : productList.getProduct()) {
                        if(stock.getProductID() == productID){
                            selectedProduct = stock;
                            quantity = productList.getAmount().get(j);
                            j++;
                        }
                    }
                    if (selectedProduct != null) {
                        quantityComboBox.getItems().clear();
                        for (int i = 1; i <= quantity; i++) {
                            quantityComboBox.getItems().add(Integer.toString(i));
                        }
                    }
                }
            });
        });
    
    
        // Add Product Button
        Button addProductButton = createStyledButton("Add Product", "#e41837", "#541801");
        List<Integer> productIDs = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        //List<Supplier> suppliers = new ArrayList<>();
    
        addProductButton.setOnAction(e -> {
            if(productComboBox.getValue() != null){
                String selectedSupplierEntry = supplierComboBox.getValue();
                int spaceIndex = selectedSupplierEntry.indexOf(" "); // Extract Manager ID
                int supplierID = Integer.parseInt(selectedSupplierEntry.substring(0, spaceIndex)); // Extract Invoice ID
                String selectedProductEntry = productComboBox.getValue();
                String selectedQuantityEntry = productComboBox.getValue();
                spaceIndex = selectedProductEntry.indexOf(" "); // Extract Manager ID
                int productID = Integer.parseInt(selectedQuantityEntry.substring(0, spaceIndex)); // Extract Invoice ID
                spaceIndex = selectedQuantityEntry.indexOf(" "); // Extract Manager ID
                int quantity = Integer.parseInt(selectedQuantityEntry.substring(0, spaceIndex)); // Extract Invoice ID
                if (system.isValidProductFromSupplier(supplierID, productID)) {
                    productIDs.add(productID);
                    quantities.add(quantity);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Product ID: " + productID, ButtonType.OK);
                    alert.showAndWait();
                    //isValid = false;
                    //break;
                }
                //productIDs.add(productID);
                //quantities.add(quantity);
                //suppliers.add(system.getSupplier(supplierID));
                productComboBox.getItems().clear();
                quantityComboBox.getItems().clear();
                productComboBox.setPromptText("Select a Supplier");
                quantityComboBox.setPromptText("Select a Supplier");
                supplierComboBox.setPromptText("Select Supplier");
            }
            
        });
    
        // Submit Button
        Button submitButton = createStyledButton("Place Order", "#e41837", "#541801");
        submitButton.setOnAction(e -> {
            for (int i = 0; i < productIDs.size(); i++) {
                /*try {
                    int productID = productIDs.get(i);
                    int quantity = quantities.get(i);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for Product ID and Quantity.", ButtonType.OK);
                    alert.showAndWait();
                    break;
                }*/
                String selectedSupplierEntry = supplierComboBox.getValue();
                if(selectedSupplierEntry != null){
                    int spaceIndex = selectedSupplierEntry.indexOf(" "); // Extract Manager ID
                    int supplierID = Integer.parseInt(selectedSupplierEntry.substring(0, spaceIndex)); // Extract Invoice ID
                    if(system.isValidProductFromSupplier(supplierID, productIDs.get(i))){
                        system.placeOrder(loggedManager.getManagerID(),supplierID, productIDs, quantities);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sale completed successfully! with Supplier ID: " + supplierID, ButtonType.OK);
                        alert.showAndWait();
                    }
                }   
            }
            placeOrderContent();
        });
        // Add elements to the form area
        formArea.getChildren().addAll(
            formTitle, supplierComboBox, productComboBox, quantityComboBox, addProductButton, submitButton
        );
    
        // Add formArea to the content area
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2); // Assuming index 2 is where content is displayed
        dashBoard.getChildren().add(2, contentArea);
    }
    private void checkStockContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );

        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_CENTER);

        // Title
        Text formTitle = createText("Stock Details");

        // Create a TableView to display stock details
        TableView<Stock> stockTableView = new TableView<>();
        stockTableView.setPrefWidth(600);
        stockTableView.setPrefHeight(400);

        // Define the columns for the TableView
        TableColumn<Stock, String> productIDColumn = new TableColumn<>("Stock ID");
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("stockID"));

        /*TableColumn<Stock, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));*/

        TableColumn<Stock, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Stock, Double> totalCostColumn = new TableColumn<>("Total Cost");
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        TableColumn<Stock, String> arrivalDateColumn = new TableColumn<>("Arrival Date");
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));

        /*TableColumn<Product, String> productNameColumn = new TableColumn<>("Name");
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> productDescriptionColumn = new TableColumn<>("Description");
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("description"));*/

        // Add columns to the TableView
        stockTableView.getColumns().addAll(productIDColumn, quantityColumn, totalCostColumn, arrivalDateColumn);

        // Fetch stock data from the system
        List<Stock> stockList = system.checkStock(loggedManager.getManagerID());

        if (stockList.isEmpty()) {
            // Show a message if no stock is available
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No products found in stock.", ButtonType.OK);
            alert.showAndWait();
        } else {
            // Add stock data to the TableView
            ObservableList<Stock> stockData = FXCollections.observableArrayList(stockList);
            stockTableView.setItems(stockData);
        }

        // Add elements to the form area
        formArea.getChildren().addAll(formTitle, stockTableView);

        // Add formArea to the content area
        contentArea.getChildren().add(formArea);

        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });

        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });

        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2); // Assuming index 2 is where content is displayed
        dashBoard.getChildren().add(2, contentArea);
    }

    private void viewOrderStatusContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        VBox formArea = new VBox(10);
        formArea.setAlignment(Pos.TOP_CENTER);
    
        // Title
        Text formTitle = createText("Order Status (Invoices)");
        // Fetch invoice data from the system
        List<Invoice> invoices = system.viewOrderStatus(loggedManager.getManagerID());
        for(Invoice invoice : invoices) {
        	System.out.println(invoice.toString());
        }
        
        // Create a TableView to display invoice details
        TableView<Invoice> invoiceTableView = new TableView<>();
        invoiceTableView.setPrefWidth(600);
        invoiceTableView.setPrefHeight(400);
    
        // Define the columns for the TableView
        TableColumn<Invoice, Integer> invoiceIDColumn = new TableColumn<>("Invoice ID");
        invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
    
        TableColumn<Invoice, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    
        TableColumn<Invoice, String> createdOnColumn = new TableColumn<>("Created On");
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
    
        TableColumn<Invoice, String> paidStatusColumn = new TableColumn<>("Delivered");
        paidStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoice, String> param) {
                // Get the index of the current row
                int index = invoiceTableView.getItems().indexOf(param.getValue());
                // Convert the boolean value to a string ("Yes" or "No") and return it
                boolean isAvailable = invoices.get(index).isDelivered();
                String availabilityText = isAvailable ? "Delivered" : "Not Delivered";
                return new SimpleStringProperty(availabilityText);
            }
        });
        //paidStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
    
        TableColumn<Invoice, String> deliveredStatusColumn = new TableColumn<>("Paid");
        deliveredStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoice, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoice, String> param) {
                // Get the index of the current row
                int index = invoiceTableView.getItems().indexOf(param.getValue());
                // Convert the boolean value to a string ("Yes" or "No") and return it
                boolean isAvailable = invoices.get(index).isPaidFor();
                String availabilityText = isAvailable ? "Paid" : "Not Paid";
                return new SimpleStringProperty(availabilityText);
            }
        });
    
        // Add columns to the TableView
        invoiceTableView.getColumns().addAll(invoiceIDColumn, createdByColumn, createdOnColumn, paidStatusColumn, deliveredStatusColumn);
    
    
        if (invoices.isEmpty()) {
            // Show a message if no invoices are available
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No invoices found.", ButtonType.OK);
            alert.showAndWait();
        } else {
            // Add invoice data to the TableView
            ObservableList<Invoice> invoiceData = FXCollections.observableArrayList(invoices);
            invoiceTableView.setItems(invoiceData);
        }
    
        // Add elements to the form area
        formArea.getChildren().addAll(formTitle, invoiceTableView);
    
        // Add formArea to the content area
        contentArea.getChildren().add(formArea);
    
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2); // Assuming index 2 is where content is displayed
        dashBoard.getChildren().add(2, contentArea);
    }

    private void generateReportManContent() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
    
        VBox reportArea = new VBox(20);
        reportArea.setAlignment(Pos.TOP_CENTER);
    
        // Title for the report section
        Text reportTitle = createText("Generate Reports");
        reportArea.getChildren().add(reportTitle);

        VBox storesReport = generateStoresReport(loggedManager.getManagerID());
        reportArea.getChildren().add(storesReport);

        contentArea.getChildren().add(reportArea);
        contentArea.setPrefSize(900, 500);
    
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }
    private void payInvoicesContent(){
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;"
        );
        //
        VBox formArea = new VBox(15);
        formArea.setAlignment(Pos.TOP_CENTER);
    
        // Title
        Text formTitle = createText("Pay or Remove Unpaid Invoices");
    
        // ComboBox for unpaid invoices
        ComboBox<String> unpaidInvoiceComboBox = new ComboBox<>();
        unpaidInvoiceComboBox.setPromptText("Select an unpaid invoice");
        unpaidInvoiceComboBox.setPrefWidth(400);
    
        // Fetch unpaid invoices
        List<Invoice> unpaidInvoices = loggedAdmin.getUnpaidInvoices();
        VBox orderDetailsBox = new VBox(5);
        orderDetailsBox.setAlignment(Pos.TOP_LEFT);
        Invoice selectedInvoice = null;
    
        if (unpaidInvoices.isEmpty()) {
            unpaidInvoiceComboBox.setPromptText("No unpaid invoices found");
            unpaidInvoiceComboBox.setDisable(true);
        } else {
            for (Invoice invoice : unpaidInvoices) {
                // Display invoice ID and details
                String invoiceEntry = invoice.getInvoiceID() + " - " + invoice.getCreatedOn();
                unpaidInvoiceComboBox.getItems().add(invoiceEntry);

            }
        }
        
        unpaidInvoiceComboBox.setOnAction(e -> {
            String selectedInvoiceEntry = unpaidInvoiceComboBox.getValue();
            if(selectedInvoiceEntry != null) {
            	int spaceIndex = selectedInvoiceEntry.indexOf(" "); // Extract Manager ID
                int invoiceID = Integer.parseInt(selectedInvoiceEntry.substring(0, spaceIndex)); // Extract Invoice ID
                for(Invoice invoice : unpaidInvoices) {
                	if(invoice.getInvoiceID() == invoiceID) {
                		orderDetailsBox.getChildren().clear();
                        orderDetailsBox.getChildren().add(createText("Order Details:"));
                        orderDetailsBox.getChildren().add(createText("Invoice ID: " + invoice.getInvoiceID()));
                        orderDetailsBox.getChildren().add(createText("Customer (Manager) ID: " + invoice.getCreatedBy()));
                        orderDetailsBox.getChildren().add(createText("Order Date: " + invoice.getCreatedOn()));
                        orderDetailsBox.getChildren().add(createText("Total Amount: " + invoice.getTotalAmount()));
                        break;
                	}
                }
            }
        });
    
        // Pay button
        Button payButton = createStyledButton("Pay Invoice", "#e41837", "#541801");
        payButton.setOnAction(event -> {
            String selectedInvoiceEntry = unpaidInvoiceComboBox.getValue();
            if (selectedInvoiceEntry != null) {
                int spaceIndex = selectedInvoiceEntry.indexOf(" "); // Extract Manager ID
                int invoiceID = Integer.parseInt(selectedInvoiceEntry.substring(0, spaceIndex)); // Extract Invoice ID
                int supplierID = -1;
                for (Invoice invoice : unpaidInvoices) {
                    // Display invoice ID and details
                    if(invoice.getInvoiceID() == invoiceID){
                        supplierID = invoice.getSupplierID();
                    }
                }
                if (supplierID != -1) {
                    system.approveOrder(loggedAdmin.getAdminID(), invoiceID);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invoice ID " + invoiceID + " marked as paid!", ButtonType.OK);
                    alert.showAndWait();
                    unpaidInvoiceComboBox.getItems().remove(selectedInvoiceEntry); // Remove from the combo box
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to mark invoice as paid.", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an invoice to pay.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    
        // Remove button
        Button removeButton = createStyledButton("Remove Invoice", "#e41837", "#541801");
        removeButton.setOnAction(event -> {
            String selectedInvoiceEntry = unpaidInvoiceComboBox.getValue();
            if (selectedInvoiceEntry != null) {
                int spaceIndex = selectedInvoiceEntry.indexOf(" "); // Extract Manager ID
                int invoiceID = Integer.parseInt(selectedInvoiceEntry.substring(0, spaceIndex)); // Extract Invoice ID
                boolean success = system.removeAdminUnpaidInvoice(loggedAdmin.getAdminID(), invoiceID);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invoice ID " + invoiceID + " removed successfully!", ButtonType.OK);
                    alert.showAndWait();
                    unpaidInvoiceComboBox.getItems().remove(selectedInvoiceEntry); // Remove from the combo box
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to remove invoice.", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an invoice to remove.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(removeButton,payButton);
        // Add elements to form area
        formArea.getChildren().addAll(formTitle, unpaidInvoiceComboBox,orderDetailsBox, buttons);
        
        contentArea.getChildren().add(formArea);
        contentArea.setPrefSize(900, 500);
    
        contentArea.setOnMouseEntered(event -> {
            contentArea.setScaleX(1.02);
            contentArea.setScaleY(1.02);
        });
    
        contentArea.setOnMouseExited(event -> {
            contentArea.setScaleX(1.0);
            contentArea.setScaleY(1.0);
        });
    
        // Replace content area in the dashboard
        dashBoard.getChildren().remove(2);
        dashBoard.getChildren().add(2, contentArea);
    }
    private VBox createAdminButtonPanel() {
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(20));
        // Styling with transparency in gradient
        buttonPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(228, 24, 55, 0.6), rgba(10, 1, 0, 0.6));" + // Gradient with transparency
            "-fx-border-color: linear-gradient(to right, rgba(228, 24, 55, 0.85), rgba(10, 1, 0, 0.85));" + // Border with the same gradient
            "-fx-border-width: 2;" + // Border thickness
            "-fx-border-radius: 30;" + // Rounded corners for border
            "-fx-background-radius: 30;" + // Match background rounding with border
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        buttonPanel.setAlignment(Pos.TOP_CENTER);
    
        // Create buttons
        Button payInvoicesButton = createDashboardButton("Pay Invoices");
        payInvoicesButton.setOnAction(e -> {
            payInvoicesContent();
        });
        Button addInventoryManagerButton = createDashboardButton("Add Inventory Manager");
        addInventoryManagerButton.setOnAction(e -> {
            addInventoryManagerContent();
        });
        Button removeInventoryManagerButton = createDashboardButton("Remove Inventory Manager");
        removeInventoryManagerButton.setOnAction(e -> {
            removeInventoryManagerContent();
        });
        Button updateInventoryManagerButton = createDashboardButton("Update Inventory Manager");
        updateInventoryManagerButton.setOnAction(e -> {
            updateInventoryManagerContent();
        });
        Button addSupplierButton = createDashboardButton("Add Supplier");
        addSupplierButton.setOnAction(e -> {
            addSupplierContent();
        });
        Button removeSupplierButton = createDashboardButton("Remove Supplier");
        removeSupplierButton.setOnAction(e -> {
            removeSupplierContent();
        });
        Button updateSupplierButton = createDashboardButton("Update Supplier");
        updateSupplierButton.setOnAction(e -> {
            updateSupplierContent();
        });
        Button generateReportButton = createDashboardButton("Generate Report");
        generateReportButton.setOnAction(e -> {
            generateReportContent();
        });
        Button updateProfileButton = createDashboardButton("Update Profile");
        updateProfileButton.setOnAction(e -> {
            updateAdminProfileContent();
        });
        Button logoutButton = createDashboardButton("Logout");
        logoutButton.setOnAction(e -> {
            HBox titleBoxPrev = titleBox;
            titleBox = createTitleBox();
            navigateToBox(titleBoxPrev, titleBox);
            navigateToBox(dashBoard,loginBox);
        });
        // Logout button style tweak
        logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-text-fill: white; -fx-font-size: 14px; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        // Hover effect
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(
            "-fx-background-color: #541801; -fx-text-fill: white; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " 
        ));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " 
        ));
        // Add buttons to the panel
        buttonPanel.getChildren().addAll(
            payInvoicesButton, addInventoryManagerButton, removeInventoryManagerButton, updateInventoryManagerButton,
            addSupplierButton, removeSupplierButton, updateSupplierButton,
            generateReportButton, updateProfileButton, logoutButton
        );
        return buttonPanel;
    }    

    private VBox createSupplierButtonPanel() {
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(20));
        // Styling with transparency in gradient
        buttonPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(228, 24, 55, 0.6), rgba(10, 1, 0, 0.6));" + // Gradient with transparency
            "-fx-border-color: linear-gradient(to right, rgba(228, 24, 55, 0.85), rgba(10, 1, 0, 0.85));" + // Border with the same gradient
            "-fx-border-width: 2;" + // Border thickness
            "-fx-border-radius: 30;" + // Rounded corners for border
            "-fx-background-radius: 30;" + // Match background rounding with border
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        buttonPanel.setAlignment(Pos.TOP_CENTER);
    
        // Create buttons for supplier actions
        Button sendOrderButton = createDashboardButton("Send Order");
        sendOrderButton.setOnAction(e -> {
            // Call method to handle sending an order
            sendOrderContent();
        });
    
        Button pastOrdersButton = createDashboardButton("Completed Orders");
        pastOrdersButton.setOnAction(e -> {
            // Call method to handle requesting payment
            displayOrderHistoryContent();
        });
    
        Button addProductButton = createDashboardButton("Add Product");
        addProductButton.setOnAction(e -> {
            // Call method to handle adding a product
            addProductContent();
        });
    
        Button removeProductButton = createDashboardButton("Remove Product");
        removeProductButton.setOnAction(e -> {
            // Call method to handle removing a product
            removeProductContent();
        });
    
        Button updateProductQuantityButton = createDashboardButton("Update Product Quantity");
        updateProductQuantityButton.setOnAction(e -> {
            // Call method to handle updating product quantity
            updateProductQuantityContent();
        });
    
        Button displayCatalogButton = createDashboardButton("Display Catalog");
        displayCatalogButton.setOnAction(e -> {
            // Call method to display the supplier's catalog
            displayCatalogContent();
        });
    
        Button logoutButton = createDashboardButton("Logout");
        logoutButton.setOnAction(e -> {
            // Handle logout functionality
            HBox titleBoxPrev = titleBox;
            titleBox = createTitleBox();
            navigateToBox(titleBoxPrev, titleBox);
            navigateToBox(dashBoard, loginBox);
        });
    
        // Logout button style tweak
        logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-text-fill: white; -fx-font-size: 14px; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        // Hover effect
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(
            "-fx-background-color: #541801; -fx-text-fill: white; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; "
        ));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; "
        ));
    
        // Add all buttons to the panel
        buttonPanel.getChildren().addAll(
            sendOrderButton, pastOrdersButton, addProductButton,
            removeProductButton, updateProductQuantityButton, displayCatalogButton,
            logoutButton
        );
    
        return buttonPanel;
    }
    
    private VBox createInventoryManagerButtonPanel() {
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(20));
        // Styling with transparency in gradient
        buttonPanel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(228, 24, 55, 0.6), rgba(10, 1, 0, 0.6));" + // Gradient with transparency
            "-fx-border-color: linear-gradient(to right, rgba(228, 24, 55, 0.85), rgba(10, 1, 0, 0.85));" + // Border with the same gradient
            "-fx-border-width: 2;" + // Border thickness
            "-fx-border-radius: 30;" + // Rounded corners for border
            "-fx-background-radius: 30;" + // Match background rounding with border
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        buttonPanel.setAlignment(Pos.TOP_CENTER);
    
        // Create buttons for inventory manager actions
        Button makeSaleButton = createDashboardButton("Make a Sale");
        makeSaleButton.setOnAction(e -> {
            // Call method to handle making a sale
            makeSaleContent();
        });
    
        Button placeOrderButton = createDashboardButton("Place an Order");
        placeOrderButton.setOnAction(e -> {
            // Call method to handle placing an order
            placeOrderContent();
        });
    
        Button checkStockButton = createDashboardButton("Check Stock");
        checkStockButton.setOnAction(e -> {
            // Call method to check stock
            checkStockContent();
        });
    
        Button generateReportButton = createDashboardButton("Generate Report");
        generateReportButton.setOnAction(e -> {
            // Call method to generate a report
            generateReportManContent();
        });
    
        Button viewOrderStatusButton = createDashboardButton("View Order Status");
        viewOrderStatusButton.setOnAction(e -> {
            // Call method to view order status
            viewOrderStatusContent();
        });
    
        Button logoutButton = createDashboardButton("Logout");
        logoutButton.setOnAction(e -> {
            // Handle logout functionality
            HBox titleBoxPrev = titleBox;
            titleBox = createTitleBox();
            navigateToBox(titleBoxPrev, titleBox);
            navigateToBox(dashBoard, loginBox);
        });
    
        // Logout button style tweak
        logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-text-fill: white; -fx-font-size: 14px; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        // Hover effect
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(
            "-fx-background-color: #541801; -fx-text-fill: white; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; "
        ));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(
            "-fx-background-color: #e41837; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; "
        ));
    
        // Add all buttons to the panel
        buttonPanel.getChildren().addAll(
            makeSaleButton, placeOrderButton, checkStockButton,
            generateReportButton, viewOrderStatusButton,
            logoutButton
        );
    
        return buttonPanel;
    }    

    private Button createDashboardButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setPrefHeight(40);
        button.setStyle(
            "-fx-background-color: #ffffff; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #e41837; -fx-text-fill: white; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " 
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #ffffff; -fx-border-radius: 30; " +
            "-fx-background-radius: 30; -fx-font-size: 14px; -fx-font-family: 'Gilroy-Medium'; " 
        ));
    
        return button;
    }
    
    private HBox createContentArea() {
        contentArea = new HBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: #0a0100;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
        contentArea.setPrefSize(900, 500); // Adjust dimensions as needed
    
        // Placeholder content
        Text placeholder = new Text("Admin Dashboard");
        placeholder.setFont(Font.font("Gilroy-Medium", 24));
        placeholder.setFill(Color.GRAY);
    
        //contentArea.getChildren().add(placeholder);
        return contentArea;
    }
    
    private HBox createTitleBox() {
        // Create the "HisaabTrack" title
        Text title = new Text("HisaabTrack");
        title.setFont(Font.font("Gilroy-Medium", 32));
        title.setFill(createGradient());

        // Flexible spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Title box configuration
        HBox titleBox = new HBox(40, title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: linear-gradient(to right, #e41837, #0a0100);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );

        titleBox.setOnMouseEntered(event -> {
            titleBox.setScaleX(1.02);
            titleBox.setScaleY(1.02);
        });

        titleBox.setOnMouseExited(event -> {
            titleBox.setScaleX(1.0);
            titleBox.setScaleY(1.0);
        });

        return titleBox;
    }
    private HBox createTitleBoxLoggedIn() {
        // Create the "HisaabTrack" title
        Text title = new Text("HisaabTrack");
        title.setFont(Font.font("Gilroy-Medium", 32));
        title.setFill(createGradient());

        // Create the tagline "One Stop Inventory"
        String str = "";
        if(userType == "Admin"){
            str = loggedAdmin.getName();
        }
        else if(userType == "Supplier"){
            str = loggedSupplier.getCompany();
        }
        else if(userType == "Inventory Manager"){
            str = loggedManager.getName();
        }
        Text tagline = new Text("Welcome, " + str);
        tagline.setFont(Font.font("Gilroy-Medium", 16));
        tagline.setFill(createGradient());
        // Flexible spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        // Title box configuration
        HBox titleBox = new HBox(tagline,spacer, title,spacer2);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: linear-gradient(to right, #e41837, #0a0100);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );

        titleBox.setOnMouseEntered(event -> {
            titleBox.setScaleX(1.02);
            titleBox.setScaleY(1.02);
        });

        titleBox.setOnMouseExited(event -> {
            titleBox.setScaleX(1.0);
            titleBox.setScaleY(1.0);
        });
        titleBox.setTranslateX(1280);
        return titleBox;
    }

    private HBox createSignUpBox() {
        VBox signUpBoxV = new VBox(20);
        signUpBoxV.setAlignment(Pos.CENTER);
        signUpBoxV.setPadding(new Insets(20, 60, 20, 60));
        signUpBoxV.setPrefSize(1280 * 0.5, 720 * 0.6); // Adjust height for additional fields
        signUpBoxV.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: linear-gradient(to bottom, #e41837, #0a0100);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );
    
        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        signUpBoxV.setEffect(shadow);
    
        // Input Fields
        //TextField typeField = createTextField("User Type");
        TextField adminNameField = createTextField("Admin Name");
        TextField cnicField = createTextField("CNIC");
        TextField emailField = createTextField("Email");
        TextField addressField = createTextField("Address");
        PasswordField passwordField = createPasswordField("Password");
    
        // Buttons
        Button registerButton = createStyledButton("Register", "#e41837", "#541801");
        Button backButton = createStyledButton("Back", "#0a0100", "#6b6a6a");
    
        // Back Button Action (Navigate to Login Box)
        backButton.setOnAction(e -> {
            navigateToBox(signUpBox,loginBox);
        });
        registerButton.setOnAction(e -> {
            if(adminNameField.getText().trim().isEmpty() || cnicField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty() || addressField.getText().trim().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Sign Up Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Fields. Please try again.");
                alert.showAndWait();
            }
            else{
                createAdminDashboard();
                HBox titleBoxPrev = titleBox;
                userType = "Admin";
                loggedAdmin = system.addAdmin(0, adminNameField.getText().trim(), cnicField.getText().trim(), addressField.getText().trim(), passwordField.getText().trim(), false);
                titleBox = createTitleBoxLoggedIn();
                navigateToBox(titleBoxPrev, titleBox);
                navigateToBox(signUpBox,dashBoard);
            }
            adminNameField.clear();
            cnicField.clear();
            emailField.clear();
            addressField.clear();
            passwordField.clear();
        });
    
        HBox buttonBox = new HBox(10, registerButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
    
        // Add all elements to the VBox
        signUpBoxV.getChildren().addAll(adminNameField, cnicField, emailField, addressField, passwordField, buttonBox);
        Rectangle leftSpacer = new Rectangle(400, 0, Color.TRANSPARENT);
        Rectangle rightSpacer = new Rectangle(400, 0, Color.TRANSPARENT);

        signUpBox = new HBox(leftSpacer, signUpBoxV, rightSpacer);
        signUpBox.setAlignment(Pos.CENTER);
        // Add hover effect on login box
        signUpBoxV.setOnMouseEntered(event -> {
            // Enlarge login box
            signUpBox.setScaleX(1.05);
            signUpBox.setScaleY(1.05);

            // Shrink side boxes
            leftSpacer.setScaleX(0.95);
            leftSpacer.setScaleY(0.95);
            rightSpacer.setScaleX(0.95);
            rightSpacer.setScaleY(0.95);
        });

        signUpBoxV.setOnMouseExited(event -> {
            // Reset login box size
            signUpBox.setScaleX(1.0);
            signUpBox.setScaleY(1.0);

            // Reset side box sizes
            leftSpacer.setScaleX(1.0);
            leftSpacer.setScaleY(1.0);
            rightSpacer.setScaleX(1.0);
            rightSpacer.setScaleY(1.0);
        });
        signUpBox.setTranslateX(1280);
        return signUpBox;
    }
    private HBox createLoginBox() {
        // Login box configuration
        VBox loginBoxV = new VBox(20);
        loginBoxV.setAlignment(Pos.CENTER);
        loginBoxV.setPadding(new Insets(20, 60, 20, 60));
        loginBoxV.setPrefSize(1280 * 0.5, 720 * 0.5);
        loginBoxV.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-border-color: linear-gradient(to bottom, #e41837, #0a0100);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-background-radius: 30;" +
            "-fx-transition: all 0.3s ease;" // Smooth transition for scaling and other properties
        );

        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        loginBoxV.setEffect(shadow);

        TextField usernameField = createTextField("Username");
        PasswordField passwordField = createPasswordField("Password");

        Button loginButton = createStyledButton("Login", "#e41837", "#541801");
        Button signInButton = createStyledButton("Sign Up", "#0a0100", "#6b6a6a");

        HBox buttonBox = new HBox(10, loginButton, signInButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Create rectangles for spacing on either side of the login box
        Rectangle leftSpacer = new Rectangle(400, 0); // Adjust width for left spacing
        leftSpacer.setFill(Color.TRANSPARENT);

        Rectangle rightSpacer = new Rectangle(400, 0); // Adjust width for right spacing
        rightSpacer.setFill(Color.TRANSPARENT);

        // Create an HBox to hold the spacers and the login box
        HBox centeredBox = new HBox(leftSpacer, loginBoxV, rightSpacer);
        centeredBox.setAlignment(Pos.CENTER);
        // Animation Trigger on Sign Up Button
        loginBox = centeredBox;
        loginButton.setOnAction(e -> {
            // Trigger both animations
            userType = system.Login(usernameField.getText().trim(), passwordField.getText().trim());
            if(userType == "Admin"){
                createAdminDashboard();
                loggedAdmin = system.getLoggedAdmin(usernameField.getText().trim(), passwordField.getText().trim());
                HBox titleBoxPrev = titleBox;
                titleBox = createTitleBoxLoggedIn();
                navigateToBox(titleBoxPrev, titleBox);
                navigateToBox(loginBox,dashBoard);
            }
            else if(userType == "Supplier"){
                createSupplierDashboard();
                loggedSupplier = system.getLoggedSupplier(usernameField.getText().trim(), passwordField.getText().trim());
                HBox titleBoxPrev = titleBox;
                titleBox = createTitleBoxLoggedIn();
                navigateToBox(titleBoxPrev, titleBox);
                navigateToBox(loginBox,dashBoard);
            }
            else if(userType == "Inventory Manager"){
                createInventoryManagerDashboard();
                loggedManager = system.getLoggedInventoryManager(usernameField.getText().trim(), passwordField.getText().trim());
                HBox titleBoxPrev = titleBox;
                titleBox = createTitleBoxLoggedIn();
                navigateToBox(titleBoxPrev, titleBox);
                navigateToBox(loginBox,dashBoard);
            }
            else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
            usernameField.clear();
            passwordField.clear();
        });
        signInButton.setOnAction(e -> {
            // Trigger both animations
            createSignUpBox();
            navigateToBox(loginBox, signUpBox);       // Move the login page to the left
        });
        // Add hover effect on login box
        loginBoxV.setOnMouseEntered(event -> {
            // Enlarge login box
            loginBoxV.setScaleX(1.05);
            loginBoxV.setScaleY(1.05);

            // Shrink side boxes
            leftSpacer.setScaleX(0.95);
            leftSpacer.setScaleY(0.95);
            rightSpacer.setScaleX(0.95);
            rightSpacer.setScaleY(0.95);
        });

        loginBoxV.setOnMouseExited(event -> {
            // Reset login box size
            loginBoxV.setScaleX(1.0);
            loginBoxV.setScaleY(1.0);

            // Reset side box sizes
            leftSpacer.setScaleX(1.0);
            leftSpacer.setScaleY(1.0);
            rightSpacer.setScaleX(1.0);
            rightSpacer.setScaleY(1.0);
        });
        loginBoxV.getChildren().addAll(usernameField, passwordField, buttonBox);
        return centeredBox;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-font-family: 'Gilroy-Medium'; -fx-font-size: 14px; -fx-prompt-text-fill: #888;");
        return textField;
    }

    private PasswordField createPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setStyle("-fx-font-family: 'Gilroy-Medium'; -fx-font-size: 14px; -fx-prompt-text-fill: #888;");
        return passwordField;
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        String style = String.format(
            "-fx-background-color: %s;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 20;" +
            "-fx-font-size: 16px;" +
            "-fx-border-radius: 30;" +
            "-fx-font-family: 'Gilroy-Medium';" +
            "-fx-background-radius: 30;" +
            "-fx-cursor: hand;" +
            "-fx-transition: all 0.3s ease;", // Smooth transition for scaling and other properties
            baseColor
        );

        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(style.replace(baseColor, hoverColor)));
        button.setOnMouseExited(e -> button.setStyle(style));
        return button;
    }

    private Group createCircleGroup() {

        Circle smallCircle = createStyledCircle(10);
        Circle mediumCircle = createStyledCircle(20);
        Circle largeCircle = createStyledCircle(30);

        largeCircle.setTranslateX(-40);
        largeCircle.setTranslateY(20);

        mediumCircle.setTranslateX(20);
        mediumCircle.setTranslateY(50);

        smallCircle.setTranslateX(60);
        smallCircle.setTranslateY(10);

        return new Group(largeCircle, mediumCircle, smallCircle);
    }

    private Group createMirroredCircleGroup() {
        Group originalGroup = createCircleGroup();

        Group mirroredGroup = new Group();
        for (Node node : originalGroup.getChildren()) {
            Circle original = (Circle) node;
            Circle mirrored = createStyledCircle(original.getRadius());;
            mirrored.setFill(original.getFill());
            mirrored.setStroke(original.getStroke());
            mirrored.setTranslateX(-original.getTranslateX() - 100);
            mirrored.setTranslateY(original.getTranslateY());
            mirroredGroup.getChildren().add(mirrored);
        }
        return mirroredGroup;
    }

    private Circle createStyledCircle(double radius) {
        Circle circle = new Circle(radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(3);
        circle.setStroke(createGradient());

        DropShadow shadow = new DropShadow(3, 3, 3, Color.web("#0a0100"));
        circle.setEffect(shadow);

        circle.setOnMouseEntered(event -> {
            circle.setScaleX(1.02);
            circle.setScaleY(1.02);
        });

        circle.setOnMouseExited(event -> {
            circle.setScaleX(1.0);
            circle.setScaleY(1.0);
        });

        return circle;
    }

    private LinearGradient createGradient() {
        return new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#e41837")),
            new Stop(1, Color.web("#0a0100"))
        );
    }
    private Text createText(String prompt){
        Text text = new Text(prompt);
        text.setFont(Font.font("Gilroy-Medium", 24));
        text.setFill(createGradient());
        return text;
    }
}
