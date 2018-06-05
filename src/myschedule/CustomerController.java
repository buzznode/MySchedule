/*
 * The MIT License
 *
 * Copyright 2018 bradd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package myschedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CustomerController {
    @FXML private Label lblTitle;
    @FXML private Label lblCustomer;
    @FXML private ComboBox cboCustomer;
    @FXML private CheckBox chkActive;
    @FXML private TextField txtCustomer;
    @FXML private ComboBox cboAddress;
    @FXML private TextField txtAddress;
    @FXML private TextField txtAddress2;
    @FXML private ComboBox cboCity;
    @FXML private TextField txtCity;
    @FXML private TextField txtPostalCode;
    @FXML private TextField txtPhone;
    @FXML private ComboBox cboCountry;
    @FXML private TextField txtCountry;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private App app;
    
    // Maps
    private Map<String, Integer> addressToAddressIdMap = new HashMap<>();
    private Map<Integer, String> addressIdToAddressMap = new HashMap<>();
    private Map<String, Integer> cityToCityIdMap = new HashMap<>();
    private Map<Integer, String> cityIdToCityMap = new HashMap<>();
    private Map<String, Integer> countryToCountryIdMap = new HashMap<>();
    private Map<Integer, String> countryIdToCountryMap = new HashMap<>();
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();
    private Map<Integer, String> customerIdToCustomerMap = new HashMap<>();

    // Lists
    private List addressList;
    private List cityList;
    private List countryList;
    private List customerList;
        
    private MainController main;
    private final boolean unsavedChanges = false;

    /**
     * Add new Customer 
     */
    @SuppressWarnings("unchecked")
    private void addNewCustomer() {
        System.out.println("Adding a new customer record");
    }
    
    /**
     * Check for un-saved changes; display warning message
     * as needed; close city maintenance function.
     */
    @SuppressWarnings("unchecked")
    private void closeCustomerMaint() {
        if (unsavedChanges) {
            if (confirmUnsaved()) {
                main.endProcess();
            }
        }
        else {
            main.endProcess();
        }
    }
    
    /**
     * Confirm closing when unsaved data exists
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    private boolean confirmUnsaved() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("Pending address changes exist.");
        alert.setContentText(
            "There have been changes made to the customer data that have not been saved.\n\nTo save these changes, " +
            "click \"No\" to close this alert, and then click on the \"Commit\" button to save the changes.\n\n" +
            "Clicking \"Yes\" will result in the pending changes being lost and the customer maintenance process ending."
        );
        
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnYes, btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == btnYes;
    }
    
    /**
     *  Create action event listeners
     */
    @SuppressWarnings("unchecked")
    private void createActionListeners() {
        // USE BUTTONS TO ADD NEW (ADDRESS< CITY< COUNTRY, etc
        
        btnCancel.setOnMouseClicked((ae) -> {
            closeCustomerMaint();
        });

//        btnSave.setOnAction((ea) -> {
//            try {
//                app.db.upsertCustomer(addressList);
//                unsavedChanges = false;
//                app.common.alertStatus(1);
//            }
//            catch (SQLException ex) {
//                app.common.alertStatus(0);
//            }
//        });

        cboAddress.setOnAction(e -> {
            handleAddressChange();
        });

//        cboAddress.getSelectionModel().selectedItemProperty().addListener(e -> {
//            handleIt(e);
//        });
        
//        cboAddress.setOnAction((ae) -> {
//            handleAddressChange(ae);
//        });
        
        cboCity.setOnAction((ae) -> {
            handleCityChange();
            ae.consume();
        });
        
        cboCustomer.setOnAction((ae) -> {
            handleCustomerChange();
            ae.consume();
        });
    }
    
    /**
     * Edit existing Customer
     * @param customerName 
     */
    @SuppressWarnings("unchecked")
    private void editCustomer(String customerName) {
        int customerId;
        ResultSet rs;
        
        customerId = customerToCustomerIdMap.get(customerName);
        
        try {
            rs = app.db.getCustomerData(customerId);
            chkActive.setSelected(rs.getBoolean("active"));
            txtCustomer.setText(rs.getString("customerName"));
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * Handle Address ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private void handleAddressChange() {
        String value = cboAddress.getValue().toString();
        
        if (value.equals("----  Select Address  ----")) {
            return;
        }
        
        if (value.equals("----  Add New  ----")) {
            String hdr = "You are about to leave Customer Maintenance. Any unsaved changes will be lost.";
            String msg = "Are you sure you wish to continue?";
            
            if (app.common.displayConfirmation(hdr, msg)) {
                main.endProcess("addressMaint");
            }
            else {
                cboAddress.setOnAction(null);
                cboAddress.setValue(null);
                cboAddress.setOnAction(e -> {
                    e.consume();
                    handleAddressChange();
                });
            }
        }
    }
    
    /**
     * Handle City ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private void handleCityChange() {
        String value = cboCity.getValue().toString();
        
        if (value.equals("----  Add New  ----")) {
            String hdr = "You are about to leave Customer Maintenance. Any unsaved changes will be lost.";
            String msg = "Are you sure you wish to continue?";
            
            if (app.common.displayConfirmation(hdr, msg)) {
                main.endProcess("cityMaint");
            }
            else {
                cboCity.setValue("----  Select City  ----");
            }
        }
    }
    
    /**
     * Handle Country ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private void handleCountryChange() {
        String value = cboCountry.getValue().toString();
        
        if (value.equals("----  Add New  ----")) {
            String hdr = "You are about to leave Customer Maintenance. Any unsaved changes will be lost.";
            String msg = "Are you sure you wish to continue?";
            
            if (app.common.displayConfirmation(hdr, msg)) {
                main.endProcess("countryMaint");
            }
            else {
                cboCountry.setValue("----  Select Country  ----");
            }
        }
    }
    
    /**
     * Handle Customer ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private void handleCustomerChange() {
        String value = cboCustomer.getValue().toString();

        if (value.equals("----  Add New  ----")) {
            addNewCustomer();
        }
        else {
            editCustomer(value);
        }
    }

    /**
     * Get next available Address Id to be use for add
     * @param list
     * @return 
     */
//    @SuppressWarnings("unchecked")
//    private int getNextAddressId(ObservableList<AddressModel> alist) {
//        if (alist.size() > 0) {
//            Optional<AddressModel> a = alist
//                .stream()
//                .max(Comparator.comparing(AddressModel::getAddressId));
//            return a.get().getAddressId() + 1;
//        }
//        else {
//            return 1;
//        }
//    }

    /**
     * Initialize "add record" form elements
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
//        int nextAddressId = getNextAddressId(addressList);

        try {
            // Load Maps
            addressToAddressIdMap = app.db.getAddressToAddressIdMap();
            addressIdToAddressMap = app.db.getAddressIdToAddressMap();
            cityToCityIdMap = app.db.getCityToCityIdMap();
            cityIdToCityMap = app.db.getCityIdToCityMap();
            countryToCountryIdMap = app.db.getCountryToCountryIdMap();
            countryIdToCountryMap = app.db.getCountryIdToCountryMap();
            customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap();
            customerIdToCustomerMap = app.db.getCustomerIdToCustomerMap();
            
            // Load Lists from Maps
            addressList = app.common.convertSIMapToList(addressToAddressIdMap);
            cityList = app.common.convertSIMapToList(cityToCityIdMap);
            countryList = app.common.convertSIMapToList(countryToCountryIdMap);
            customerList = app.common.convertSIMapToList(customerToCustomerIdMap);
            
            // Put "Add New" options into Lists
            addressList.add(0, "----  Add New  ----");
            cityList.add(0, "----  Add New  ----");
            countryList.add(0, "----  Add New  ----");
            customerList.add(0, "----  Add New  ----");
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
        
        chkActive.setSelected(false);
        txtCustomer.setText("");
        txtAddress.setText("");
        txtAddress2.setText("");
        txtPostalCode.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtAddress2.setText("");
        txtCity.setVisible(false);
        txtCountry.setVisible(false);

        cboAddress.getItems().addAll(addressList);
        cboCity.getItems().addAll(cityList);
        cboCountry.getItems().addAll(countryList);
        cboCustomer.getItems().addAll(customerList);
        
        cboAddress.setValue("----  Select Address  ----");
        cboCity.setValue("----  Select City  ----");
        cboCountry.setValue("----  Select Country  ----");
        cboCustomer.setValue("----  Select Customer  ----");
    }
    
    /**
     * Inject App object
     * @param _app 
     */
    @SuppressWarnings("unchecked")
    public void injectApp(App _app) {
        this.app = _app;
    }

    /**
     * Inject MainController object
     * @param _main 
     */
    @SuppressWarnings("unchecked")
    public void injectMainController(MainController _main) {
        main = _main;
    }

    @SuppressWarnings("unchecked")
    public void loadCustomerData() throws SQLException {
        
    }
    
    /**
     * Start address maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        createActionListeners();
        lblTitle.setText(app.localize("customers"));
        initializeForm();
    }
    
    /**
     * Validate new record data
     * @return 
     */
//    @SuppressWarnings("unchecked")
//    private boolean validateAddressRecord() {
//        return app.common.isNumber(txtAddressId.getText())
//              && app.common.isString(txtAddress.getText())
//              && app.common.isString(txtAddress2.getText())
//              && app.common.isString((String) cboCity.getValue())
//              && app.common.isString(txtPostalCode.getText())
//              && app.common.isString(txtPhone.getText());
//    }
}
