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

import java.util.HashMap;
import java.util.Map;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Address {

    protected static final Map<String, Address> ADDRESSES = new HashMap<String, Address>();
    protected String addressId = "";
    protected String address = "";
    protected String address2 = "";
    protected int cityId = 0;
    protected String postalCode = "";
    protected String phone = "";
    protected String createDate = "";
    protected String createdBy = "";
    protected String lastUpdate = "";
    protected String lastUpdateBy = "";
    
    /**
     * @param _addressId
     * @return Address object
     */
    protected Address of(String _addressId) {
        Address xAddress = ADDRESSES.get(_addressId);
        
        if (xAddress == null) {
            xAddress = new Address(_addressId);
            ADDRESSES.put(_addressId, xAddress);
        }
        
        return xAddress;
    }
    
    /**
     * @param _addressId 
     */
    protected Address(String _addressId) {
        this.addressId = _addressId;
    }
    
    /* Getters & Setters */
    
    /**
     * @return addressId
     */
    protected String addressId() {
        return addressId;
    }
    
    /**
     * @param _addressId
     * @return addressId
     */
    protected String addressId(String _addressId) {
        return addressId = _addressId;
    }
    
    /**
     * @return address
     */
    protected String address() {
        return address;
    }
    
    /**
     * @param _address
     * @return address
     */
    protected String address(String _address) {
        return address = _address;
    }
    
    /**
     * @return address2
     */
    protected String address2() {
        return address2;
    }
    
    /**
     * @param _address2
     * @return address2
     */
    protected String address2(String _address2) {
        return address2 = _address2;
    }
    
    /**
     * @return cityId
     */
    protected int cityId() {
        return cityId;
    }
    
    /**
     * @param _cityId
     * @return cityId
     */
    protected int cityId(int _cityId) {
        return cityId = _cityId;
    }

    /**
     * @return postalCode
     */
    protected String postalCode() {
        return postalCode;
    }
    
    /**
     * @param _postalCode
     * @return postalCode
     */
    protected String postalCode(String _postalCode) {
        return postalCode = _postalCode;
    }
    
    /**
     * @return phone
     */
    protected String phone() {
        return phone;
    }
    
    /**
     * @param _phone
     * @return phone
     */
    protected String phone(String _phone) {
        return phone = _phone;
    }
    
    /**
     * @return createDate
     */
    protected String createDate() {
        return createDate;
    }

    /**
     * @param _createDate
     * @return createDate
     */
    protected String _createDate(String _createDate) {
        return createDate = _createDate;
    }
    
    /**
     * @return createdBy
     */
    protected String createdBy() {
        return createdBy;
    }
    
    /**
     * @param _createdBy
     * @return createdBy
     */
    protected String createdBy(String _createdBy) {
        return createdBy = _createdBy;
    }
    
    /**
     * @return lastUpdate
     */
    protected String lastUpdate() {
        return lastUpdate;
    }
    
    /**
     * @param _lastUpdate
     * @return lastUpdate
     */
    protected String lastUpdate(String _lastUpdate) {
        return lastUpdate = _lastUpdate;
    }
    
    /**
     * @return lastUpdateBy
     */
    protected String lastUpdateBy() {
        return lastUpdateBy;
    }
    
    /**
     * @param _lastUpdateBy
     * @return lastUpdateBy
     */
    protected String lastUpdateBy(String _lastUpdateBy) {
        return lastUpdateBy = _lastUpdateBy;
    }
}

