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
public class City {
    protected static final Map<String, City> CITIES = new HashMap<String, City>();
    protected String cityId = "";
    protected String city = "";
    protected int countryId = 0;
    protected String createDate = "";
    protected String createdBy = "";
    protected String lastUpdate = "";
    protected String lastUpdateBy = "";
    
    /**
     * @param _cityId
     * @return City object
     */
    protected City of(String _cityId) {
        City xCity = CITIES.get(_cityId);
        
        if (xCity == null) {
            xCity = new City(_cityId);
            CITIES.put(_cityId, xCity);
        }
        
        return xCity;
    }
    
    /**
     * @param _cityId 
     */
    protected City(String _cityId) {
        this.cityId = _cityId;
    }
    
    /* Getters & Setters */
    
    /**
     * @return cityId
     */
    protected String cityId() {
        return cityId;
    }
    
    /**
     * @param _cityId
     * @return cityId
     */
    protected String cityId(String _cityId) {
        return cityId = _cityId;
    }
    
    /**
     * @param _city
     * @return city
     */
    protected String city(String _city) {
        return city = _city;
    }
    
    /**
     * @return countryId
     */
    public int countryId() {
        return countryId;
    }

    /**
     * @param _countryId
     * @return 
     */
    public int setCountryId(int _countryId) {
        return countryId = _countryId;
    }
    
    /**
     * @return createDate
     */
    public String getCreateDate() {
        return createDate;
    }
    
    /**
     * @param _createDate 
     */
    public void setCreateDate(String _createDate) {
        this.createDate = _createDate;
    }
    
    /**
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }
    
    /**
     * @param _createdBy 
     */
    public void setCreatedBy(String _createdBy) {
        this.createdBy = _createdBy;
    }
    
    /**
     * @return lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }
    
    /**
     * @param _lastUpdate
     */
    public void setLastUpdate(String _lastUpdate) {
        this.lastUpdate = _lastUpdate;
    }
    
    /**
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdateBy;
    }
    
    /**
     * @param _lastUpdateBy
     */
    public void setLastUpdateBy(String _lastUpdateBy) {
        this.lastUpdateBy = _lastUpdateBy;
    }
}
