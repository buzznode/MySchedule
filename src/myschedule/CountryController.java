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

import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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
    
    private App app;
    private MainController main;
    
    private final TableView<CountryModel> table = new TableView<>();
    private final ObservableList<CountryModel> data =
            FXCollections.observableArrayList(
            new Person("Jacob", "Smith", "jacob.smith@example.com"),
            new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
            new Person("Ethan", "Williams", "ethan.williams@example.com"),
            new Person("Emma", "Jones", "emma.jones@example.com"),
            new Person("Michael", "Brown", "michael.brown@example.com"));
    

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
    public void start() {
        createActionListeners();
//        btnCancel.setText(app.localize("cancel"));
//        btnLogin.setText(app.localize("login"));
        lblTitle.setText(app.localize("countries"));
//        app.common.loadUsers();
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
