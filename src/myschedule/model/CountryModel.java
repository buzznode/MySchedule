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
public class CountryModel {
    private final SimpleIntegerProperty countryId;
    private final SimpleStringProperty country;
    private final SimpleStringProperty createDate;
    private final SimpleStringProperty createdBy;
    private final SimpleStringProperty lastUpdate;
    private final SimpleStringProperty lastUpdateBy;
    
    private CountryModel(int _countryId, String _country, String _createDate, String _createdBy,
        String _lastUpdate, String _lastUpdateBy) {
        this.countryId = new SimpleIntegerProperty(_countryId);
        this.country = new SimpleStringProperty(_country);
        this.createDate = new SimpleStringProperty(_createDate);
        this.createdBy = new SimpleStringProperty(_createdBy);
        this.lastUpdate = new SimpleStringProperty(_lastUpdate);
        this.lastUpdateBy = new SimpleStringProperty(_lastUpdateBy);
    }
    
    /**
     * @return countryId
     */
    public int countryId() {
        return countryId.get();
    }
    
    /**
     * @param _countryId
     */
    public void countryId(int _countryId) {
        countryId.set(_countryId);
    }
    
    /**
     * @return country
     */
    public String country() {
        return country.get();
    }
    
    /**
     * @param _country
     */
    public void country(String _country) {
        country.set(_country);
    }
    
    /**
     * @return createDate
     */
    public String createDate() {
        return createDate.get();
    }
    
    /**
     * @param _createDate
     */
    public void createDate(String _createDate) {
        createDate.set(_createDate);
    }
    
    /**
     * @return createdBy
     */
    public String createdBy() {
        return createdBy.get();
    }
    
    /**
     * @param _createdBy
     */
    public void createdBy(String _createdBy) {
        createdBy.set(_createdBy);
    }
    
    /**
     * @return lastUpdate
     */
    public String lastUpdate() {
        return lastUpdate.get();
    }
    
    /**
     * @param _lastUpdate
     */
    public void lastUpdate(String _lastUpdate) {
        lastUpdate.set(_lastUpdate);
    }
    
    /**
     * @return lastUpdateBy
     */
    public String lastUpdateBy() {
        return lastUpdateBy.get();
    }
    
    /**
     * @param _lastUpdateBy
     */
    public void lastUpdateBy(String _lastUpdateBy) {
        lastUpdateBy.set(_lastUpdateBy);
    }
}
