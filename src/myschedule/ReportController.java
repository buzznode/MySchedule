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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import myschedule.model.AppointmentTypeCountModel;
import myschedule.model.ConsultantScheduleModel;
import myschedule.model.TotalAppointmentsModel;

/**
 * @author bradd
 * @version 0.5.0
 */
@SuppressWarnings("unchecked")
public class ReportController {
    @FXML protected BorderPane reportContainer;
    @FXML Button btnClose;
    @FXML Button btnRun;
    @FXML ComboBox<Report> cboReport;
    @FXML Label lblReports;
    @FXML Label lblTitle;
    
    // Table and Columns
    @FXML HBox tableViewContainer;

    private App app;
    private MainController main;
    private final TableView<AppointmentTypeCountModel> tableViewATC = new TableView<>();
    private final TableView<ConsultantScheduleModel> tableViewCS = new TableView<>();
    private final TableView<TotalAppointmentsModel> tableViewTA = new TableView<>();
    private ObservableList<AppointmentTypeCountModel> atcList = FXCollections.observableArrayList();
    private ObservableList<ConsultantScheduleModel> csList = FXCollections.observableArrayList();
    private ObservableList<TotalAppointmentsModel> taList = FXCollections.observableArrayList();
    
    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnClose.setOnMouseClicked(e -> { handleClose(); } );
        btnRun.setOnMouseClicked(e -> { handleRun(); } );
    }
    
    /**
     * Close Report Module
     */
    @SuppressWarnings("unchecked")
    private void handleClose() {
        main.endProcess();
    }

    /**
     * Handle Report Run
     */
    @SuppressWarnings("unchecked")
    private void handleRun() {
        String reportName;
        
        if (cboReport.getValue() == null) {
            app.common.alertStatus(0, "Report Name Required", "A report must be selected first.");
            return;
        }
        
        Report rpt = cboReport.getSelectionModel().getSelectedItem();
        reportName = rpt.getName();
        
            switch (reportName) {
                case "atcReport":
                    runATCReport();
                    break;
                case "csReport":
                    runCSReport();
                    break;
                case "taReport":
                    runTAReport();
                    break;
                default:
                    break;
            }
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
     * Load Available Reports
     */
    @SuppressWarnings("unchecked")
    private void loadReports() {
        cboReport.getItems().addAll(
            new Report("Consultants Schedule by Month, Start Date, and Appointment Type", "csReport"),
            new Report("Monthly Appointment Type Count by Consultant", "taReport"),
            new Report("Monthly Counts by Appointment Type", "atcReport")
        );
    }
    
    /**
     * Run the Appointment Type Count Report
     */
    @SuppressWarnings("unchecked")
    private void runATCReport() {
        atcList = app.db.getAppointmentsTypeCountReport();
        tableViewATC.getColumns().remove(0, tableViewATC.getColumns().size());
        tableViewCS.getColumns().remove(0, tableViewCS.getColumns().size());
        tableViewTA.getColumns().remove(0, tableViewTA.getColumns().size());
        tableViewATC.setEditable(false);

        TableColumn<AppointmentTypeCountModel, String> monthNameColumn = new TableColumn<>("Month");
        monthNameColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getMonthName()));

        TableColumn<AppointmentTypeCountModel, String> descriptionColumn = new TableColumn<>("Appointment Type");
        descriptionColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getDescription()));

        TableColumn<AppointmentTypeCountModel, Integer> cntColumn = new TableColumn<>("Count");
        cntColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCnt()));

        tableViewATC.setPrefSize(880.0, 590.0);
        tableViewATC.setItems(atcList);
        tableViewATC.getColumns().addAll(
            monthNameColumn, descriptionColumn, cntColumn
        );

        if (tableViewContainer.getChildren().size() > 0) {
            tableViewContainer.getChildren().remove(tableViewATC);
            tableViewContainer.getChildren().remove(tableViewCS);
            tableViewContainer.getChildren().remove(tableViewTA);
        }
        tableViewContainer.getChildren().add(tableViewATC);
    }
    
    /**
     * Run the Consultants Schedule Report
     */
    @SuppressWarnings("unchecked")
    private void runCSReport() {
        csList = app.db.getConsultantScheduleReport(app.strZoneOffset());
        tableViewATC.getColumns().remove(0, tableViewATC.getColumns().size());
        tableViewCS.getColumns().remove(0, tableViewCS.getColumns().size());
        tableViewTA.getColumns().remove(0, tableViewTA.getColumns().size());
        tableViewCS.setEditable(false);

        TableColumn<ConsultantScheduleModel, String> contactColumn = new TableColumn<>("Consultant");
        contactColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getContact()));

        TableColumn<ConsultantScheduleModel, String> monthNameColumn = new TableColumn<>("Month");
        monthNameColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getMonthName()));

        TableColumn<ConsultantScheduleModel, String> startColumn = new TableColumn<>("Start Date");
        startColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getStart()));

        TableColumn<ConsultantScheduleModel, String> endColumn = new TableColumn<>("End Date");
        endColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getEnd()));

        TableColumn<ConsultantScheduleModel, String> descriptionColumn = new TableColumn<>("Appointment Type");
        descriptionColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getDescription()));

        TableColumn<ConsultantScheduleModel, String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCustomerName()));

        TableColumn<ConsultantScheduleModel, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getTitle()));

        TableColumn<ConsultantScheduleModel, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getLocation()));
            
        tableViewCS.setPrefSize(880.0, 590.0);
        tableViewCS.setItems(csList);
        tableViewCS.getColumns().addAll(
            contactColumn, monthNameColumn, startColumn, endColumn, descriptionColumn, customerNameColumn, titleColumn, locationColumn 
        );
            
        if (tableViewContainer.getChildren().size() > 0) {
            tableViewContainer.getChildren().remove(tableViewATC);
            tableViewContainer.getChildren().remove(tableViewCS);
            tableViewContainer.getChildren().remove(tableViewTA);
        }
        tableViewContainer.getChildren().add(tableViewCS);
    }
    
    /**
     * Run the Total Appointments Report
     */
    @SuppressWarnings("unchecked")
    private void runTAReport() {
        taList = app.db.getTotalAppointmentsReport();
        tableViewATC.getColumns().remove(0, tableViewATC.getColumns().size());
        tableViewCS.getColumns().remove(0, tableViewCS.getColumns().size());
        tableViewTA.getColumns().remove(0, tableViewTA.getColumns().size());
        tableViewTA.setEditable(false);

        TableColumn<TotalAppointmentsModel, String> monthNameColumn = new TableColumn<>("Month");
        monthNameColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getMonthName()));

        TableColumn<TotalAppointmentsModel, String> descriptionColumn = new TableColumn<>("Appointment Type");
        descriptionColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getDescription()));

        TableColumn<TotalAppointmentsModel, String> contactColumn = new TableColumn<>("Consultant");
        contactColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getContact()));

        TableColumn<TotalAppointmentsModel, Integer> cntColumn = new TableColumn<>("Count");
        cntColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCnt()));

        tableViewTA.setPrefSize(880.0, 590.0);
        tableViewTA.setItems(taList);
        tableViewTA.getColumns().addAll(
            monthNameColumn, descriptionColumn, contactColumn, cntColumn
        );

        if (tableViewContainer.getChildren().size() > 0) {
            tableViewContainer.getChildren().remove(tableViewATC);
            tableViewContainer.getChildren().remove(tableViewCS);
            tableViewContainer.getChildren().remove(tableViewTA);
        }
        tableViewContainer.getChildren().add(tableViewTA);
    }
    
    /**
     * Start Calendar View
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        loadReports();
//        createCalendar();
    }
}    
