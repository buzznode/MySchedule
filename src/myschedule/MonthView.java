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

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.layout.Region;

/**
 * @author bradd
 * @version 0.5.0
 */
public class MonthView {
    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    private App app;
    private MainController main;
    

    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public MonthView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
    }
    
    public void buildCalendar() {
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(300, 200);
        calendar.setGridLinesVisible(true);
        
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(200,200);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }
        
        // Days of the week labels
        Text[] dayNames = new Text[]{ new Text("Sun"), new Text("Mon"), new Text("Tue"),
            new Text("Wed"), new Text("Thu"), new Text("Fri"), new Text("Sat") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(300);
        Integer col = 0;
        
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            AnchorPane.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox();
        titleBar.setSpacing(10);
        titleBar.getChildren().addAll(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        
        // Populate calendar with the appropriate day numbers
        populateCalendar(currentYearMonth);
        
        // Create HBox for spacing between titleBar and dayLabels
        Region spacer = new Region();
        spacer.setPrefSize(100.0, 20.0);
        
        // Create the calendar view
        view = new VBox(titleBar, spacer, dayLabels, calendar);
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
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(formatter);
        String yyyy = Integer.toString(LocalDate.now().getYear());
        String mm = Integer.toString(LocalDate.now().getMonthValue());
        mm = mm.length() < 2 ? "0" + mm : mm;
        String dd = Integer.toString(LocalDate.now().getDayOfMonth());
        dd = dd.length() < 2 ? "0" + dd : dd;
        System.out.println("yyyy: " + yyyy + "; mm: " + mm + "; dd: " + dd);
        
        // Get resultset of appointments for given month / year
        try {
            ResultSet rs = app.db.getAppointments(mm, yyyy);
        }
        catch (SQLException ex) {
            app.common.alertStatus(0);
        }
        
        
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (!ap.getChildren().isEmpty()) {
                ap.getChildren().remove(0);
            }
            
            // here's where we're looping through stuff and building te calendar. Perhaps we should
           // have code here to add a background color or count of scheduled appointments or soemthing 
           
           //  java resultset filtering on condition
           // https://stackoverflow.com/questions/33817166/multiple-conditions-to-filter-a-result-set-using-java-8
           
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            AnchorPaneNode.setTopAnchor(txt, 5.0);
            AnchorPaneNode.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            
            if (txt.getText().equals("15")) {
                ap.setStyle("-fx-background-color: lime");
            }
            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Re-populate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Move the month forward by one. Re-populate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
