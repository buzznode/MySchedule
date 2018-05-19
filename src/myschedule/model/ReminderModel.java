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
public class ReminderModel {
    private int reminderId;
    private LocalDateTime reminderDate;
    private int snoozeIncrement;
    private int snoozeIncrementTypeId;
    private int appointmentId;
    private String createdBy;
    private LocalDateTime createdDate;
    private String remindercol;
    
    /**
     * @return reminderId
     */
    public int reminderId() {
        return reminderId;
    }
    
    /**
     * @param _reminderId
     * @return reminderId
     */
    public int reminderId(int _reminderId) {
        return reminderId = _reminderId;
    }
    
    /**
     * @return reminderDate
     */
    public LocalDateTime reminderDate() {
        return reminderDate;
    }
    
    /**
     * @param _reminderDate
     * @return reminderDate
     */
    public LocalDateTime reminderDate(LocalDateTime _reminderDate) {
        return reminderDate = _reminderDate;
    }
    
    /**
     * @return snoozeIncrement
     */
    public int snoozeIncrement() {
        return snoozeIncrement;
    }
    
    /**
     * @param _snoozeIncrement
     * @return snoozeIncrement
     */
    public int snoozeIncrement(int _snoozeIncrement) {
        return snoozeIncrement = _snoozeIncrement;
    }
    
    /**
     * @return snoozeIncrementTypeId
     */
    public int snoozeIncrementTypeId() {
        return snoozeIncrementTypeId;
    }
    
    /**
     * @param _snoozeIncrementTypeId
     * @return snoozeIncrementTypeId
     */
    public int snoozeIncrementTypeId(int _snoozeIncrementTypeId) {
        return snoozeIncrementTypeId = _snoozeIncrementTypeId;
    }
    
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
     * @return createdDate
     */
    public LocalDateTime createdDate() {
        return createdDate;
    }
    
    /**
     * @param _createdDate
     * @return createdDate
     */
    public LocalDateTime createdDate(LocalDateTime _createdDate) {
        return createdDate = _createdDate;
    }
    
    /**
     * @return remindercol
     */
    public String remindercol() {
        return remindercol;
    }
    
    /**
     * 
     * @param _remindercol
     * @return remindercol
     */
    public String remindercol(String _remindercol) {
        return remindercol = _remindercol;
    }
}
