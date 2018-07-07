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
import myschedule.model.AddressModel;
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
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private ComboBox cboCustomer;
    @FXML private ComboBox cboAddress;
    @FXML private CheckBox chkActive;
    @FXML private Label lblTitle;
    @FXML private TextField txtCity;
    @FXML private TextField txtCountry;
    @FXML private TextField txtCustomer;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPostalCode;

    private App app;
    private MainController main;
    
    // Maps
    private Map<String, Integer> addressToAddressIdMap = new HashMap<>();
    private Map<Integer, String> addressIdToAddressMap = new HashMap<>();
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();
    private Map<Integer, String> customerIdToCustomerMap = new HashMap<>();

    // Lists
    private List addressList;
    private List customerList;
        
    private final static String ADD_CUSTOMER = "----  Add New Customer  ----";
    private final AddressModel addressModel = new AddressModel();
    private boolean phoneChanged = false;
    private boolean postalCodeChanged = false;
    private final boolean unsavedChanges = false;

    /**
     *  Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnAddAddress.setOnAction(e -> { handleAddOther("addressMaint"); } );
        btnAddCity.setOnAction(e -> { handleAddOther("cityMaint"); } );
        btnAddCountry.setOnAction(e -> { handleAddOther("countryMaint"); } );
        btnCancel.setOnMouseClicked(e -> { closeCustomerMaint(); } );
        btnDelete.setOnAction(e -> { handleDelete(); }  );
        btnSave.setOnAction(e -> { handleSave(); } );
        cboAddress.setOnAction(e -> { handleAddressChange(); } );
        cboCustomer.setOnAction(e -> { handleCustomerChange(); } );
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            phoneChanged = (newValue == null ? oldValue != null : !newValue.equals(oldValue));
            phoneChanged = (oldValue == null || oldValue.isEmpty()) ? false : phoneChanged;
        });
        
        txtPostalCode.textProperty().addListener((observable, oldValue, newValue) -> {
            postalCodeChanged = (newValue == null ? oldValue != null : !newValue.equals(oldValue));
            postalCodeChanged = (oldValue == null || oldValue.isEmpty()) ? false : postalCodeChanged;
        });
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
            main.endProcess(routine, null);
        }
    }

    /**
     * Handle address change request
     */
    @SuppressWarnings("unchecked")
    private void handleAddressChange() {
        String address;
        int addressId;
        String address2;
        String addressLine;
        String city;
        int cityId;
        String country;
        int countryId;
        String phone;
        String postalCode;
        String rightNow;
        ResultSet rs;
        String userName;
        int userId;
        
        address = cboAddress.getValue().toString().trim();
        
        if (address.equals("")) {
            return;
        }
        
        addressId = addressToAddressIdMap.get(address);
    
        try {
            rs = app.db.getAddressData(addressId);
            
            address = rs.getString("address").trim();
            address2 = rs.getString("address2").trim();
            addressLine = (!address.isEmpty()) && (!address2.isEmpty()) ? address + " " + address2 :
                          (!address.isEmpty()) && (address2.isEmpty())  ? address  :
                          (address.isEmpty())  && (!address2.isEmpty()) ? address2 : "";
            addressLine += " | ";
            addressLine += String.join(" | ", 
                rs.getString("city"),
                rs.getString("country"),
                rs.getString("postalCode"),
                rs.getString("phone")
            );
            
            city = rs.getString("city").trim();
            txtCity.setText(city);
            cityId = rs.getInt("cityId");
            country = rs.getString("country").trim();
            countryId = rs.getInt("countryId");
            txtCountry.setText(country);
            phone = rs.getString("phone").trim();
            txtPhone.setText(phone);
            postalCode = rs.getString("postalCode");
            txtPostalCode.setText(postalCode);
            
            rightNow = app.common.rightNow();
            userName = app.userName();
            userId = app.userId();
            addressModel.setAddressId(addressId);
            addressModel.setAddress(address);
            addressModel.setAddress2(address2);
            addressModel.setCity(city);
            addressModel.setCityId(cityId);
            addressModel.setCountry(country);
            addressModel.setCountryId(countryId);
            addressModel.setPostalCode(postalCode);
            addressModel.setPhone(phone);
            addressModel.setCreateDate(rightNow);
            addressModel.setCreatedBy(userName);
            addressModel.setLastUpdate(rightNow);
            addressModel.setLastUpdateBy(userName);
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * Handle Customer ComboBox onChange
     */
    @SuppressWarnings("unchecked")
    private void handleCustomerChange() {
        String address;
        int addressId;
        int customerId;
        String customerName;
        ResultSet rs;
        
        customerName = cboCustomer.getValue().toString();
        
        if (customerName.equals(ADD_CUSTOMER)) {
            txtCustomer.requestFocus();
            cboAddress.setValue("");
        }
        else if (!customerName.isEmpty()) {
            customerId = customerToCustomerIdMap.get(customerName);

            try {
                rs = app.db.getCustomerData(customerId);
                chkActive.setSelected(rs.getBoolean("active"));
                txtCustomer.setText(rs.getString("customerName").trim());
                addressId = rs.getInt("addressId");
                address = addressIdToAddressMap.get(addressId);
                cboAddress.setValue(address);
            }
            catch (SQLException ex) {
            }
        }
    }

    /**
     * Handle Delete Customer
     */
    @SuppressWarnings("unchecked")
    private void handleDelete() {
        int customerId;
        String customerName;
        
        if (cboCustomer.getSelectionModel().getSelectedIndex() < 0) {
            app.common.alertStatus(0, "Customer Required", "You must select a customer to delete.");
            return;
        }
        
        customerName = cboCustomer.getValue().toString().trim();
        customerId = customerToCustomerIdMap.get(customerName);
        app.db.deleteCustomer(customerId);
        customerToCustomerIdMap.remove(customerName);
        customerIdToCustomerMap.remove(customerId);
        cboCustomer.getItems().remove(cboCustomer.getSelectionModel().getSelectedIndex());
    }
    
    /**
     * Handle Customer Save
     */
    @SuppressWarnings("unchecked")
    private void handleSave() {
        boolean active;
        String address;
        String address2;
        String addressLine;
        int addressId = 0;
        String customer;
        int customerId;
        String customerName;
        int id = 0;

        if (validateForm()) {
            if (phoneChanged || postalCodeChanged) {
                try {
                    addressModel.setPhone(txtPhone.getText().trim());
                    addressModel.setPostalCode(txtPostalCode.getText().trim());
                    addressId = app.db.upsertAddress(addressModel);
                    
                    address = addressModel.getAddress().trim();
                    address2 = addressModel.getAddress2().trim();
                    addressLine = (!address.isEmpty()) && (!address2.isEmpty()) ? address + " " + address2 :
                                               (!address.isEmpty()) && (address2.isEmpty())  ? address  :
                                               (address.isEmpty())  && (!address2.isEmpty()) ? address2 : "";
                    addressLine += " | ";
                    addressLine += String.join(" | ", 
                        addressModel.getCity().trim(),
                        addressModel.getCountry().trim(),
                        addressModel.getPostalCode().trim(),
                        addressModel.getPhone().trim()
                    );

                    addressToAddressIdMap.put(addressLine, addressId);
                    addressIdToAddressMap.put(addressId, addressLine);
                    
                    addressList.stream().filter((item) -> (!item.equals(""))).forEachOrdered((item) -> {
                        cboAddress.getItems().remove(item);
                    });
                    
                    addressList.add(addressLine);
                    
                    addressList.stream().filter((item) -> (!item.equals(""))).forEachOrdered((item) -> {
                        cboAddress.getItems().add(item);
                    });
                }
                catch (SQLException ex) {
                    app.common.alertStatus(0, "Error performing upsertAddress");
                }
                phoneChanged = false;
                postalCodeChanged = false;
            }

            active = chkActive.isSelected();
            addressId = addressId > 0 ? addressId :  addressToAddressIdMap.get(cboAddress.getValue().toString());
            customer = cboCustomer.getValue().toString();
            customerId = customer.equals(ADD_CUSTOMER) ?  0 : customerToCustomerIdMap.get(customer);
            customerName = txtCustomer.getText();

            CustomerModel customerRecord = new CustomerModel();
            customerRecord.setActive(active);
            customerRecord.setCustomerId(customerId);
            customerRecord.setCustomerName(customerName);
            customerRecord.setAddressId(addressId);

            try {
                app.db.upsertCustomer(customerRecord, app.userName());
            }
            catch (SQLException ex) {
                app.common.alertStatus(0);
            }

            app.common.alertStatus(1);
            customerToCustomerIdMap.put(customerName, id);
            customerIdToCustomerMap.put(id, customerName);

            customerList.stream().filter((item) -> (!item.equals(ADD_CUSTOMER))).forEachOrdered((item) -> {
                cboCustomer.getItems().remove(item);
            });
                    
            customerList.add(customerName);

            customerList.stream().filter((item) -> (!item.equals(ADD_CUSTOMER))).forEachOrdered((item) -> {
                cboCustomer.getItems().add(item);
            });

            initializeForm();
        }
    }
    
    /**
     * Initialize "add record" form elements
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
        cboAddress.setEditable(true);
        cboCustomer.setEditable(true);
        cboAddress.setValue("");
        cboCustomer.setValue("");
        
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
     * Load customer record
     * @param customerId 
     */
    @SuppressWarnings("unchecked")
    public void loadCustomer(int customerId) {
        String customerName;
        
        customerName = customerIdToCustomerMap.get(customerId);
        cboCustomer.setValue(customerName);
        handleCustomerChange();
        handleAddressChange();
    }
    
    /**
     * Load Maps and Lists
     */
    @SuppressWarnings("unchecked")
    private void loadMapsAndLists() {
        try {
            addressToAddressIdMap = app.db.getAddressToAddressIdMap();
            addressIdToAddressMap = app.db.getAddressIdToAddressMap();
            customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap();
            customerIdToCustomerMap = app.db.getCustomerIdToCustomerMap();
            
            addressList = app.common.createAddressList(addressToAddressIdMap);
            customerList = app.common.createCustomerList(customerToCustomerIdMap);
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }

        cboAddress.getItems().addAll(addressList);
        cboCustomer.getItems().addAll(customerList);
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
     * Start address maintenance with customer
     * @param customerId 
     */
    @SuppressWarnings("unchecked")
    public void start(int customerId) {
        addListeners();
        lblTitle.setText(app.localize("customers"));
        loadMapsAndLists();
        initializeForm();
        loadCustomer(customerId);
    }
    
    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private boolean validateForm() {
        return app.common.isValidString(txtCustomer.getText(), false)
              && app.common.isValidString(cboAddress.getValue().toString(), false)
              && app.common.isValidString(txtCity.getText(), false)
              && app.common.isValidString(txtPostalCode.getText(), false)
              && app.common.isValidString(txtPhone.getText(), false)
              && app.common.isValidString(txtCountry.getText(), false);
    }
}
