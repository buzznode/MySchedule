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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import myschedule.model.AppointmentModel;

/**
 * @author bradd
 * @version 0.5.0
 */
public class AppointmentController {
    @FXML private TabPane appointments;
    @FXML private Tab tabCreate;
    @FXML private Label lblCustomer;
    @FXML private ComboBox<String> cboCustomer;
    @FXML private Label lblTitle;
    @FXML private TextField txtTitle;
    @FXML private Label lblDescription;
    @FXML private TextField txtDescription;
    @FXML private Label lblLocation;
    @FXML private TextField txtLocation;
    @FXML private Label lblContact;
    @FXML private TextField txtContact;
    @FXML private Label lblURL;
    @FXML private TextField txtURL;
    @FXML private Label lblStartDate;
    @FXML private DatePicker dteStartDate;
    @FXML private Label lblEndDate;
    @FXML private DatePicker dteEndDate;
    @FXML private Label lblStartTime;
    @FXML private ComboBox<String> cboStartHour;
    @FXML private ComboBox<String> cboStartMinute;
    @FXML private ComboBox<String> cboStartAMPM;
    @FXML private Label lblEndTime;
    @FXML private ComboBox<String> cboEndHour;
    @FXML private ComboBox<String> cboEndMinute;
    @FXML private ComboBox<String> cboEndAMPM;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Tab tabView;

    private App app;
    private MainController main;

    // Maps
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();

    // Lists
    private List customerList;

    private final boolean unsavedChanges = false;
    
    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnCancel.setOnMouseClicked(e -> { closeAppointmentAdd(); } );
        btnSave.setOnAction(e -> { handleSave(); } );
    }
    
    /**
     * Close appointment add
     */
    @SuppressWarnings("unchecked")
    private void closeAppointmentAdd() {
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("Pending appointment changes exist.");
        alert.setContentText(
            "There have been appointment changes made that have not been saved.\n\nTo save these changes, " +
            "click \"No\" to close this alert, and then click on the \"Save\" button to save the changes.\n\n" +
            "Clicking \"Yes\" will result in the pending changes being lost and the appointment maintenance process ending."
        );
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnYes, btnNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == btnYes;
    }
    
    /**
     * Save appointment
     */
    @SuppressWarnings("unchecked")
    private void handleSave() {
        LocalDateTime endDateTime;
        LocalDate endDate;
        LocalTime endTime;
        LocalDateTime startDateTime;
        LocalDate startDate;
        LocalTime startTime;
        String str;
        
        if (validateForm()) {
            AppointmentModel appt = new AppointmentModel();
            appt.setAppointmentId(0);
            appt.setCustomerId(customerToCustomerIdMap.get(cboCustomer.getSelectionModel().getSelectedItem()));
            appt.setTitle(txtTitle.getText());
            appt.setDescription(txtDescription.getText());
            appt.setLocation(txtLocation.getText());
            appt.setContact(txtContact.getText());
            appt.setUrl(txtURL.getText());
            
            startDate = dteStartDate.getValue();
            endDate = dteEndDate.getValue();
            
            str = cboStartHour.getValue() + ":" +cboStartMinute.getValue() + cboStartAMPM.getValue(); 
            startTime = LocalTime.parse(str);
            str = cboEndHour.getValue() + ":" +cboEndMinute.getValue() + cboEndAMPM.getValue(); 
            endTime = LocalTime.parse(str);
            
            startDateTime = LocalDateTime.parse(startDate + " " + startTime);
            endDateTime = LocalDateTime.parse(endDate + " " + endTime);
            System.out.println("startDateTime:" + startDateTime + "; endDateTime: " + endDateTime);
        }
        else {
            app.common.alertStatus(0);
        }
    }
    
    /**
     * Initializes the form
     */
    @SuppressWarnings("unchecked")
    private void initializeForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtLocation.setText("");
        txtContact.setText("");
        txtURL.setText("");
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

    
    @SuppressWarnings("unchecked")
    private void loadComboBoxes() {
        String hours[] =  {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String minutes[] = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
        String ampm[] = {"AM", "PM"};
        
        cboStartHour.getItems().addAll(hours);
        cboEndHour.getItems().addAll(hours);
        cboStartMinute.getItems().addAll(minutes);
        cboEndMinute.getItems().addAll(minutes);
        cboStartAMPM.getItems().addAll(ampm);
        cboEndAMPM.getItems().addAll(ampm);
        
        try {
            customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap(false);
            customerList = app.common.createCustomerList(customerToCustomerIdMap);
            cboCustomer.getItems().addAll(customerList);
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
    }
    
    /**
     * Start address maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        loadComboBoxes();
        initializeForm();
    }

    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private boolean validateForm() {
        return app.common.isValidString(cboCustomer.getValue(), false)
              && app.common.isValidString(txtTitle.getText(), false)
              && app.common.isValidString(txtDescription.getText(), false)
              && app.common.isValidString(txtLocation.getText(), false)
              && app.common.isValidString(txtContact.getText(), false)
              && app.common.isValidString(txtURL.getText(), false)
              && app.common.isValidString(dteStartDate.getValue().toString(), false)
              && app.common.isValidString(dteEndDate.getValue().toString(), false)
              && app.common.isValidString(cboStartHour.getValue(), false)
              && app.common.isValidString(cboStartMinute.getValue(), false)
              && app.common.isValidString(cboStartAMPM.getValue(), false)
              && app.common.isValidString(cboEndHour.getValue(), false)
              && app.common.isValidString(cboEndMinute.getValue(), false)
              && app.common.isValidString(cboEndAMPM.getValue(), false);
    }
}
