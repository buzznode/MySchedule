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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author bradd
 * @version 0.5.0
 */
public class CustomerModel {
    private final SimpleIntegerProperty customerId;
    private final SimpleStringProperty customerName;
    private final SimpleIntegerProperty addressId;
    private final SimpleBooleanProperty active;
    private final SimpleStringProperty createDate;
    private final SimpleStringProperty createdBy;
    private final SimpleStringProperty lastUpdate;
    private final SimpleStringProperty lastUpdateBy;
    
    public CustomerModel() {
        this.customerId = new SimpleIntegerProperty(0);
        this.customerName = new SimpleStringProperty("");
        this.addressId = new SimpleIntegerProperty(0);
        this.active = new SimpleBooleanProperty(true);
        this.createDate = new SimpleStringProperty("");
        this.createdBy = new SimpleStringProperty("");
        this.lastUpdate = new SimpleStringProperty("");
        this.lastUpdateBy = new SimpleStringProperty("");
    }
    
    public CustomerModel(int _customerId, String _customerName, int _addressId, boolean _active, 
            String _createDate, String _createdBy, String _lastUpdate, String _lastUpdateBy) {
        this.customerId = new SimpleIntegerProperty(_customerId);
        this.customerName = new SimpleStringProperty(_customerName);
        this.addressId = new SimpleIntegerProperty(_addressId);
        this.active = new SimpleBooleanProperty(_active);
        this.createDate = new SimpleStringProperty(_createDate);
        this.createdBy = new SimpleStringProperty(_createdBy);
        this.lastUpdate = new SimpleStringProperty(_lastUpdate);
        this.lastUpdateBy = new SimpleStringProperty(_lastUpdateBy);
    }
    
    /**
     * Get customerId value
     * @return customerId value
     */
    public int getCustomerId() {
        return customerId.get();
    }
    
    /**
     * Set customerId value
     * @param _customerId 
     */
    public void setCustomerId(int _customerId) {
        customerId.set(_customerId);
    }

    /**
     * Get customerName value
     * @return customerName value
     */
    public String getCustomerName() {
        return customerName.get();
    }

    /**
     * Set customerName value
     * @param _customerName 
     */
    public void setCustomerName(String _customerName) {
        customerName.set(_customerName);
    }
    
    /**
     * Get addressId value
     * @return addressId value
     */
    public int getAddressId() {
        return addressId.get();
    }
    
    /**
     * Set addressId value
     * @param _addressId 
     */
    public void setAddressId(int _addressId) {
        addressId.set(_addressId);
    }
    
    /**
     * Get active value
     * @return active value
     */
    public boolean getActive() {
        return active.get();
    }

    /**
     * Set active value
     * @param _active 
     */
    public void setActive(boolean _active) {
        active.set(_active);
    }
    
    /**
     * Get createDate value
     * @return createDate value
     */
    public String getCreateDate() {
        return createDate.get();
    }

    /**
     * Set createDate value
     * @param _createDate 
     */
    public void setCreateDate(String _createDate) {
        createDate.set(_createDate);
    }

    /**
     * Get createdBy value
     * @return createdBy value
     */
    public String getCreatedBy() {
        return createdBy.get();
    }

    /**
     * Get createdBy value
     * @param _createdBy 
     */
    public void setCreatedBy(String _createdBy) {
        createdBy.set(_createdBy);
    }

    /**
     * Get lastUpdate value
     * @return lastUpdate value
     */
    public String getLastUpdate() {
        return lastUpdate.get();
    }

    /**
     * Set lastUpdate value
     * @param _lastUpdate 
     */
    public void setLastUpdate(String _lastUpdate) {
        lastUpdate.set(_lastUpdate);
    }

    /**
     * Get lastUpdateBy value
     * @return lastUpdateBy value
     */
    public String getLastUpdateBy() {
        return lastUpdateBy.get();
    }

    /**
     * Set lastUpdateBy value
     * @param _lastUpdateBy 
     */
    public void setLastUpdateBy(String _lastUpdateBy) {
        lastUpdateBy.set(_lastUpdateBy);
    }
}