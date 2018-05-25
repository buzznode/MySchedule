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
import javafx.beans.property.StringProperty;
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
import myschedule.model.CityModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CityController {
    @FXML private Label lblTitle;
    @FXML private TableView table;
    @FXML private TableColumn<CityModel, Integer> cityIdColumn;
    @FXML private TableColumn<CityModel, String> cityColumn;
    @FXML private TableColumn<CityModel, String> countryColumn;
    @FXML private TableColumn<CityModel, String> createDateColumn;
    @FXML private TableColumn<CityModel, String> createdByColumn;
    @FXML private TableColumn<CityModel, String> lastUpdateColumn;
    @FXML private TableColumn<CityModel, String> lastUpdateByColumn;
    @FXML private TextField txtCityId;
    @FXML private TextField txtCity;
    @FXML private ComboBox cboCountry;
    @FXML private TextField txtCreateDate;
    @FXML private TextField txtCreatedBy;
    @FXML private TextField txtLastUpdate;
    @FXML private TextField txtLastUpdateBy;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnClose;
    @FXML private Button btnCommit;

    private App app;
    private ObservableList<CityModel> cityList = FXCollections.observableArrayList();
    private MainController main;
    private boolean unsavedChanges = false;

    /**
     * Alert status
     * @param status 
     */
    @SuppressWarnings("unchecked")
    private void alertStatus(int status) {
        if (status == 1) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Database commit was successful. Record added.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait();
        }
    }
    
    /**
     * Close country maintenance 
     */
    @SuppressWarnings("unchecked")
    private void closeCityMaint() {
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
     * Configure Cell Factories and Cell Value Factories
     */
    @SuppressWarnings("unchecked")
    private void configureColumns() {
        // City Id column
        cityIdColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCityId()));
        
        // City column
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CityModel, String> t) -> {
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCity(t.getNewValue());
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
        
        // Country column
        List countries = app.db.getCountryNames();
        
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.setCellFactory(ComboBoxTableCell.forTableColumn((ObservableList) countries));
        countryColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CityModel, String> t) -> {
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCountry(t.getNewValue());
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.now());
                ((CityModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
            
        // Create Date column
        createDateColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCreateDate()));

        // Created By column
        createdByColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCreatedBy()));

        // Last Update column
        lastUpdateColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getLastUpdate()));

        // Last Update By column
        lastUpdateByColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getLastUpdateBy()));
    }
    
    /**
     * Configure TextFields
     */
    @SuppressWarnings("unchecked")
    private void configureTextFields() {
        txtCityId.setDisable(true);
        txtCity.setDisable(false);
        cboCountry.setDisable(false);
        txtCreateDate.setDisable(true);
        txtCreatedBy.setDisable(true);
        txtLastUpdate.setDisable(true);
        txtLastUpdateBy.setDisable(true);
    }
    
    /**
     * Confirm closing when unsaved data exists
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    private boolean confirmUnsaved() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("Pending city changes exist.");
        alert.setContentText(
            "There have been changes made to the city data that have not been saved.\n\nTo save these changes, " +
            "click \"No\" to close this alert, and then click on the \"Commit\" button to save the changes.\n\n" +
            "Clicking \"Yes\" will result in the pending changes being lost and the city maintenance process ending."
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
        
        btnAdd.setOnAction((ae) -> {
            if (validateCityRecord()) {
                cityList.add(new CityModel(
                    Integer.parseInt(txtCityId.getText()),
                    txtCity.getText(),
                    (String) cboCountry.getValue(),
                    txtCreateDate.getText(),
                    txtCreatedBy.getText(),
                    txtLastUpdate.getText(),
                    txtLastUpdateBy.getText())
                );
                
                unsavedChanges = true;
                initializeNewRecord();
            }
            else {
                app.log.write(Level.SEVERE, "Error parsing new country record");
            }
        });
        
        btnClose.setOnMouseClicked((ea) -> {
            closeCityMaint();
        });
        
        btnCommit.setOnAction((ea) -> {
            try {
                String country = "";
                app.db.updateCities(cityList, country);
                unsavedChanges = false;
                alertStatus(1);
            }
            catch (SQLException ex) {
                alertStatus(0);
            }
        });

        
        btnRemove.setOnAction((ae) -> {
            ObservableList<CityModel> citySelected, allCities;
            allCities = table.getItems();
            citySelected = table.getSelectionModel().getSelectedItems();
            citySelected.forEach(allCities::remove);
            unsavedChanges = true;
        });
    }

    /**
     * Get next available Country Id to be use for add
     * @param clist
     * @return 
     */
    @SuppressWarnings("unchecked")
    private int getNextCityId(ObservableList<CityModel> clist) {
        Optional<CityModel> c = clist
            .stream()
            .max(Comparator.comparing(CityModel::getCityId));
        return c.get().getCityId() + 1;
    }

    /**
     * Set default values for new record
     */
    @SuppressWarnings("unchecked")
    private void initializeNewRecord() {
        int nextCityId = getNextCityId(cityList);
        String now = app.common.now();
        String user = app.userName();
        
        txtCityId.setText(Integer.toString(nextCityId));
        txtCity.setText("");
//        cboCountry=;
        txtCreateDate.setText(now);
        txtCreatedBy.setText(user);
        txtLastUpdate.setText(now);
        txtLastUpdateBy.setText(user);
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
     * Start country maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        createActionListeners();
//        btnCancel.setText(app.localize("cancel"));
//        btnLogin.setText(app.localize("login"));
        lblTitle.setText(app.localize("cities"));
//        app.common.loadUsers();
        cityList = app.db.getCities();
        initializeNewRecord();
        configureColumns();
        configureTextFields();
        table.setEditable(true);
        table.setItems(cityList);
    }
    
    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private boolean validateCityRecord() {
        return app.common.isNumber(txtCityId.getText())
              && app.common.isString(txtCity.getText())
//              && app.common.isString(cboCountry)
              && app.common.isString(txtCreateDate.getText())   
              && app.common.isString(txtCreatedBy.getText())
              && app.common.isString(txtLastUpdate.getText())  
              && app.common.isString(txtLastUpdateBy.getText());
    }
}
