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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    @FXML private TableColumn<CityModel, Integer> countryIdColumn;
    @FXML private TableColumn<CityModel, String> createDateColumn;
    @FXML private TableColumn<CityModel, String> createdByColumn;
    @FXML private TableColumn<CityModel, String> lastUpdateColumn;
    @FXML private TableColumn<CityModel, String> lastUpdateByColumn;
    @FXML private TextField txtCityId;
    @FXML private TextField txtCity;
    @FXML private ComboBox cboCountryId;
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
    @FXML private VBox city;
    @FXML private HBox tableContainer;
    @FXML private HBox controlsContainer;
    @FXML private HBox buttonsContainer;
    
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
        
        // Country Id column
        
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
    private void configureTextFields() {
        txtCityId.setDisable(true);
        txtCity.setDisable(false);
        cboCountryId.setDisable(false);
        txtCreateDate.setDisable(true);
        txtCreatedBy.setDisable(true);
        txtLastUpdate.setDisable(true);
        txtLastUpdateBy.setDisable(true);
    }
    
    /**
     * Confirm closing when unsaved data exists
     * @return boolean
     */
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
    private void createActionListeners() {
        btnAdd.setOnAction((ae) -> {
            if (validateCityRecord()) {
                cityList.add(new CityModel(
                    Integer.parseInt(txtCityId.getText()),
                    txtCity.getText(),
                    cboCountryId.getSelectionModel().getSelectedItem(),
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
            closeCountryMaint();
        });
        
        btnCommit.setOnAction((ea) -> {
            try {
                app.db.updateCountries(countryList);
                unsavedChanges = false;
                alertStatus(1);
            }
            catch (SQLException ex) {
                alertStatus(0);
            }
        });

        btnRemove.setOnAction((ae) -> {
            ObservableList<CityModel> countrySelected, allCountries;
            allCountries = table.getItems();
            countrySelected = table.getSelectionModel().getSelectedItems();
            countrySelected.forEach(allCountries::remove);
            unsavedChanges = true;
        });
    }

    /**
     * Get next available Country Id to be use for add
     * @param clist
     * @return 
     */
    private int getNextCountryId(ObservableList<CityModel> clist) {
        Optional<CityModel> country = clist
            .stream()
            .max(Comparator.comparing(CityModel::getCountryId));
        return country.get().getCountryId() + 1;
    }

    /**
     * Set default values for new record
     */
    private void initializeNewRecord() {
        int nextCountryId = getNextCountryId(countryList);
        String now = app.common.now();
        String user = app.userName();
        
        txtCountryId.setText(Integer.toString(nextCountryId));
        txtCountry.setText("");
        txtCreateDate.setText(now);
        txtCreatedBy.setText(user);
        txtLastUpdate.setText(now);
        txtLastUpdateBy.setText(user);
    }
    
    /**
     * Inject App object
     * @param _app 
     */
    public void injectApp(App _app) {
        this.app = _app;
    }

    /**
     * Inject MainController object
     * @param _main 
     */
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
        lblTitle.setText(app.localize("countries"));
//        app.common.loadUsers();
        countryList = app.db.getCountries();
        initializeNewRecord();
        configureColumns();
        configureTextFields();
        table.setEditable(true);
        table.setItems(countryList);

    }
    
    /**
     * Validate new record data
     * @return 
     */
    private boolean validateCountryRecord() {
        return app.common.isNumber(txtCountryId.getText())
              && app.common.isString(txtCountry.getText())
              && app.common.isString(txtCreateDate.getText())   
              && app.common.isString(txtCreatedBy.getText())
              && app.common.isString(txtLastUpdate.getText())  
              && app.common.isString(txtLastUpdateBy.getText());

    }
}
