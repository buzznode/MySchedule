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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author bradd
 */
public class MainController implements Initializable {

    // Controllers
    @FXML private final App app = new App();

    // FXML Components
    @FXML protected BorderPane mainContainer;

    // Private variables
    protected MenuItem miFileNew;     
    protected MenuItem miFileOpen;
    protected MenuItem miFileSave;
    protected MenuItem miFileSaveAs;
    protected MenuItem miFileExit;
    protected MenuItem miEditDelete;
    protected MenuItem miUserLogin;
    protected MenuItem miUserLogout;
    protected MenuItem miHelpAbout;
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        app.injectMainController(this);
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

        setupActions();
        menuBar.getMenus().addAll(menuFile, menuEdit, menuUser, menuHelp);
        mainContainer.setTop(menuBar);
    }
  
    private void setupActions() {
        // Define Action Event Handlers
        miFileExit.setOnAction((e) -> {
          System.exit(0);
        });

        miUserLogin.setOnAction((e) -> {
            try {
              loadFXML("Login.fxml");
            }
            catch (Exception ex) {

            }
        });
    }
  
    private void loadFXML(String fxml) throws Exception {
        Node node = FXMLLoader.load(getClass().getResource(fxml));
        this.mainContainer.setCenter(node);
    }
}

