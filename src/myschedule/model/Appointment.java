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

import java.time.LocalDateTime;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Appointment {
    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;

    /**
     * @return appointmentId
     */
    public int appointmentId() {
        return appointmentId;
    }
    
    /**
     * @param _appointmentId
     * @return appointmentId
     */
    public int appointmentId(int _appointmentId) {
        return appointmentId = _appointmentId;
    }
    
    /**
     * @return customerId
     */
    public int customerId() {
        return customerId;
    }
    
    /**
     * @param _customerId
     * @return customerId
     */
    public int customerId(int _customerId) {
        return customerId = _customerId;
    }
    
    /**
     * @return title
     */
    public String title() {
        return title;
    }
    
    /**
     * @param _title
     * @return title
     */
    public String title(String _title) {
        return title = _title;
    }
    
    /**
     * @return description
     */
    public String description() {
        return description;
    }
    
    /**
     * @param _description
     * @return description
     */
    public String description(String _description) {
        return description = _description;
    }
    
    /**
     * @return location
     */
    public String location() {
        return location;
    }
    
    /**
     * @param _location
     * @return location
     */
    public String location(String _location) {
        return location = _location;
    }
    
    /**
     * @return contact
     */
    public String contact() {
        return contact;
    }
    
    /**
     * @param _contact
     * @return contact
     */
    public String contact(String _contact) {
        return contact = _contact;
    }
    
    /**
     * @return url
     */
    public String url() {
        return url;
    }
    
    /**
     * @param _url
     * @return url
     */
    public String url(String _url) {
        return url = _url;
    }
    
    /**
     * @return start
     */
    public LocalDateTime start() {
        return start;
    }
    
    /**
     * @param _start
     * @return start
     */
    public LocalDateTime start(LocalDateTime _start) {
        return start = _start;
    }
    
    /**
     * @return end
     */
    public LocalDateTime end() {
        return end;
    }
    
    /**
     * @param _end
     * @return end
     */
    public LocalDateTime end(LocalDateTime _end) {
        return end = _end;
    }
    
    /**
     * @return createDate
     */
    public LocalDateTime createDate() {
        return createDate;
    }
    
    /**
     * @param _createDate
     * @return createDate
     */
    public LocalDateTime createDate(LocalDateTime _createDate) {
        return createDate = _createDate;
    }
    
    /**
     * @return createdBy
     */
    public String createdBy() {
        return createdBy;
    }
    
    /**
     * @param _createdBy
     * @return createdBy
     */
    public String createdBy(String _createdBy) {
        return createdBy = _createdBy;
    }
    
    /**
     * @return lastUpdate
     */
    public LocalDateTime lastUpdate() {
        return lastUpdate;
    }
    
    /**
     * @param _lastUpdate
     * @return lastUpdate
     */
    public LocalDateTime lastUpdate(LocalDateTime _lastUpdate) {
        return lastUpdate = _lastUpdate;
    }
    
    /**
     * @return lastUpdateBy
     */
    public String lastUpdateBy() {
        return lastUpdateBy;
    }
    
    /**
     * @param _lastUpdateBy
     * @return lastUpdateBy
     */
    public String lastUpdateBy(String _lastUpdateBy) {
        return lastUpdateBy = _lastUpdateBy;
    }
}
