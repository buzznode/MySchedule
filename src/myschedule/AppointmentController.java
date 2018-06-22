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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 * @author bradd
 * @version 0.5.0
 */
public class AppointmentController {
    @FXML private TabPane appointments;
    @FXML private Tab tabCreate;
    @FXML private Label lblCustomer;
    @FXML private ComboBox<?> cboCustomer;
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
    @FXML private ChoiceBox<?> chcStartHour;
    @FXML private ChoiceBox<?> chcStartMinute;
    @FXML private ChoiceBox<?> chcStartAMPM;
    @FXML private Label lblEndTime;
    @FXML private ChoiceBox<?> chcEndHour;
    @FXML private ChoiceBox<?> chcEndMinute;
    @FXML private ChoiceBox<?> chcEndAMPM;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Tab tabView;

    /**
     * Initializes the form
     */
    @SuppressWarnings("unchecked")
    public void initializeForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtLocation.setText("");
        txtContact.setText("");
        txtURL.setText("");
    }    
    
    @SuppressWarnings("unchecked")
    public void loadTimeChoiceBoxes() {
        chcStartHour.getItems().addAll("1:00",)
    }
    
}
