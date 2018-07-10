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
package myschedule.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author bradd
 * @version 0.5.0
 */
public class AppointmentTypeCountModel {
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty cnt;
    private final SimpleIntegerProperty month;
    private final SimpleStringProperty monthName;

    public AppointmentTypeCountModel() {
        this.description = new SimpleStringProperty("");
        this.cnt = new SimpleIntegerProperty(0);
        this.month = new SimpleIntegerProperty(0);
        this.monthName = new SimpleStringProperty("");
    }
    
    public AppointmentTypeCountModel(String _description, int _cnt, int _month, String _monthName) {
        this.description = new SimpleStringProperty(_description);
        this.cnt = new SimpleIntegerProperty(_cnt);
        this.month = new SimpleIntegerProperty(_month);
        this.monthName = new SimpleStringProperty(_monthName);
    }
    
    /**
     * Get description value
     * @return description value
     */
    @SuppressWarnings("unchecked")
    public String getDescription() {
        return description.get();
    }
    
    /**
     * Set description value
     * @param _description
     */
    @SuppressWarnings("unchecked")
    public void setDescription(String _description) {
        description.set(_description);
    }
    
    /**
     * Get cnt value
     * @return cnt value
     */
    @SuppressWarnings("unchecked")
    public int getCnt() {
        return cnt.get();
    }
    
    /**
     * Set cnt value
     * @param _cnt
     */
    @SuppressWarnings("unchecked")
    public void setCnt(int _cnt) {
        cnt.set(_cnt);
    }

    /**
     * Get month value
     * @return month value
     */
    @SuppressWarnings("unchecked")
    public int getMonth() {
        return month.get();
    }
    
    /**
     * Set month value
     * @param _month
     */
    @SuppressWarnings("unchecked")
    public void setMonth(int _month) {
        month.set(_month);
    }
    
    /**
     * Get monthName value
     * @return monthName value
     */
    @SuppressWarnings("unchecked")
    public String getMonthName() {
        return monthName.get();
    }
    
    /**
     * Set monthName value
     * @param _monthName
     */
    @SuppressWarnings("unchecked")
    public void setMonthName(String _monthName) {
        monthName.set(_monthName);
    }
}
