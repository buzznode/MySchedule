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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import myschedule.model.AppointmentModel;

/**
 * @author bradd
 * @version 0.5.0
 */
@SuppressWarnings("unchecked")
public class CalendarController {
    @FXML protected BorderPane calendarContainer;
    @FXML Button btnClose;
//    @FXML Pane calendarPane;
    @FXML Label lblAppointments;
    @FXML Label lblTitle;
    @FXML RadioButton radioMonth;
    @FXML RadioButton radioWeek;
    
    // Table and Columns
    @FXML HBox tableViewContainer;
//    @FXML TableView table;
//    @FXML TableColumn<AppointmentModel, Integer> appointmentIdColumn;
//    @FXML TableColumn<AppointmentModel, String> customerColumn;
//    @FXML TableColumn<AppointmentModel, String> titleColumn;
//    @FXML TableColumn<AppointmentModel, String> descriptionColumn;
//    @FXML TableColumn<AppointmentModel, String> locationColumn;
//    @FXML TableColumn<AppointmentModel, String> contactColumn;
//    @FXML TableColumn<AppointmentModel, String> urlColumn;
//    @FXML TableColumn<AppointmentModel, String> startColumn;
//    @FXML TableColumn<AppointmentModel, String> endColumn;

    private App app;
    private MainController main;
    private TableView<AppointmentModel> tableView = new TableView<>();
    private ObservableList<AppointmentModel> appointmentList = FXCollections.observableArrayList();
    
    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        btnClose.setOnMouseClicked(e -> { closeCalendar(); } );
        radioMonth.setOnAction(e -> { createCalendar(); } );
        radioWeek.setOnAction(e -> { createCalendar(); } );
    }
    
    /**
     * Check for un-saved changes; display warning message
     * as needed; close city maintenance function.
     */
    @SuppressWarnings("unchecked")
    private void closeCalendar() {
        main.endProcess();
    }

    /**
     * Fires the build of the calendar
     */
    @SuppressWarnings("unchecked")
    private void createCalendar() {
        Node node = calendarContainer.getCenter();
        calendarContainer.getChildren().removeAll(node);
        
        Pane calendarPane = new Pane();
        calendarContainer.setCenter(calendarPane);
        calendarPane.setPrefSize(500.0, 250.0);
        
        CalendarView calendarView = new CalendarView(YearMonth.now());
        calendarView.buildCalendar();
        calendarPane.getChildren().add(calendarView.getView());
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
     * Populate table
     */
    @SuppressWarnings("unchecked")
    private void populateTable() {
        tableView.getColumns().remove(0, tableView.getColumns().size());
        tableView.setEditable(false);
        
        Callback<TableColumn, TableCell> integerCellFactory = 
            new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                IntegerTableCell cell = new IntegerTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseClickEventHandler());
                return cell;
            }
        };
                
        Callback<TableColumn, TableCell> stringCellFactory = 
            new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn p) {
                    StringTableCell cell = new StringTableCell();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseClickEventHandler());
                    return cell;
            }
        };
        
        // Appointment Id column
//        appointmentIdColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getAppointmentId()));
        TableColumn appointmentIdColumn = new TableColumn("Appointment Id");
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentIdColumn.setCellFactory(integerCellFactory);
        appointmentIdColumn.setVisible(false);
        
        // Customer Name column
//        customerColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getCustomerName()));
        TableColumn  customerNameColumn = new TableColumn("Customer Name");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerNameColumn.setCellFactory(stringCellFactory);

        // Title column
//        titleColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getTitle()));
        TableColumn  titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setCellFactory(stringCellFactory);

        // Description column
//        descriptionColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getDescription()));
        TableColumn  descriptionColumn = new TableColumn("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(stringCellFactory);
        
        // Location column
//        locationColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getLocation()));
        TableColumn  locationColumn = new TableColumn("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setCellFactory(stringCellFactory);
        
        // Contact column
//        contactColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getContact()));
        TableColumn  contactColumn = new TableColumn("Contact");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactColumn.setCellFactory(stringCellFactory);
        
        // URL column
//        urlColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getUrl()));
        TableColumn  urlColumn = new TableColumn("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setCellFactory(stringCellFactory);
        
        // Start column
//        startColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getStart()));
        TableColumn  startColumn = new TableColumn("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        startColumn.setCellFactory(stringCellFactory);
        
        //End column
//        endColumn.setCellValueFactory(x -> new ReadOnlyObjectWrapper<>(x.getValue().getEnd()));
        TableColumn  endColumn = new TableColumn("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        endColumn.setCellFactory(stringCellFactory);
        
        tableView.setItems(appointmentList);
        tableView.getColumns().addAll(
            appointmentIdColumn, customerNameColumn, titleColumn, descriptionColumn, 
            locationColumn, contactColumn, urlColumn, startColumn, endColumn
        );
        
        if (tableViewContainer.getChildren().size() > 0) {
            tableViewContainer.getChildren().remove(tableView);
        }
        tableViewContainer.getChildren().add(tableView);
     }
    
    /**
     * Start Calendar View
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        createCalendar();
    }
    
class IntegerTableCell extends TableCell<AppointmentModel, Integer> {
    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : getString());
        setGraphic(null);
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
 
class StringTableCell extends TableCell<AppointmentModel, String> {
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : getString());
        setGraphic(null);
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
 
class MouseClickEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent t) {
        TableCell c = (TableCell) t.getSource();
        int index = c.getIndex();
        System.out.println("appointmentId: " + appointmentList.get(index).getAppointmentId());
        System.out.println("customerName: " + appointmentList.get(index).getCustomerName());
        System.out.println("title: " + appointmentList.get(index).getTitle());
        System.out.println("description: " + appointmentList.get(index).getDescription());
        System.out.println("location: " + appointmentList.get(index).getLocation());
        System.out.println("contact: " + appointmentList.get(index).getContact());
        System.out.println("url: " + appointmentList.get(index).getUrl());
        System.out.println("start: " + appointmentList.get(index).getStart());
        System.out.println("end: " + appointmentList.get(index).getEnd());
    }
}    
    
    // Define CalendarView as inner class of CalendarController class
    public class CalendarView {
        private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
        private ArrayList<AnchorPaneNode> allWeekDays = new ArrayList<>(7);
        private Calendar cal = Calendar.getInstance();
        private Text calendarTitle;
        private CalendarController cc;
        private YearMonth currentYearMonth;
        private VBox view;
        private int weekOfYear;
        private int weekYear;
        
        /**
         * Create a calendar view
         * @param yearMonth year month to create the calendar of
         */
        public CalendarView(YearMonth yearMonth) {
            currentYearMonth = yearMonth;
        }

        /**
         * Build calendar (either monthly or weekly)
         */
        @SuppressWarnings("unchecked")
        public void buildCalendar() {
            // Create the calendar grid pane
            GridPane calendar = new GridPane();
            calendar.setPrefSize(500, 200);
            calendar.setGridLinesVisible(true);

            // Create rows and columns with each cell being an anchor pane
            // for a given calendar date
            if (radioMonth.isSelected()) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 7; j++) {
                        AnchorPaneNode ap = new AnchorPaneNode();
                        ap.setPrefSize(200,200);
                        calendar.add(ap, j, i);
                        allCalendarDays.add(ap);
                    }
                }
            }
            else {
                for (int j = 0; j < 7; j++) {
                    AnchorPaneNode ap = new AnchorPaneNode();
                    ap.setPrefSize(200,75);
                    calendar.add(ap, j, 0);
                    allWeekDays.add(ap);
                }
            }

            // Days of the week labels
            Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday") };
            GridPane dayLabels = new GridPane();
            dayLabels.setPrefWidth(500);
            Integer col = 0;

            // Add DOW column headers
            for (Text txt : dayNames) {
                AnchorPane ap = new AnchorPane();
                ap.setPrefSize(200, 10);
                AnchorPane.setBottomAnchor(txt, 5.0);
                ap.getChildren().add(txt);
                dayLabels.add(ap, col++, 0);
            }
            
            HBox titleBar = new HBox();

            if (radioMonth.isSelected()) {
                // Create calendar title and buttons to change current month
                calendarTitle = new Text();
                Button previousMonth = new Button("<<");
                previousMonth.setOnAction(e -> previousMonth());
                Button nextMonth = new Button(">>");
                nextMonth.setOnAction(e -> nextMonth());
                titleBar.setSpacing(10);
                titleBar.getChildren().addAll(previousMonth, calendarTitle, nextMonth);
                titleBar.setAlignment(Pos.BASELINE_CENTER);

                // Populate calendar with the appropriate day numbers
                populateMonthCalendar(currentYearMonth);
            }
            else {
                // Create callendar title and buttons to change current week
                calendarTitle = new Text();
                Button previousWeek = new Button("<<");
                previousWeek.setOnAction(e -> previousWeek());
                Button nextWeek = new Button(">>");
                nextWeek.setOnAction(e -> nextWeek());
                titleBar.setSpacing(10);
                titleBar.getChildren().addAll(previousWeek, calendarTitle, nextWeek);
                titleBar.setAlignment(Pos.BASELINE_CENTER);
                
                // Populate calendar with appropriate day numbers
                weekYear = cal.get(Calendar.YEAR);
                weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                populateWeekCalendar(weekYear, weekOfYear);
            }

            // Create HBox for spacing between titleBar and dayLabels
            Region spacer = new Region();
            spacer.setPrefSize(100.0, 20.0);

            // Create the calendar view
            view = new VBox(titleBar, spacer, dayLabels, calendar);
        }

        /**
         * Set the days of the calendar to correspond to the appropriate date
         * @param yearMonth year and month of month to render
         */
        @SuppressWarnings("unchecked")
        public void populateMonthCalendar(YearMonth yearMonth) {
            String mm;
            String yyyy;

            // Get the date we want to start with on the calendar. calendarDate ends up being the first
            // of the month for the current month (or chosen month) hence the "1" second parameter below 
            LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
            yyyy = Integer.toString(yearMonth.getYear());
            mm = yearMonth.getMonthValue() < 10 ? "0" + Integer.toString(yearMonth.getMonthValue()) : Integer.toString(yearMonth.getMonthValue());

            // Query database to get appointments for the given month
            try {
                cc = CalendarController.this;
                cc.appointmentList = app.db.getAppointmentsByMonth(mm, yyyy);
                cc.lblAppointments.setText("Appointments: " + Integer.toString(cc.appointmentList.size()));
                cc.populateTable();
            }
            catch (SQLException ex) {
                app.common.alertStatus(0);
            }

            // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
            while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
                calendarDate = calendarDate.minusDays(1);
            }

            // Loop through calendar anchor panes, removing children
            for (AnchorPaneNode ap : allCalendarDays) {
                if (!ap.getChildren().isEmpty()) {
                    ap.getChildren().remove(0);
                }

                Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
                String str = calendarDate.toString();
                ap.setDate(calendarDate);
                AnchorPaneNode.setTopAnchor(txt, 5.0);
                AnchorPaneNode.setLeftAnchor(txt, 5.0);
                ap.getChildren().add(txt);
                calendarDate = calendarDate.plusDays(1);
            }
            // Change the title of the month calendar
            calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
        }

        /**
         * Set the days of the calendar to correspond to the appropriate date (week)
         * @param weekYear year for the specified week
         * @param weekOfYear week of the year
         */
        @SuppressWarnings("unchecked")
        public void populateWeekCalendar(int weekYear, int weekOfYear) {
            int[] days = new int[7];
            String endDate;
            int idx = 0;
            int mm;
            int saturday;
            String startDate;
            int sunday;
            
            
            cal.setWeekDate(weekYear, weekOfYear, 1);
            sunday = cal.get(Calendar.DATE);
            cal.add(Calendar.DATE, 6);
            saturday = cal.get(Calendar.DATE);
            mm = cal.get(Calendar.MONTH) + 1;
            cal.set(Calendar.DATE, sunday - 1);
            
            for (int i = 0; i <= 6; i++ ) {
                cal.add(Calendar.DATE, 1);
                days[i] = cal.get(Calendar.DATE);
            }
            
            startDate = String.join("-",
                Integer.toString(weekYear),
                Integer.toString(mm),
                Integer.toString(sunday)
            );
            
            endDate = String.join("-",
                Integer.toString(weekYear),
                Integer.toString(mm),
                Integer.toString(saturday)
            );
            
            // Query database to get appointments for the given month
            try {
                cc = CalendarController.this;
                cc.appointmentList = app.db.getAppointmentsByWeek(startDate, endDate);
                cc.lblAppointments.setText("Appointments: " + Integer.toString(cc.appointmentList.size()));
                cc.populateTable();
            }
            catch (SQLException ex) {
                app.common.alertStatus(0);
            }

            // Loop through calendar anchor panes, removing children
            idx = 0;
            for (AnchorPaneNode ap : allWeekDays) {
                if (!ap.getChildren().isEmpty()) {
                    ap.getChildren().remove(0);
                }

                Text txt = new Text(Integer.toString(days[idx]));
                AnchorPaneNode.setTopAnchor(txt, 5.0);
                AnchorPaneNode.setLeftAnchor(txt, 5.0);
                ap.getChildren().add(txt);
                idx++;
            }
            // Change the title of the week calendar
            calendarTitle.setText("Week Ending " + Integer.toString(mm) + "/" + Integer.toString(saturday));
        }


        /**
         * Move the month forward by one. Re-populate the calendar with the correct dates.
         */
        @SuppressWarnings("unchecked")
        private void nextMonth() {
            currentYearMonth = currentYearMonth.plusMonths(1);
            populateMonthCalendar(currentYearMonth);
        }

        /**
         * Move the week forward by one. Re-populate the calendar with the correct dates.
         */
        @SuppressWarnings("unchecked")
        private void nextWeek() {
            weekOfYear++;
            populateWeekCalendar(weekYear, weekOfYear);
        }

        /**
         * Move the month back by one. Re-populate the calendar with the correct dates.
         */
        @SuppressWarnings("unchecked")
        private void previousMonth() {
            currentYearMonth = currentYearMonth.minusMonths(1);
            populateMonthCalendar(currentYearMonth);
        }

        /**
         * Move the week back by one. Re-populate the calendar with the correct dates.
         */
        @SuppressWarnings("unchecked")
        private void previousWeek() {
            weekOfYear--;
            populateWeekCalendar(weekYear, weekOfYear);
        }
        /**
         * Return View
         * @return view
         */
        @SuppressWarnings("unchecked")
        public VBox getView() {
            return view;
        }

        /**
         * Get all calendar days
         * @return allCalendarDays
         */
        @SuppressWarnings("unchecked")
        public ArrayList<AnchorPaneNode> getAllCalendarDays() {
            return allCalendarDays;
        }

        /**
         * Set all calendar days
         * @param allCalendarDays 
         */
        @SuppressWarnings("unchecked")
        public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
            this.allCalendarDays = allCalendarDays;
        }
    }
}

