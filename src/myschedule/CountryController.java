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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import myschedule.model.CountryModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CountryController {

    @FXML private Label lblTitle;
    @FXML private HBox tableContainer;
    @FXML private TableView table;
    @FXML private TableColumn<CountryModel, Integer> countryIdColumn;
    @FXML private TableColumn<CountryModel, String> countryColumn;
    @FXML private TableColumn<CountryModel, String> createDateColumn;
    @FXML private TableColumn<CountryModel, String> createdByColumn;
    @FXML private TableColumn<CountryModel, String> lastUpdateColumn;
    @FXML private TableColumn<CountryModel, String> lastUpdateByColumn;
    @FXML private HBox controlsContainer;
    @FXML private TextField txtCountryId;
    @FXML private TextField txtCountry;
    @FXML private TextField txtCreateDate;
    @FXML private TextField txtCreatedBy;
    @FXML private TextField txtLastUpdate;
    @FXML private TextField txtLastUpdateBy;
    @FXML private HBox buttonsContainer;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnCancel;
    @FXML private Button btnCommit;

    private App app;
    private ObservableList<CountryModel> countryList = FXCollections.observableArrayList();
    private MainController main;
    
    /**
     * Close country maintenance 
     */
    private void closeCountryMaint() {

        main.endProcess();

    }
    
    /**
     *  Create action event listeners
     */
    private void createActionListeners() {
        
        btnAdd.setOnAction((ae) -> {
            if (validateRecord()) {
                countryList.add(new CountryModel(
                    Integer.parseInt(txtCountryId.getText()),
                    txtCountry.getText(),
                    txtCreateDate.getText(),
                    txtCreatedBy.getText(),
                    txtLastUpdate.getText(),
                    txtLastUpdateBy.getText())
                );
                
                initializeNewRecord();
            }
            else {
                app.log.write(Level.SEVERE, "Error parsing new country record");
            }
        });
        
        btnCancel.setOnMouseClicked((ea) -> {
            closeCountryMaint();
        });
        
        btnCommit.setOnAction((ea) -> {
            try {
                app.db.updateCountries(countryList);
            }
            catch (SQLException ex) {
                
            }
        });

        btnRemove.setOnAction((ae) -> {
            ObservableList<CountryModel> countrySelected, allCountries;
            allCountries = table.getItems();
            countrySelected = table.getSelectionModel().getSelectedItems();
            countrySelected.forEach(allCountries::remove);
        });

    }

    /**
     * Get next available Country Id to be use for add
     * @param clist
     * @return 
     */
    private int getNextCountryId(ObservableList<CountryModel> clist) {

        Optional<CountryModel> country = clist
            .stream()
            .max(Comparator.comparing(CountryModel::getCountryId));
        return country.get().getCountryId() + 1;

    }

    /**
     * Set default values for new record
     */
    private void initializeNewRecord() {

        int nextCountryId = getNextCountryId(countryList);
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String user = app.userName();
        
        txtCountryId.setDisable(false);
        txtCountryId.setText(Integer.toString(nextCountryId));
        txtCountryId.setDisable(true);
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
     * Setup Cell Factories and Cell Value Factories
     */
    private void setupColumns() {

        // Country Id column
        countryIdColumn.setCellValueFactory(new PropertyValueFactory("countryId"));
        
        // Country column
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCountry(t.getNewValue());
        });
        
        // Create Date column
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        createDateColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCreateDate(t.getNewValue());
        });

        // Created By column
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        createdByColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        createdByColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCreatedBy(t.getNewValue());
        });

        // Last Update column
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastUpdateColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(t.getNewValue());
        });

        // Last Update By column
        lastUpdateByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        lastUpdateByColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastUpdateByColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(t.getNewValue());
        });

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
        setupColumns();
        table.setEditable(true);
        table.setItems(countryList);

    }
    
    /**
     * Validate new record data
     * @return 
     */
    private boolean validateRecord() {

        return app.common.isNumber(txtCountryId.getText())
              && app.common.isString(txtCountry.getText())
              && app.common.isString(txtCreateDate.getText())   
              && app.common.isString(txtCreatedBy.getText())
              && app.common.isString(txtLastUpdate.getText())  
              && app.common.isString(txtLastUpdateBy.getText());

    }
}
