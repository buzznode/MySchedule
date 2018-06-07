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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import myschedule.model.AddressModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class AddressController {
    @FXML private Label lblTitle;
    @FXML private TableView table;
    @FXML private TableColumn<AddressModel, Integer> addressIdColumn;
    @FXML private TableColumn<AddressModel, String> addressColumn;
    @FXML private TableColumn<AddressModel, String> address2Column;
    @FXML private TableColumn<AddressModel, String> cityColumn;
    @FXML private TableColumn<AddressModel, String> postalCodeColumn;
    @FXML private TableColumn<AddressModel, String> phoneColumn;
    @FXML private TableColumn<AddressModel, String> countryColumn;
    @FXML private TableColumn<AddressModel, String> createDateColumn;
    @FXML private TableColumn<AddressModel, String> createdByColumn;
    @FXML private TableColumn<AddressModel, String> lastUpdateColumn;
    @FXML private TableColumn<AddressModel, String> lastUpdateByColumn;
    @FXML private TextField txtAddressId;
    @FXML private TextField txtAddress;
    @FXML private TextField txtAddress2;
    @FXML private ComboBox cboCity;
    @FXML private TextField txtPostalCode;
    @FXML private TextField txtPhone;
    @FXML private TextField txtCountry;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnClose;
    @FXML private Button btnCommit;

    private App app;
    private ObservableList<AddressModel> addressList = FXCollections.observableArrayList();
    private List cityNameList;
        
    private MainController main;
    private boolean unsavedChanges = false;

    /**
     * Check for un-saved changes; display warning message
     * as needed; close city maintenance function.
     */
    @SuppressWarnings("unchecked")
    private void closeAddressMaint() {
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
            "There have been changes made to the address data that have not been saved.\n\nTo save these changes, " +
            "click \"No\" to close this alert, and then click on the \"Commit\" button to save the changes.\n\n" +
            "Clicking \"Yes\" will result in the pending changes being lost and the address maintenance process ending."
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
        btnAdd.setOnAction(e -> {
            handleAddAddress();
        });
        
        btnClose.setOnMouseClicked((ea) -> {
            closeAddressMaint();
        });
        
        btnCommit.setOnAction((ea) -> {
            try {
                app.db.updateAddressTable(addressList);
                unsavedChanges = false;
                app.common.alertStatus(1);
                refreshTableView();
            }
            catch (SQLException ex) {
                app.common.alertStatus(0);
            }
        });

        btnRemove.setOnAction((ae) -> {
            ObservableList<AddressModel> addressSelected, allAddresses;
            allAddresses = table.getItems();
            addressSelected = table.getSelectionModel().getSelectedItems();
            addressSelected.forEach(allAddresses::remove);
            unsavedChanges = true;
        });
    }

    /**
     * Get next available Address Id to be use for add
     * @param clist
     * @return 
     */
    @SuppressWarnings("unchecked")
    private int getNextAddressId(ObservableList<AddressModel> alist) {
        if (alist.size() > 0) {
            Optional<AddressModel> a = alist
                .stream()
                .max(Comparator.comparing(AddressModel::getAddressId));
            return a.get().getAddressId() + 1;
        }
        else {
            return 1;
        }
    }

    private void handleAddAddress() {
        int cityId = 0;
        int countryId = 0;
        String now = app.common.now();
        String user = app.userName();

        try {
            cityId = app.db.getACityId(cboCity.getValue().toString());
            countryId = app.db.getACountryId(txtCountry.getText());
        }
        catch (SQLException ex) {
            
        }
        
        if (validateAddressRecord()) {
            addressList.add(new AddressModel(
                Integer.parseInt(txtAddressId.getText()), txtAddress.getText(), 
                txtAddress2.getText(), cboCity.getValue().toString(), cityId, 
                txtPostalCode.getText(), txtPhone.getText(), txtCountry.getText(), 
                countryId, now, user, now, user));

            unsavedChanges = true;
            initializeForm();
        }
        else {
            app.log.write(Level.SEVERE, "Error parsing new city record");
        }
    }
    
    /**
     * Initialize "add record" form elements
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
        int nextAddressId = getNextAddressId(addressList);

        txtAddressId.setText(Integer.toString(nextAddressId));
        txtAddress.setText("");
        txtAddress2.setText("");
        cboCity.getItems().addAll(cityNameList);
        txtPostalCode.setText("");
        txtPhone.setText("");
        txtCountry.setText("");
        
        txtAddressId.setDisable(true);
        txtAddress.setDisable(false);
        txtAddress2.setDisable(false);
        cboCity.setDisable(false);
        txtPostalCode.setDisable(false);
        txtPhone.setDisable(false);
        txtCountry.setDisable(true);
    }
    
    /**
     * Initialize Cell Factories and Cell Value Factories
     */
    @SuppressWarnings("unchecked")
    private void initializeTableViewColumns() {
        // Address Id column
        addressIdColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getAddressId()));
        
        // Address column
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addressColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<AddressModel, String> t) -> {
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );

        // Address2 column
        address2Column.setCellValueFactory(new PropertyValueFactory<>("address2"));
        address2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        address2Column.setOnEditCommit(
            (TableColumn.CellEditEvent<AddressModel, String> t) -> {
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress2(t.getNewValue());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
        
        // City column
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setCellFactory(ComboBoxTableCell.forTableColumn((ObservableList) cityNameList));
        cityColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<AddressModel, String> t) -> {
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCity(t.getNewValue());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );

        // PostalCode column
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        postalCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        postalCodeColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<AddressModel, String> t) -> {
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPostalCode(t.getNewValue());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
        
        // Phone column
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<AddressModel, String> t) -> {
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone(t.getNewValue());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((AddressModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
        
        // Country column
        countryColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCountry()));
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
     * Refresh Address TableView
     */
    @SuppressWarnings("unchecked")
    private void refreshTableView() {
        try {
            addressList = app.db.getAddressModelList("address", "asc");
            table.setItems(addressList);
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * Start address maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        createActionListeners();
        lblTitle.setText(app.localize("addresses"));
        
        try {
            addressList = app.db.getAddressModelList("address", "asc");
            cityNameList = app.db.getCityNameList();
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
        initializeForm();
        initializeTableViewColumns();
        table.setEditable(true);
        table.setItems(addressList);
    }
    
    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private boolean validateAddressRecord() {
        return app.common.isNumber(txtAddressId.getText())
              && app.common.isString(txtAddress.getText())
              && app.common.isString(txtAddress2.getText())
              && app.common.isString((String) cboCity.getValue())
              && app.common.isString(txtPostalCode.getText())
              && app.common.isString(txtPhone.getText());
    }
}
