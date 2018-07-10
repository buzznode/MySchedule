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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
    @FXML private GridPane gridLayout;
    @FXML private Label lblEndDate;
    @FXML private Label lblStartTime;
    @FXML private ComboBox<String> cboStartHour;
    @FXML private ComboBox<String> cboStartMinute;
    @FXML private ComboBox<String> cboStartAMPM;
    @FXML private Label lblEndTime;
    @FXML private ComboBox<String> cboEndHour;
    @FXML private ComboBox<String> cboEndMinute;
    @FXML private ComboBox<String> cboEndAMPM;
    @FXML private Button btnCancel;
    @FXML private Button btnCustomer;
    @FXML private Button btnSave;
    @FXML private Tab tabView;

    private App app;
    private MainController main;

    // Maps
    private Map<String, Integer> customerToCustomerIdMap = new HashMap<>();

    // Lists
    private List customerList;

    // Miscellaneous
    private int appointmentId = 0;
    private DatePicker dteEndDate;
    private DatePicker dteStartDate;
    private final boolean unsavedChanges = false;
    
    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnCancel.setOnMouseClicked(e -> { closeAppointmentMaint(); } );
        btnCustomer.setOnAction(e -> { handleCustomer(); } );
        btnSave.setOnAction(e -> { handleSave(); } );
    }
    
    /**
     * Close appointment add
     */
    @SuppressWarnings("unchecked")
    private void closeAppointmentMaint() {
        if (unsavedChanges) {
            if (confirmUnsaved()) {
                if (this.appointmentId > 0) {
                    main.endProcess("appointmentView", null);
                }
                else {
                    main.endProcess();
                }
            }
        }
        else {
            if (this.appointmentId > 0) {
                main.endProcess("appointmentView", null);
            }
            else {
                main.endProcess();
            }
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
    
    @SuppressWarnings("unchecked")
    private LocalDateTime formatDateTime(LocalDate yyyymmdd, String hh, String mm, String ampm) {
        String tme;
        
        if (ampm.equals("AM")) {
            if (hh.equals("12")) {
                hh = "00";
            }
            else if (hh.length() < 2) {
                hh = "0" + hh;
            }
        }
        else {
            if (!hh.equals("12")) {
                hh = Integer.toString(Integer.parseInt(hh) + 12);
            }
        }
        
        tme = hh + ":" + mm ;
        return LocalDateTime.parse(yyyymmdd + " " + tme, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Handle Edit Customer
     */
    @SuppressWarnings("unchecked")
    private void handleCustomer() {
        int customerId;
        String customerName;
        
        if (cboCustomer.getValue() == null) {
            app.common.alertStatus(0, "Customer Required", "You must first select a customer.");
            return;
        }
        
        customerName = cboCustomer.getValue();
        customerId = customerToCustomerIdMap.get(customerName);
        main.endProcess("customerMaint", Integer.toString(customerId));
    }
    
    /**
     * Save appointment
     */
    @SuppressWarnings("unchecked")
    private void handleSave() {
        String ampm;
        LocalDateTime endDateTime;
        String hh;
        String mm;
        int rows = 0;
        LocalDateTime startDateTime;
        LocalDate yyyymmdd;

        try {
            validateForm();
            AppointmentModel appt = new AppointmentModel();
            appt.setAppointmentId(this.appointmentId);
            appt.setCustomerId(customerToCustomerIdMap.get(cboCustomer.getSelectionModel().getSelectedItem()));
            appt.setCustomerName(cboCustomer.getSelectionModel().getSelectedItem());
            appt.setTitle(txtTitle.getText());
            appt.setDescription(txtDescription.getText());
            appt.setLocation(txtLocation.getText());
            appt.setContact(txtContact.getText());
            appt.setUrl(txtURL.getText());

            yyyymmdd = dteStartDate.getValue();
            hh = cboStartHour.getValue();
            mm = cboStartMinute.getValue();
            ampm = cboStartAMPM.getValue();
            startDateTime = formatDateTime(yyyymmdd, hh, mm, ampm);
            appt.setStart(startDateTime.toString());

            yyyymmdd = dteEndDate.getValue();
            hh = cboEndHour.getValue();
            mm = cboEndMinute.getValue();
            ampm = cboEndAMPM.getValue();
            endDateTime = formatDateTime(yyyymmdd, hh, mm, ampm);
            appt.setEnd(endDateTime.toString());

            try {
                app.db.upsertAppointment(appt, app.userName(), app.strZoneOffset());
                app.common.alertStatus(1);
                
                if (this.appointmentId > 0) {
                    main.endProcess("appointmentView", null);
                }
                else {
                    cboCustomer.setValue("");
                    txtTitle.setText("");
                    txtDescription.setText("");
                    txtLocation.setText("");
                    txtContact.setText("");
                    txtURL.setText("");
                    dteStartDate.setValue(null);
                    dteEndDate.setValue(null);
                    cboStartHour.setValue(null);
                    cboStartMinute.setValue(null);
                    cboStartAMPM.setValue(null);
                    cboEndHour.setValue(null);
                    cboEndMinute.setValue(null);
                    cboEndAMPM.setValue(null);
                }
            }
            catch (Exception ex) {
                app.common.alertStatus(0, "Error Creating Appointment", ex.getMessage());
            }
        }
        catch (Exception ex) {
            app.log.write(Level.WARNING, ex.getMessage());
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
        txtContact.setDisable(false);
        txtContact.setText(app.userName());
        txtContact.setDisable(true);
        txtURL.setText("");
        
        dteStartDate = new DatePicker();
        dteStartDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        dteStartDate.setEditable(false);
        gridLayout.add(dteStartDate, 1, 3);
        
        dteEndDate = new DatePicker();
        dteEndDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        dteEndDate.setDisable(false);
        gridLayout.add(dteEndDate, 4, 3);
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
     * Load Appointment
     * @param appointmentId 
     */
    @SuppressWarnings("unchecked")
    private void loadAppointment(int appointmentId) {
        AppointmentModel appt;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int hh;
        String ampm;
        String mm;
        String time;
        
        appt = app.db.getAppointment(appointmentId, app.strZoneOffset());
        cboCustomer.setValue(appt.getCustomerName());
        txtTitle.setText(appt.getTitle());
        txtDescription.setText(appt.getDescription());
        txtLocation.setText(appt.getLocation());
        txtContact.setText(appt.getContact());
        txtURL.setText(appt.getUrl());
        dteStartDate.setValue(LocalDate.parse(appt.getStart().substring(0, 10), dateFormatter));
        dteEndDate.setValue(LocalDate.parse(appt.getEnd().substring(0, 10), dateFormatter));
        // Process Start time
        time = appt.getStart().substring(11);
        hh = Integer.parseInt(time.substring(0, 2));
        mm = time.substring(3, 5);
        ampm = hh > 12 ? "PM" : "AM";
        hh = hh > 12 ? hh - 12 : hh;
        cboStartHour.setValue(Integer.toString(hh));
        cboStartMinute.setValue(mm);
        cboStartAMPM.setValue(ampm);
        //Process End time
        time = appt.getEnd().substring(11);
        hh = Integer.parseInt(time.substring(0, 2));
        mm = time.substring(3, 5);
        ampm = hh > 12 ? "PM" : "AM";
        hh = hh > 12 ? hh - 12 : hh;
        cboEndHour.setValue(Integer.toString(hh));
        cboEndMinute.setValue(mm);
        cboEndAMPM.setValue(ampm);
    }
    
    /**
     * Load ComboBoxes
     */
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
        customerToCustomerIdMap = app.db.getCustomerToCustomerIdMap(false);
        customerList = app.common.createCustomerList(customerToCustomerIdMap);
        cboCustomer.getItems().addAll(customerList);
    }
    
    /**
     * Start appointment maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        this.appointmentId = 0;
        addListeners();
        loadComboBoxes();
        initializeForm();
    }

    /**
     * Start appointment maintenance (edit)
     * @param appointmentId 
     */
    @SuppressWarnings("unchecked")
    public void start(int appointmentId) {
        this.appointmentId = appointmentId;
        addListeners();
        loadComboBoxes();
        initializeForm();
        loadAppointment(appointmentId);
    }

    /**
     * Validate new record data
     * @return 
     */
    @SuppressWarnings("unchecked")
    private void validateForm() throws Exception {
        boolean notValid = false;
        LocalDate start;
        LocalDate end;
        int comp = 0;
        String hdr;
        String msg = "";
        
        msg += !app.common.isValidString(cboCustomer.getValue(), false) ? "  -  Customer" : "";
        msg += !app.common.isValidString(txtTitle.getText(), false) ? "  -  Title" : "";
        msg += !app.common.isValidString(txtDescription.getText(), false) ? "  -  Description" : "";
        msg += !app.common.isValidString(txtLocation.getText(), false) ? "  -  Location" : "";
        msg += !app.common.isValidString(txtContact.getText(), false) ? "  -  Contact" : "";
        msg += !app.common.isValidString(txtURL.getText(), true) ? "  -  URL" : "";
        msg += !app.common.isValidString(dteStartDate.getValue().toString(), false) ? "  -  Start Date" : "";
        msg += !app.common.isValidString(dteEndDate.getValue().toString(), false) ? "  -  End Date" : "";
        msg += !app.common.isValidString(cboStartHour.getValue(), false) ? "  -  Start Hour" : "";
        msg += !app.common.isValidString(cboStartMinute.getValue(), false) ? "  -  Start Minutes" : "";
        msg += !app.common.isValidString(cboStartAMPM.getValue(), false) ? "  -  Start AM/PM" : "";
        msg += !app.common.isValidString(cboEndHour.getValue(), false) ? "  -  End Hour" : "";
        msg += !app.common.isValidString(cboEndMinute.getValue(), false) ? "  -  End Minutes" : "";
        msg += !app.common.isValidString(cboEndAMPM.getValue(), false) ? "  -  End AM/PM" : "";

        if (!"".equals(msg)) {
            hdr = "Invalid Form Value(s)";
            msg = "The following fields contain invalid values:\n" + msg;
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
        }
        
        start = LocalDate.parse(dteStartDate.getValue().toString());
        end = LocalDate.parse(dteEndDate.getValue().toString());
        Date s = new Date(start.getYear(), start.getMonthValue(), start.getDayOfMonth());
        Date e = new Date(end.getYear(), end.getMonthValue(), end.getDayOfMonth());

        if (e.before(s)) {
            hdr = "Invalid Date Range";
            msg = "The end date cannot be after the start date";
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
        }
        
        if (cboStartAMPM.getValue().equals("AM")) {
          if (Integer.parseInt(cboStartHour.getValue()) < 8) {
              hdr = "Invalid Start Time";
              msg = "The start time is outside of normal business hours";
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
          }
        }
        else {
            if (Integer.parseInt(cboStartHour.getValue()) >= 5)  {
              hdr = "Invalid Start Time";
              msg = "The start time is outside of normal business hours";
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
            }
        }
        
        if (cboEndAMPM.getValue().equals("AM")) {
          if (Integer.parseInt(cboEndHour.getValue()) < 8) {
              hdr = "Invalid End Time";
              msg = "The end time is outside of normal business hours";
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
          }
            
        }
        else {
            if (Integer.parseInt(cboStartHour.getValue()) >= 5)  {
              hdr = "Invalid End Time";
              msg = "The end time is outside of normal business hours";
            app.common.alertStatus(0, hdr, msg);
            throw new Exception("Appointment creation - Invalid Form Values");
            }
        }
    }
}
