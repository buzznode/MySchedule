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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * @author bradd
 */
public class MainController implements Initializable {

    private App _app;
    
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
    
    // Protected general variables
    protected boolean _loggedIn;
    protected String _userName;

    private static final Logger LOGGER = Logger.getLogger("myschedule.log");
    
    /**
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu menuFile = new Menu("File");
        miFileNew = new MenuItem("New");
        miFileOpen = new MenuItem("Open");
        miFileSave = new MenuItem("Save");
        miFileSaveAs = new MenuItem("Save As...");
        miFileExit = new MenuItem("Exit");
        menuFile.getItems().addAll(miFileNew, miFileOpen, miFileSave, miFileSaveAs, miFileExit);

        // Edit menu
        Menu menuEdit = new Menu("Edit");
        miEditDelete = new MenuItem("Delete");
        menuEdit.getItems().addAll(miEditDelete);

        // User menu
        Menu menuUser = new Menu("User");
        miUserLogin = new MenuItem("Login");
        miUserLogout = new MenuItem("Logout");
        menuUser.getItems().addAll(miUserLogin, miUserLogout);

        // Help menu
        Menu menuHelp = new Menu("Help");
        miHelpAbout = new MenuItem("About");
        menuHelp.getItems().addAll(miHelpAbout);

        createActions();
        menuBar.getMenus().addAll(menuFile, menuEdit, menuUser, menuHelp);
        mainContainer.setTop(menuBar);
    }
  
    /**
     * 
     */
    private void createActionListeners() {
        miFileExit.setOnAction((ea) -> {
            System.exit(0);
        });
        
        miUserLogin.setOnAction((ae) -> {
            startLogin();
        });
    }
    
    /**
     * 
     */
    protected void endProcess() {
        Node node = mainContainer.getCenter();
        mainContainer.getChildren().removeAll(node);
    }

    protected void injectApp(App app) {
        _app = app;
        this.writeLog(Level.INFO, "app has been injected");
    }
    
    /**
     * 
     */
    private void startLogin() throws Exception {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Login.fxml"));
            Node node = loader.load();
            LoginController controller = loader.getController();
            controller.injectMainController(this);
            mainContainer.setCenter(node);
    }
    
    /**
     * @param level
     * @param msg 
     */
    protected void writeLog(Level level, String msg) {
        LOGGER.log(level, msg);
    }
    
    /* Getters & Setters */
    
    /**
     * @return  _loggedIn as boolean
     */
    protected boolean loggedIn() {
        return _loggedIn;
    }
    
    /**
     * @param value
     * @return  _loggedIn as boolean
     */
    protected boolean loggedIn(boolean value) {
        return _loggedIn = value;
    }
    
    /**
     * @return _userName as String
     */
    protected String userName() {
        return _userName;
    }

    /**
     * @param user
     * @return _userName as String
     */
    protected String userName(String user) {
        return _userName = user;
    }
}

