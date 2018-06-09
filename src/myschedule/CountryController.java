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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import myschedule.model.CountryModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CountryController {
    @FXML private Label lblTitle;
    @FXML private TableView table;
    @FXML private TableColumn<CountryModel, Integer> countryIdColumn;
    @FXML private TableColumn<CountryModel, String> countryColumn;
    @FXML private TextField txtCountryId;
    @FXML private TextField txtCountry;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnClose;
    @FXML private Button btnCommit;

    private App app;
    private ObservableList<CountryModel> countryList = FXCollections.observableArrayList();
    private MainController main;
    private boolean unsavedChanges = false;

    /**
     *  Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnAdd.setOnAction(e -> {
            handleAdd();
        });
        
        btnClose.setOnMouseClicked((ea) -> {
            closeCountryMaint();
        });
        
        btnCommit.setOnAction(e -> {
            handleCommit();
        });

        btnRemove.setOnAction(e -> {
            handleRemove();
        });
    }

    /**
     * Close country maintenance 
     */
    @SuppressWarnings("unchecked")
    private void closeCountryMaint() {
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
        alert.setHeaderText("Pending country changes exist.");
        alert.setContentText(
            "There have been changes made to the country data that have not been saved.\n\nTo save these changes, " +
            "click \"No\" to close this alert, and then click on the \"Commit\" button to save the changes.\n\n" +
            "Clicking \"Yes\" will result in the pending changes being lost and the country maintenance process ending."
        );
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnYes, btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == btnYes;
    }
    
    /**
     * Get next available Country Id to be use for add
     * @param clist
     * @return 
     */
    @SuppressWarnings("unchecked")
    private int getNextCountryId(ObservableList<CountryModel> clist) {
        if (clist.size() > 0) {
            Optional<CountryModel> country = clist
                .stream()
                .max(Comparator.comparing(CountryModel::getCountryId));
            return country.get().getCountryId() + 1;
        }
        else {
            return 1;
        }
    }

    /**
     * Handle add action
     */
    private void handleAdd() {
        String rightNow = app.common.rightNow();
        String user = app.userName();

        if (validateCountryRecord()) {
            countryList.add(new CountryModel(
                Integer.parseInt(txtCountryId.getText()), txtCountry.getText(), rightNow, user, rightNow, user)
            );
            unsavedChanges = true;
            initializeForm();
        }
        else {
            app.log.write(Level.SEVERE, "Error parsing new country record");
        }
    }

    private void handleCommit() {
        try {
            app.db.updateCountryTable(countryList);
            unsavedChanges = false;
            app.common.alertStatus(1);
            refreshTableView();
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
    }

    /**
     * Handle remove action
     */
    private void handleRemove() {
        ObservableList<CountryModel> countrySelected, allCountries;
        allCountries = table.getItems();
        countrySelected = table.getSelectionModel().getSelectedItems();
        countrySelected.forEach(allCountries::remove);
        unsavedChanges = true;
    }
    
    /**
     * Set default values for new record
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
        int nextCountryId = getNextCountryId(countryList);
        txtCountryId.setText(Integer.toString(nextCountryId));
        txtCountry.setText("");
        txtCountryId.setDisable(true);
        txtCountry.setDisable(false);
    }
    
    /**
     * Initialize Cell Factories and Cell Value Factories
     */
    @SuppressWarnings("unchecked")
    private void initializeTableColumns() {
        // Country Id column
        countryIdColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCountryId()));
        
        // Country column
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCountry(t.getNewValue());
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(app.common.rightNow());
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(app.userName());
                table.refresh();
                unsavedChanges = true;
            }
        );
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
     * Refresh Country TableView
     */
    @SuppressWarnings("unchecked")
    private void refreshTableView() {
        try {
            countryList = app.db.getCountryModelList("country", "asc");
            table.setItems(countryList);
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * Start country maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        lblTitle.setText(app.localize("countries"));
        
        try {
            countryList = app.db.getCountryModelList("country", "asc");
        }
        catch (SQLException ex) {
            app.log.write(Level.SEVERE, ex.getMessage());
        }
        
        initializeForm();
        initializeTableColumns();
        table.setEditable(true);
        table.setItems(countryList);
    }
    
    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private boolean validateCountryRecord() {
        return app.common.isNumber(txtCountryId.getText())
              && app.common.isString(txtCountry.getText());
    }
}
