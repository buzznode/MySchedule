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

import java.util.Date;

/**
 * @author bradd
 * @version 0.5.0
 */
public class User {

    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private String createBy;
    private Date createDate;
    private Date lastUpdate;
    private String lastUpdatedBy;
    
    /**
     * @return addressId
     */
    public int addressId() {
        return addressId;
    }
    
    /**
     * @param _addressId
     * @return addressId
     */
    public int addressId(int _addressId) {
        return addressId = _addressId;
    }
    
    /**
     * @return address
     */
    public String address() {
        return address;
    }
    
    /**
     * @param _address
     * @return address
     */
    public String address(String _address) {
        return address = _address;
    }
    
    /**
     * @return address2
     */
    public String address2() {
        return address2;
    }
    
    /**
     * @param _address2
     * @return address2
     */
    public String address2(String _address2) {
        return address2 = _address2;
    }

    /**
     * @return cityId
     */
    public int cityId() {
        return cityId;
    }
    
    /**
     * @param _cityId
     * @return cityId
     */
    public int cityId(int _cityId) {
        return cityId = _cityId;
    }
    
    /**
     * @return postalCode
     */
    public String postalCode() {
        return postalCode;
    }
    
    /**
     * @param _postalCode
     * @return postalCode
     */
    public String postalCode(String _postalCode) {
        return postalCode = _postalCode;
    }
    
    /**
     * @return phone
     */
    public String phone() {
        return phone;
    }
    
    /**
     * @param _phone
     * @return phone
     */
    public String phone(String _phone) {
        return phone = _phone;
    }
    
    /**
     * @return createDate
     */
    public Date createDate() {
        return createDate;
    }
    
    /**
     * @param _createDate
     * @return createDate
     */
    public Date createDate(Date _createDate) {
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
    public Date lastUpdate() {
        return lastUpdate;
    }

/**
 * @param _lastUpdate
 * @return lastUpdate
 */    
    public Date lastUpdate(Date _lastUpdate) {
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