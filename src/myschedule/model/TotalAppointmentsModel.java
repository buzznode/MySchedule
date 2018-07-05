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
 *
 * @author bradd
 */
public class TotalAppointmentsModel {
    private final SimpleStringProperty contact;
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty month;
    private final SimpleStringProperty monthName;
    private final SimpleIntegerProperty cnt;
    
    public TotalAppointmentsModel() {
        this.contact = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.month = new SimpleIntegerProperty(0);
        this.monthName = new SimpleStringProperty("");
        this.cnt = new SimpleIntegerProperty(0);
    }
    
    public TotalAppointmentsModel(String _contact, String _description, int _month, String _monthName, int _cnt) {
        this.contact = new SimpleStringProperty(_contact);
        this.description = new SimpleStringProperty(_description);
        this.month = new SimpleIntegerProperty(_month);
        this.monthName = new SimpleStringProperty(_monthName);
        this.cnt = new SimpleIntegerProperty(_cnt);
    }
    
    /**
     * Get contact value
     * @return contact value
     */
    public String getContact() {
        return contact.get();
    }
    
    /**
     * Set contact value
     * @param _contact
     */
    public void setContact(String _contact) {
        contact.set(_contact);
    }

    /**
     * Get description value
     * @return description value
     */
    public String getDescription() {
        return description.get();
    }
    
    /**
     * Set description value
     * @param _description
     */
    public void setDescription(String _description) {
        description.set(_description);
    }

    /**
     * Get month value
     * @return month value
     */
    public int getMonth() {
        return month.get();
    }
    
    /**
     * Set month value
     * @param _month
     */
    public void setMonth(int _month) {
        month.set(_month);
    }

    /**
     * Get monthName value
     * @return monthName value
     */
    public String getMonthName() {
        return monthName.get();
    }
    
    /**
     * Set monthName value
     * @param _monthName
     */
    public void setMonthName(String _monthName) {
        monthName.set(_monthName);
    }

        /**
     * Get cnt value
     * @return cnt value
     */
    public int getCnt() {
        return cnt.get();
    }
    
    /**
     * Set cnt value
     * @param _cnt
     */
    public void setCnt(int _cnt) {
        cnt.set(_cnt);
    }
}
