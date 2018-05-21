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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
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
    @FXML private HBox controlsContainer;
    @FXML private HBox buttonsContainer;
    
    private App app;
    private MainController main;
    
    private final TableView<CountryModel> table = new TableView<>();
    private ObservableList<CountryModel> countryList = FXCollections.observableArrayList();
    
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
//        btnCancel.setOnMouseClicked((ea) -> {
//            cancelLogin();
//        });
//        
//        btnLogin.setOnMouseClicked((ea) -> {
//            userLogin();
//        });
//        
//        txtPassword.setOnMouseClicked((me) -> {
//            txtPassword.setText("");
//            lblFeedback.setText("");
//        });
//        
//        txtUsername.setOnMouseClicked((me) -> {
//            txtUsername.setText("");
//            lblFeedback.setText("");
//        });
    }

    /**
     * Start country maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        table.setMinHeight(300);
        table.setMinWidth(800);
        createActionListeners();
//        btnCancel.setText(app.localize("cancel"));
//        btnLogin.setText(app.localize("login"));
        lblTitle.setText(app.localize("countries"));
//        app.common.loadUsers();
        countryList = app.db.getCountries();
        table.setEditable(true);
        
        // Country Id column
        TableColumn<CountryModel, Integer> countryIdColumn = new TableColumn<>("Country Id");
        countryIdColumn.setMinWidth(80);
        countryIdColumn.setCellValueFactory(new PropertyValueFactory("countryId"));
        
        // Country column
        TableColumn<CountryModel, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setMinWidth(120);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setOnEditCommit(
            (CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCountry(t.getNewValue());
            });
        
        // Create Date column
        TableColumn<CountryModel, String> createDateColumn = new TableColumn<>("Create Date");
        createDateColumn.setMinWidth(80);
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        createDateColumn.setOnEditCommit(
            (CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCreateDate(t.getNewValue());
            });

        // Created By column
        TableColumn<CountryModel, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setMinWidth(80);
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        createdByColumn.setOnEditCommit(
            (CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCreatedBy(t.getNewValue());
            });

        // Last Update column
        TableColumn<CountryModel, String> lastUpdateColumn = new TableColumn<>("Last Update");
        lastUpdateColumn.setMinWidth(80);
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        lastUpdateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastUpdateColumn.setOnEditCommit(
            (CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdate(t.getNewValue());
            });

        // Last Update By column
        TableColumn<CountryModel, String> lastUpdateByColumn = new TableColumn<>("Last Update By");
        lastUpdateByColumn.setMinWidth(80);
        lastUpdateByColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        lastUpdateByColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastUpdateByColumn.setOnEditCommit(
            (CellEditEvent<CountryModel, String> t) -> {
                ((CountryModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastUpdateBy(t.getNewValue());
            });

        table.setItems(countryList);
        table.getColumns().addAll(countryIdColumn, countryColumn, createDateColumn, 
            createdByColumn, lastUpdateColumn, lastUpdateByColumn);
        tableContainer.getChildren().add(table);
        
        final TextField addCountryId = new TextField();
        addCountryId.setPromptText("Country Id");
        addCountryId.setMinWidth(countryIdColumn.getPrefWidth());
        
        final TextField addCountry = new TextField();
        addCountry.setPromptText("Country");
        addCountry.setMinWidth(countryColumn.getPrefWidth());
        
        final TextField addCreateDate = new TextField();
        addCreateDate.setPromptText("Create Date");
        addCreateDate.setMinWidth(createDateColumn.getPrefWidth());
        
        final TextField addCreatedBy = new TextField();
        addCreatedBy.setPromptText("Created By");
        addCreatedBy.setMinWidth(createdByColumn.getPrefWidth());
        
        final TextField addLastUpdate = new TextField();
        addLastUpdate.setPromptText("Last Update");
        addLastUpdate.setMinWidth(lastUpdateColumn.getPrefWidth());
        
        final TextField addLastUpdateBy = new TextField();
        addLastUpdateBy.setPromptText("Last Update By");
        addLastUpdateBy.setMinWidth(lastUpdateByColumn.getPrefWidth());
        
        final Button addButton = new Button("Add");
        addButton.setOnAction((ae) -> {
            countryList.add(new CountryModel(
                Integer.parseInt(addCountryId.getText()),
                addCountry.getText(),
                addCreateDate.getText(),
                addCreatedBy.getText(),
                addLastUpdate.getText(),
                addLastUpdateBy.getText()
            ));
            addCountryId.clear();
            addCountry.clear();
            addCreateDate.clear();
            addCreatedBy.clear();
            addLastUpdate.clear();
            addLastUpdateBy.clear();
        });
        
        final Button deleteButton = new Button("Delete");
        deleteButton.setOnAction((ae) -> {
            ObservableList<CountryModel> countrySelected, allCountries;
            allCountries = table.getItems();
            countrySelected = table.getSelectionModel().getSelectedItems();
            countrySelected.forEach(allCountries::remove);
        });
 
        controlsContainer.getChildren().addAll(addCountryId, addCountry, addCreateDate, 
                addCreatedBy, addLastUpdate, addLastUpdateBy);
        controlsContainer.setSpacing(5);
        controlsContainer.setPadding(new Insets(10, 0, 0, 10));
        
        buttonsContainer.getChildren().addAll(addButton, deleteButton);
        buttonsContainer.setSpacing(5);
        buttonsContainer.setPadding(new Insets(10, 0, 0, 10));
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
}
