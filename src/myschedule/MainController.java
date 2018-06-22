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
    protected MenuItem miAppointmentAdd;
    protected MenuItem miMaintAddress;
    protected MenuItem miMaintCity;
    protected MenuItem miMaintCustomer;
    protected MenuItem miMaintCountry;
    protected MenuItem miUserLogin;
    protected MenuItem miUserLogout;
    protected MenuItem miHelpAbout;

    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        miFileExit.setOnAction(e -> { System.exit(0); } );
        miAppointmentAdd.setOnAction(e -> { handleAppointmentAdd(); } );
        miMaintAddress.setOnAction(e -> { handleAddressMaintenance(); } );
        miMaintCity.setOnAction(e -> { handleCityMaintenance(); } );
        miMaintCountry.setOnAction(e -> { handleCountryMaintenance(); } );
        miMaintCustomer.setOnAction(e -> { handleCustomerMaintenance(); } );
        miUserLogin.setOnAction(e -> { handleLogin(); } ); 
    }

    /**
     * Disable Users.Login
     */
    @SuppressWarnings("unchecked")
    public void disableLogin() {
        menuBar.getMenus().get(3).getItems().get(0).setDisable(true);
    }
    
    /**
     * Disable Users.Logout
     */
    @SuppressWarnings("unchecked")
    public void disableLogout() {
        menuBar.getMenus().get(3).getItems().get(1).setDisable(false);
    }
    
    /**
     * Disable menu
     */
    @SuppressWarnings("unchecked")
    public void disableMenu() {
        menuBar.getMenus().forEach(( m ) -> {
            m.getItems().forEach(( mi ) -> {
                mi.setDisable( true );
            });
        });
    }

    /**
     * Enable Help.About
     */
    @SuppressWarnings("unchecked")
    public void enableAbout() {
        menuBar.getMenus().get(4).getItems().get(0).setDisable(false);
    }
    
    /**
     * Enable Menu.Exit
     */
    @SuppressWarnings("unchecked")
    public void enableExit() {
        menuBar.getMenus().get(0).getItems().get(4).setDisable(false);
    }
    
    /**
     * Enable Users.Login
     */
    @SuppressWarnings("unchecked")
    public void enableLogin() {
        menuBar.getMenus().get(3).getItems().get(0).setDisable(false);
    }
    
    /**
     * Enable Users.Logout
     */
    @SuppressWarnings("unchecked")
    public void enableLogout() {
        menuBar.getMenus().get(3).getItems().get(1).setDisable(false);
    }
    
    /**
     * Enable menu
     */
    @SuppressWarnings("unchecked")
    public void enableMenu() {
        menuBar.getMenus().forEach((m) -> {
            m.getItems().forEach((mi) -> {
                mi.setDisable(false);
            });
        });
    }
    
    /**
     * End currently running process
     */
    @SuppressWarnings("unchecked")
    protected void endProcess() {
        try {
            Node node = mainContainer.getCenter();
            mainContainer.getChildren().removeAll(node);
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error ending current process");
        }
    }

    /**
     * End currently running process and start the next action
     * @param nextAction 
     */
    @SuppressWarnings("unchecked")
    protected void endProcess(String nextAction) {
        Node node = mainContainer.getCenter();
        mainContainer.getChildren().removeAll(node);
        
        try {
            switch (nextAction) {
                case "addressMaint":
                    handleAddressMaintenance();
                    break;
                case "cityMaint":
                    handleCityMaintenance();
                    break;
                case "countryMaint":
                    handleCountryMaintenance();
                    break;
                default:
                    break;
            }
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error ending current process and starting " + nextAction);
        }
    }

    /**
     * Handle AddressMaintenance request
     */
    @SuppressWarnings("unchecked")
    private void handleAddressMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Address.fxml"));
            Node node = loader.load();
            AddressController controller = loader.getController();
            controller.injectMainController(this);
            controller.injectApp(app);
            mainContainer.setCenter(node);
            controller.start();
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting Address Maintenance");
        }
    }

    @SuppressWarnings("unchecked")
    private void handleAppointmentAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Appointment.fxml"));
            Node node = loader.load();
            AppointmentController controller = loader.getController();
            controller.injectMainController(this);
            controller.injectApp(app);
            mainContainer.setCenter(node);
            controller.start();
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting Appointment Add");
        }
    }
    
    /**
     * Handle CityMaintenance request
     */
    @SuppressWarnings("unchecked")
    private void handleCityMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("City.fxml"));
            Node node = loader.load();
            CityController controller = loader.getController();
            controller.injectMainController(this);
            controller.injectApp(app);
            mainContainer.setCenter(node);
            controller.start();
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting City Maintenance");
        }
    }

    /**
     * Handle CountryMaintenance request
     */
    @SuppressWarnings("unchecked")
    private void handleCountryMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Country.fxml"));
            Node node = loader.load();
            CountryController controller = loader.getController();
            controller.injectMainController(this);
            controller.injectApp(app);
            mainContainer.setCenter(node);
            controller.start();
        }
        catch(Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting Country Maintenance");
        }
    }

    /**
     * Handle CustomerMaintenance request
     */
    @SuppressWarnings("unchecked")
    private void handleCustomerMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Customer.fxml"));
            Node node = loader.load();
            CustomerController controller = loader.getController();
            controller.injectMainController(this);
            controller.injectApp(app);
            mainContainer.setCenter(node);
            controller.start();
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting Customer Maintenance");
        }
    }
    
    @SuppressWarnings("unchecked")
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.this.getClass().getResource("Login.fxml"));
            Node node = loader.load();
            LoginController login = loader.getController();
            login.injectMainController(this);
            login.injectApp(app);
            mainContainer.setCenter(node);
            login.start();
        }
        catch (Exception ex) {
            app.common.alertStatus(0);
            app.log.write(Level.SEVERE, "Error starting Login");
        }
    }
    
    /**
     * Inject App object
     * @param _app 
     */
    @SuppressWarnings("unchecked")
    protected void injectApp(App _app) {
        app = _app;
    }
    
    /**
     * Start the MainController
     */
    @SuppressWarnings("unchecked")
    protected void start() {

        // File menu [0]
        Menu menuFile = new Menu(app.localize("file"));
        miFileNew = new MenuItem(app.localize("new")); // [0.0]
        miFileOpen = new MenuItem(app.localize("open")); // [0.1]
        miFileSave = new MenuItem(app.localize("save")); // [0.2]
        miFileSaveAs = new MenuItem(app.localize("save_as")); // [0.3]
        miFileExit = new MenuItem(app.localize("exit")); // [0.4]
        menuFile.getItems().addAll(miFileNew, miFileOpen, miFileSave, miFileSaveAs, miFileExit);

        // Appointment menu [1]
        Menu menuAppointment = new Menu(app.localize("appointment"));
        miAppointmentAdd = new MenuItem(app.localize("add")); // [1.0]
        menuAppointment.getItems().addAll(miAppointmentAdd); 

        // Maintenance menu [2]
        Menu menuMaint = new Menu(app.localize("maintain"));
        miMaintAddress = new MenuItem(app.localize("address")); // [2.0]
        miMaintCity = new MenuItem(app.localize("city")); // [2.1]
        miMaintCountry = new MenuItem(app.localize("country")); // [2.2]
        miMaintCustomer = new MenuItem(app.localize("customer")); // [2.3]
        menuMaint.getItems().addAll(miMaintAddress, miMaintCity, miMaintCountry, miMaintCustomer);
        
        // User menu [3]
        Menu menuUser = new Menu(app.localize("user"));
        miUserLogin = new MenuItem(app.localize("login")); // [3.0]
        miUserLogout = new MenuItem(app.localize("logout")); // [3.1]
        menuUser.getItems().addAll(miUserLogin, miUserLogout);

        // Help menu [4]
        Menu menuHelp = new Menu(app.localize("help"));
        miHelpAbout = new MenuItem(app.localize("about")); // [4.0]
        menuHelp.getItems().addAll(miHelpAbout);

        addListeners();
        menuBar.getMenus().addAll(menuFile, menuAppointment, menuMaint, menuUser, menuHelp);
        mainContainer.setTop(menuBar);
        
        if (!app.loggedIn()) {
            disableMenu();
            enableAbout();
            enableExit();
            enableLogin();
            handleLogin();
        }
        else {
            enableMenu();
            disableLogin();
        }
    }
}
