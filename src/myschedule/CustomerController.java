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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private Map<String, Integer> cityToCityIdMap = new HashMap<>();
    private Map<String, Integer> countryToCountryIdMap = new HashMap<>();
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();

    // Lists
    private List addressList;
    private List cityList;
    private List countryList;
    private List customerList;
        
    private MainController main;
    private final boolean unsavedChanges = false;

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
        btnCancel.setOnMouseClicked((ea) -> {
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

        cboCustomer.setOnAction((ea) -> {
            try {
                loadCustomerData();
            }
            catch (SQLException ex) {
                app.common.alertStatus(0);
            }
        });
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
            addressToAddressIdMap = app.db.getAddressToAddressIdMap();
            cityToCityIdMap = app.db.getCityToCityIdMap();
            countryToCountryIdMap = app.db.getCountryToCountryIdMap();
            customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap();
            
            addressList = app.common.convertSIMapToList(addressToAddressIdMap);
            cityList = app.common.convertSIMapToList(cityToCityIdMap);
            countryList = app.common.convertSIMapToList(countryToCountryIdMap);
            customerList = app.common.convertSIMapToList(customerToCustomerIdMap);
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
