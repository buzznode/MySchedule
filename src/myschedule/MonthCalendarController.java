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
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * @author bradd
 * @version 0.5.0
 */
@SuppressWarnings("unchecked")
public class MonthCalendarController {
    @FXML Label lblTitle;
    @FXML Pane monthCalendarPane;

    private App app;
    private MainController main;
    
    /**
     * Add listeners
     */
    @SuppressWarnings("unchecked")
    private void addListeners() {
        
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
     * Start country maintenance
     */
    @SuppressWarnings("unchecked")
    public void start() {
        addListeners();
        lblTitle.setText(app.localize("appointments_month_view"));
        
//        initializeForm();
    }
}

