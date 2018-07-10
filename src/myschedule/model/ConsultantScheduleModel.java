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
public class ConsultantScheduleModel {
    private final SimpleIntegerProperty customerId;
    private final SimpleStringProperty customerName;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;
    private final SimpleStringProperty location;
    private final SimpleStringProperty contact;
    private final SimpleStringProperty start;
    private final SimpleIntegerProperty month;
    private final SimpleStringProperty monthName;
    private final SimpleStringProperty end;

    public ConsultantScheduleModel() {
        this.customerId = new SimpleIntegerProperty(0);
        this.customerName = new SimpleStringProperty("");
        this.title = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.location = new SimpleStringProperty("");
        this.contact = new SimpleStringProperty("");
        this.start = new SimpleStringProperty("");
        this.month = new SimpleIntegerProperty(0);
        this.monthName = new SimpleStringProperty("");
        this.end = new SimpleStringProperty("");
    }
    
    public ConsultantScheduleModel(int _customerId, String _customerName, String _title, String _description, String _location, 
                                                                     String _contact, String _start, int _month, String _monthName, String _end) {
        this.customerId = new SimpleIntegerProperty(_customerId);
        this.customerName = new SimpleStringProperty(_customerName);
        this.title = new SimpleStringProperty(_title);
        this.description = new SimpleStringProperty(_description);
        this.location = new SimpleStringProperty(_location);
        this.contact = new SimpleStringProperty(_contact);
        this.start = new SimpleStringProperty(_start);
        this.month = new SimpleIntegerProperty(_month);
        this.monthName = new SimpleStringProperty(_monthName);
        this.end = new SimpleStringProperty(_end);
    }
    
    /**
     * Get customerId value
     * @return description value
     */
    @SuppressWarnings("unchecked")
    public int getCustomerId() {
        return customerId.get();
    }
    
    /**
     * Set customerId value
     * @param _customerId
     */
    @SuppressWarnings("unchecked")
    public void setCustomerId(int _customerId) {
        customerId.set(_customerId);
    }
    
    /**
     * Get customerName value
     * @return customerName value
     */
    @SuppressWarnings("unchecked")
    public String getCustomerName() {
        return customerName.get();
    }
    
    /**
     * Set customerName value
     * @param _customerName
     */
    @SuppressWarnings("unchecked")
    public void setCustomerName(String _customerName) {
        customerName.set(_customerName);
    }

    /**
     * Get title value
     * @return title value
     */
    @SuppressWarnings("unchecked")
    public String getTitle() {
        return title.get();
    }
    
    /**
     * Set title value
     * @param _title
     */
    @SuppressWarnings("unchecked")
    public void setTitle(String _title) {
        title.set(_title);
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
     * Get location value
     * @return location value
     */
    @SuppressWarnings("unchecked")
    public String getLocation() {
        return location.get();
    }
    
    /**
     * Set location value
     * @param _location
     */
    @SuppressWarnings("unchecked")
    public void setLocation(String _location) {
        location.set(_location);
    }

    /**
     * Get contact value
     * @return contact value
     */
    @SuppressWarnings("unchecked")
    public String getContact() {
        return contact.get();
    }
    
    /**
     * Set contact value
     * @param _contact
     */
    @SuppressWarnings("unchecked")
    public void setContact(String _contact) {
        contact.set(_contact);
    }

    /**
     * Get start value
     * @return start value
     */
    @SuppressWarnings("unchecked")
    public String getStart() {
        return start.get();
    }
    
    /**
     * Set start value
     * @param _start
     */
    @SuppressWarnings("unchecked")
    public void setStart(String _start) {
        start.set(_start);
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

    /**
     * Get end value
     * @return end value
     */
    @SuppressWarnings("unchecked")
    public String getEnd() {
        return end.get();
    }
    
    /**
     * Set end value
     * @param _end
     */
    @SuppressWarnings("unchecked")
    public void setEnd(String _end) {
        end.set(_end);
    }
}
