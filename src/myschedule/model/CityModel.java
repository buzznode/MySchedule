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
public class CityModel {
    private final SimpleIntegerProperty cityId;
    private final SimpleStringProperty city;
    private final SimpleStringProperty country;
    private final SimpleStringProperty createDate;
    private final SimpleStringProperty createdBy;
    private final SimpleStringProperty lastUpdate;
    private final SimpleStringProperty lastUpdateBy;
    
    public CityModel() {
        this.cityId = new SimpleIntegerProperty(0);
        this.city = new SimpleStringProperty("");
        this.country = new SimpleStringProperty("");
        this.createDate = new SimpleStringProperty("");
        this.createdBy = new SimpleStringProperty("");
        this.lastUpdate = new SimpleStringProperty("");
        this.lastUpdateBy = new SimpleStringProperty("");
    }
    
    public CityModel(int _cityId, String _city, String _country, String _createDate, String _createdBy, String _lastUpdate, String _lastUpdateBy) {
        this.cityId = new SimpleIntegerProperty(_cityId);
        this.city = new SimpleStringProperty(_city);
        this.country = new SimpleStringProperty(_country);
        this.createDate = new SimpleStringProperty(_createDate);
        this.createdBy = new SimpleStringProperty(_createdBy);
        this.lastUpdate = new SimpleStringProperty(_lastUpdate);
        this.lastUpdateBy = new SimpleStringProperty(_lastUpdateBy);
    }
    
    /**
     * Get cityId value
     * @return cityId value
     */
    @SuppressWarnings("unchecked")
    public int getCityId() {
        return cityId.get();
    }
    
    /**
     * Set cityId value
     * @param _cityId 
     */
    @SuppressWarnings("unchecked")
    public void setCityId(int _cityId) {
        cityId.set(_cityId);
    }

    /**
     * Get city value
     * @return city value
     */
    @SuppressWarnings("unchecked")
    public String getCity() {
        return city.get();
    }

    /**
     * Set city value
     * @param _city 
     */
    @SuppressWarnings("unchecked")
    public void setCity(String _city) {
        city.set(_city);
    }
    
    /**
     * Get country value
     * @return country value
     */
    @SuppressWarnings("unchecked")
    public String getCountry() {
        return country.get();
    }

    /**
     * Set country value
     * @param _country 
     */
    @SuppressWarnings("unchecked")
    public void setCountry(String _country) {
        country.set(_country);
    }
    
    /**
     * Get country object
     * @return country object
     */
//    public StringProperty countryProperty() {
//        return country;
//    }
    
    /**
     * Get createDate value
     * @return createDate value
     */
    @SuppressWarnings("unchecked")
    public String getCreateDate() {
        return createDate.get();
    }

    /**
     * Set createDate value
     * @param _createDate 
     */
    @SuppressWarnings("unchecked")
    public void setCreateDate(String _createDate) {
        createDate.set(_createDate);
    }

    /**
     * Get createdBy value
     * @return createdBy value
     */
    @SuppressWarnings("unchecked")
    public String getCreatedBy() {
        return createdBy.get();
    }

    /**
     * Get createdBy value
     * @param _createdBy 
     */
    @SuppressWarnings("unchecked")
    public void setCreatedBy(String _createdBy) {
        createdBy.set(_createdBy);
    }

    /**
     * Get lastUpdate value
     * @return lastUpdate value
     */
    @SuppressWarnings("unchecked")
    public String getLastUpdate() {
        return lastUpdate.get();
    }

    /**
     * Set lastUpdate value
     * @param _lastUpdate 
     */
    @SuppressWarnings("unchecked")
    public void setLastUpdate(String _lastUpdate) {
        lastUpdate.set(_lastUpdate);
    }

    /**
     * Get lastUpdateBy value
     * @return lastUpdateBy value
     */
    @SuppressWarnings("unchecked")
    public String getLastUpdateBy() {
        return lastUpdateBy.get();
    }

    /**
     * Set lastUpdateBy value
     * @param _lastUpdateBy 
     */
    @SuppressWarnings("unchecked")
    public void setLastUpdateBy(String _lastUpdateBy) {
        lastUpdateBy.set(_lastUpdateBy);
    }
}
