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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * @author bradd
 * @version 0.5.0
 */
public class MainController {

    private App app;
    private final MenuBar menuBar = new MenuBar();
    
    // FXML Components
    @FXML protected BorderPane mainContainer;

    // Protected MenuItem variables
    protected MenuItem miFileNew;     
    protected MenuItem miFileOpen;
    protected MenuItem miFileSave;
    protected MenuItem miFileSaveAs;
    protected MenuItem miFileExit;
    protected MenuItem miEditDelete;
    protected MenuItem miUserLogin;
    protected MenuItem miUserLogout;
    protected MenuItem miHelpAbout;

    /**
     * Create action listeners
     */
    private void createActionListeners() {
        miFileExit.setOnAction((ea) -> {
            System.exit(0);
        });
        
        miUserLogin.setOnAction((ae) -> {
            try {
                startLogin();
            }
            catch(Exception ex) {
            }
        });
    }

    /**
     * Disable menu
     */
    public void disableMenu() {
        menuBar.getMenus().forEach(( m ) -> {
            m.setDisable(true);
            m.getItems().forEach(( mi ) -> {
                mi.setDisable( true );
            });
        });
    }

    /**
     * Enable menu
     */
    public void enableMenu() {
        menuBar.getMenus().forEach((m) -> {
            m.setDisable(false);
            m.getItems().forEach((mi) -> {
                mi.setDisable(false);
            });
        });
    }
    
    /**
     * End currently running process
     */
    protected void endProcess() {
        Node node = mainContainer.getCenter();
        mainContainer.getChildren().removeAll(node);
    }
    
    /**
     * Start the MainController
     */
    protected void go() {

        // File menu
        Menu menuFile = new Menu(app.localize("file"));
        miFileNew = new MenuItem(app.localize("new"));
        miFileOpen = new MenuItem(app.localize("open"));
        miFileSave = new MenuItem(app.localize("save"));
        miFileSaveAs = new MenuItem(app.localize("save_as"));
        miFileExit = new MenuItem(app.localize("exit"));
        menuFile.getItems().addAll(miFileNew, miFileOpen, miFileSave, miFileSaveAs, miFileExit);

        // Edit menu
        Menu menuEdit = new Menu(app.localize("edit"));
        miEditDelete = new MenuItem(app.localize("delete"));
        menuEdit.getItems().addAll(miEditDelete);

        // User menu
        Menu menuUser = new Menu(app.localize("user"));
        miUserLogin = new MenuItem(app.localize("login"));
        miUserLogout = new MenuItem(app.localize("logout"));
        menuUser.getItems().addAll(miUserLogin, miUserLogout);

        // Help menu
        Menu menuHelp = new Menu(app.localize("help"));
        miHelpAbout = new MenuItem(app.localize("about"));
        menuHelp.getItems().addAll(miHelpAbout);

        createActionListeners();
        menuBar.getMenus().addAll(menuFile, menuEdit, menuUser, menuHelp);
        mainContainer.setTop(menuBar);
        
        if (!app.loggedIn()) {
            disableMenu();
            
            try {
                startLogin();
            }
            catch(Exception ex) {
                
            }
        }
        else {
            enableMenu();
        }
    }

    /**
     * Inject App object
     * @param _app 
     */
    protected void injectApp(App _app) {
        app = _app;
    }
    
    /**
     * Start the login process
     */
    private void startLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Login.fxml"));
        Node node = loader.load();
        LoginController login = loader.getController();
        login.injectMainController(this);
        login.injectApp(app);
        mainContainer.setCenter(node);
        login.go();
    }
}

