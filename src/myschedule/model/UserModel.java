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
public class UserModel {

    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private String createBy;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    
    /**
     * @return userId
     */
    public int userId() {
        return userId;
    }
    
    /**
     * @param _userId
     * @return userId
     */
    public int userId(int _userId) {
        return userId = _userId;
    }
    
    /**
     * @return userName
     */
    public String userName() {
        return userName;
    }
    
    /**
     * @param _userName
     * @return userName
     */
    public String userName(String _userName) {
        return userName = _userName;
    }
    
    /**
     * @return password
     */
    public String password() {
        return password;
    }
    
    /**
     * @param _password
     * @return password
     */
    public String password(String _password) {
        return password = _password;
    }

    /**
     * @return active
     */
    public boolean active() {
        return active;
    }
    
    /**
     * @param _active
     * @return active
     */
    public boolean active(boolean _active) {
        return active = _active;
    }
    
    /**
     * @return createBy
     */
    public String createBy() {
        return createBy;
    }
    
    /**
     * @param _createBy
     * @return createBy
     */
    public String createBy(String _createBy) {
        return createBy = _createBy;
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
     * @return lastUpdatedBy
     */
    public String lastUpdatedBy() {
        return lastUpdatedBy;
    }
    
    /**
     * @param _lastUpdatedBy
     * @return lastUpdatedBy
     */
    public String lastUpdatedBy(String _lastUpdatedBy) {
        return lastUpdatedBy = _lastUpdatedBy;
    }
}