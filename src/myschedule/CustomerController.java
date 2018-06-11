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
import myschedule.model.CustomerModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CustomerController {
    @FXML private Button btnAddAddress;
    @FXML private Button btnAddCity;
    @FXML private Button btnAddCountry;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private ComboBox cboCustomer;
    @FXML private ComboBox cboAddress;
    @FXML private CheckBox chkActive;
    @FXML private Label lblAddress;
    @FXML private Label lblCity;
    @FXML private Label lblCountry;
    @FXML private Label lblCustomer;
    @FXML private Label lblPhone;
    @FXML private Label lblPostalCode;
    @FXML private Label lblTitle;
    @FXML private TextField txtAddress;
    @FXML private TextField txtCity;
    @FXML private TextField txtCountry;
    @FXML private TextField txtCustomer;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPostalCode;

    private App app;
    
    // Maps
    private Map<String, Integer> addressToAddressIdMap = new HashMap<>();
    private Map<Integer, String> addressIdToAddressMap = new HashMap<>();
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();
    private Map<Integer, String> customerIdToCustomerMap = new HashMap<>();

    // Lists
    private List addressList;
    private List customerList;
        
    private MainController main;
    private final boolean unsavedChanges = false;
    private boolean phoneChanged = false;
    private boolean postalCodeChanged = false;
    private final static String ADD_CUSTOMER = "----  Add New Customer  ----";

    /**
     *  Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnAddAddress.setOnAction(e -> { handleAddOther("addressMaint"); } );
        btnAddCity.setOnAction(e -> { handleAddOther("cityMaint"); } );
        btnAddCountry.setOnAction(e -> { handleAddOther("countryMaint"); } );
        btnCancel.setOnMouseClicked(e -> { closeCustomerMaint(); } );
        btnSave.setOnAction(e -> { handleSave(); } );
        cboAddress.setOnAction(e -> { handleAddressChange(); } );
        cboCustomer.setOnAction(e -> { 
            int addressId = handleCustomerChange();
            
            if (addressId != 0) {
                handleAddressChange(addressId);
            }
        } );
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            phoneChanged = (newValue == null ? oldValue != null : !newValue.equals(oldValue));
        });
        
        txtPostalCode.textProperty().addListener((observable, oldValue, newValue) -> {
            postalCodeChanged = (newValue == null ? oldValue != null : !newValue.equals(oldValue));
        });
    }
    
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
     * Fires off other Maintenance routines
     * @param routine 
     */
    @SuppressWarnings("unchecked")
    private void handleAddOther(String routine) {
        String hdr = "You are about to leave Customer Maintenance. Any unsaved changes will be lost.";
        String msg = "Are you sure you want to continue?";

        if (app.common.displayConfirmation(hdr, msg)) {
            main.endProcess(routine);
        }
    }

    /**
     * Handle address change request
     */
    @SuppressWarnings("unchecked")
    private void handleAddressChange() {
        String address;
        int addressId;
        
        address = cboAddress.getValue().toString().trim();
        addressId = addressToAddressIdMap.get(address);
        handleAddressChange(addressId);
    }
    
    /**
     * Handle address change request using addressId
     * @param addressId 
     */
    @SuppressWarnings("unchecked")
    private void handleAddressChange(int addressId) {
        String address;
        String address2;
        String addressLine;
        String city;
        int cityId;
        String country;
        int countryId;
        String phone;
        String postalCode;
        ResultSet rs;
        
        try {
            rs = app.db.getAddressData(addressId);
            address = rs.getString("address").trim();
            address2 = rs.getString("address2").trim();
            addressLine = (!address.isEmpty()) && (!address2.isEmpty()) ? address + " " + address2 :
                          (!address.isEmpty()) && (address2.isEmpty())  ? address  :
                          (address.isEmpty())  && (!address2.isEmpty()) ? address2 : "";
            cboAddress.setValue(addressLine);
            city = rs.getString("city").trim();
            txtCity.setText(city);
            country = rs.getString("country").trim();
            txtCountry.setText(country);
            phone = rs.getString("phone").trim();
            txtPhone.setText(phone);
            postalCode = rs.getString("postalCode");
            txtPostalCode.setText(postalCode);
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * Handle Customer ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private int handleCustomerChange() {
        int addressId = 0;
        int customerId;
        String customerName;
        ResultSet rs;
        
        customerName = cboCustomer.getValue().toString();
        
        if (customerName.equals(ADD_CUSTOMER)) {
            initializeForm();
            txtCustomer.requestFocus();
        }
        else if (!customerName.isEmpty()) {
            customerId = customerToCustomerIdMap.get(customerName);

            try {
                rs = app.db.getCustomerData(customerId);
                chkActive.setSelected(rs.getBoolean("active"));
                txtCustomer.setText(rs.getString("customerName").trim());
                addressId = rs.getInt("addressId");
            }
            catch (SQLException ex) {
            }
            return addressId;
        }
        return 0;
    }

    /**
     * Handle Customer Save
     */
    @SuppressWarnings("unchecked")
    private void handleSave() {
        boolean active;
        int addressId;
        String customer;
        int customerId;
        String customerName;
        int rows;
        
        active = chkActive.isSelected();
        addressId = addressToAddressIdMap.get(cboAddress.getValue().toString());
        customer = cboCustomer.getValue().toString();
        customerId = customer.equals(ADD_CUSTOMER) ? 
            0 : customerToCustomerIdMap.get(customer);
        customerName = txtCustomer.getText();
        
        CustomerModel record = new CustomerModel();
        record.setActive(active);
        record.setCustomerId(customerId);
        record.setCustomerName(customerName);
        record.setAddressId(addressId);

//        System.out.println("phoneChanged: " + phoneChanged);
//        System.out.println("postalCodeChanged: " + postalCodeChanged);
        
        try {
            rows = app.db.upsertCustomer(record, app.userName());
            
            if (rows > 0) {
                app.common.alertStatus(1);
            }
            else {
                app.common.alertStatus(0);
            }
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
    }
    
    /**
     * Initialize "add record" form elements
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
        chkActive.setSelected(false);
        txtCity.setText("");
        txtCountry.setText("");
        txtCustomer.setText("");
        txtPostalCode.setText("");
        txtPhone.setText("");
        
        txtCity.setEditable(false);
        txtCountry.setEditable(false);
        txtPhone.setEditable(true);
        txtPostalCode.setEditable(true);
        
        cboAddress.getItems().addAll(addressList);
        cboCustomer.getItems().addAll(customerList);
        
//        cboAddress.setValue("----  Select Address  ----");
//        cboCustomer.setValue("----  Select Customer  ----");
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

    /**
     * Load Maps and Lists
     */
    @SuppressWarnings("unchecked")
    private void loadMapsAndLists() {
        try {
            // Load Maps
            addressToAddressIdMap = app.db.getAddressToAddressIdMap();
            addressIdToAddressMap = app.db.getAddressIdToAddressMap();
            customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap();
            customerIdToCustomerMap = app.db.getCustomerIdToCustomerMap();
            
            // Load Lists from Maps
            addressList = app.common.convertSIMapToList(addressToAddressIdMap);
            customerList = app.common.convertSIMapToList(customerToCustomerIdMap);
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
    }
        
    /**
     * Start address maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        lblTitle.setText(app.localize("customers"));
        loadMapsAndLists();
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
