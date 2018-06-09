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
package myschedule.service;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import myschedule.model.AddressModel;
import myschedule.model.CityModel;
import myschedule.model.CountryModel;
import myschedule.model.CustomerModel;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * @author bradd
 * @version 0.5.0
 */
public class DB {
    Connection conn;
    String db;
    String driver;
    String dbPwd;
    String dbUser;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement stmt;
    String url;
    private Logging log;
    
    /**
     * Default constructor
     */
    public DB() {
        conn = null;
        db = "U03MuY";
        dbPwd = "53688020218";
        dbUser = "U03MuY";
        driver = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://52.206.157.109/" + db;
    }
    
    /**
     * Constructor taking log parameter
     * @param _log 
     */
    public DB(Logging _log) {
        conn = null;
        db = "U03MuY";
        dbPwd = "53688020218";
        dbUser = "U03MuY";
        driver = "com.mysql.jdbc.Driver";
        log = _log;
        url = "jdbc:mysql://52.206.157.109/" + db;
    }
    
    /**
     * Connect to database
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    protected void connect() throws SQLException {
        try {
            try {
                Class.forName(driver);
            }
            catch (ClassNotFoundException e) {
                log.write(Level.SEVERE, "DB driver error: " + e.toString());
            }
            conn = DriverManager.getConnection(url, dbUser, dbPwd);
            stmt = conn.createStatement();
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
    }

    /**
     * Replace tick-marks with escaped flavor
     * @param str (String)
     * @return String containing escaped tick-marks
     */
    private String escapeTicks(String str) {
        return str.replaceAll("'", "\'");
    }

    /**
     * Creates an Exception Message
     * @param ex (SQLException)
     * @return Exception message
     */
    private String exception(SQLException ex) {
        log.write(Level.SEVERE, ex.toString(), ex);
        log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
        log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
        log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
        String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
        return msg;
    }
    
    /**
     * Execute SQL and return result
     * @param sql
     * @return Query ResultSet
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    protected ResultSet exec(String sql) throws SQLException {
        ResultSet rset = null;

        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
            rset =  stmt.executeQuery(sql);
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return rset;
    }

    /**
     * Finalize
     * @throws Throwable 
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void finalize() throws Throwable {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } 
        finally {
            super.finalize();
        }
    }

    /**
     * Get Address Data for customer maintenance
     * @param addressId
     * @return ResultSet
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public ResultSet getAddressData(int addressId) throws SQLException {
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.address, a.address2, b.city, a.postalCode, a.phone, c.country",
            "FROM address a",
            "JOIN city b ON b.cityId = a.cityId",
            "JOIN country c ON c.countryId = b.countryId",
            "WHERE a.addressId = " + addressId
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return rs;
    }
    
    /**
     * Get list of Addresses
     * @param sortColumn
     * @param direction
     * @return ObservableList (AddressModel)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AddressModel> getAddressModelList(String sortColumn, String direction) throws SQLException {
        ObservableList<AddressModel> list = FXCollections.observableArrayList();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.addressId, a.address, a.address2, b.city, b.cityId, a.postalCode, a.phone, c.country,",
            "   c.countryId, a.createDate, a.createdBy, a.lastUpdate, a.lastUpdateBy",
            "FROM address a",
            "JOIN city b ON b.cityId = a.cityId",
            "JOIN country c ON c.countryId = b.countryId",
            "ORDER BY",
            sortColumn,
            direction
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AddressModel (
                    rs.getInt("addressId"), 
                    rs.getString("address"), 
                    rs.getString("address2"), 
                    rs.getString("city"),
                    rs.getInt("cityId"),
                    rs.getString("postalCode"), 
                    rs.getString("phone"),
                    rs.getString("country"),
                    rs.getInt("countryId"),
                    rs.getString("createDate"), 
                    rs.getString("createdBy"), 
                    rs.getString("lastUpdate"), 
                    rs.getString("lastUpdateBy")
                ));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }

    /**
     * Get list of Cities
     * @param sortColumn
     * @param direction
     * @return ObservableList (CityModel)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CityModel> getCityModelList(String sortColumn, String direction) throws SQLException {
        ObservableList<CityModel> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT a.cityId, a.city, b.country, a.createDate, a.createdBy, a.lastUpdate, a.lastUpdateBy",
            "FROM city a",
            "JOIN country b ON a.countryId = b.countryId",
            "ORDER BY",
            sortColumn,
            direction
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CityModel (
                    rs.getInt("cityId"), 
                    rs.getString("city"),  
                    rs.getString("country"), 
                    rs.getString("createDate"), 
                    rs.getString("createdBy"), 
                    rs.getString("lastUpdate"), 
                    rs.getString("lastUpdateBy")
                ));
            }
        }
        catch(SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }
    
    /**
     * Get list of Countries
     * @param sortColumn
     * @param direction
     * @return OberservableList (CountryModel)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CountryModel> getCountryModelList(String sortColumn, String direction) throws SQLException {
        ObservableList<CountryModel> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy",
            "FROM country",
            "ORDER BY",
            sortColumn,
            direction
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CountryModel (
                    rs.getInt("countryId"), 
                    rs.getString("country"), 
                    rs.getString("createDate"), 
                    rs.getString("createdBy"), 
                    rs.getString("lastUpdate"), 
                    rs.getString("lastUpdateBy")
                ));
            }
        }
        catch(SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }

    /**
     * Get Customer Data for maintenance
     * @param customerId
     * @return ResultSet
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public ResultSet getCustomerData(int customerId) throws SQLException {
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.customerName, a.active, b.address, b.address2, c.city, b.postalCode, b.phone, d.country",
            "FROM customer a",
            "JOIN address b on b.addressId = a.addressId",
            "JOIN city c ON c.cityId = b.cityId",
            "JOIN country d ON d.countryId = c.countryId",
            "WHERE a.customerId = " + customerId
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return rs;
    }
    
    /**
     * Get list of Customers
     * @param sortColumn
     * @param direction
     * @return OberservableList (CustomerModel)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CustomerModel> getCustomerModelList(String sortColumn, String direction) throws SQLException {
        ObservableList<CustomerModel> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy",
            "FROM customer",
            "ORDER BY",
            sortColumn,
            direction
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CustomerModel (
                    rs.getInt("customerId"), 
                    rs.getString("customerName"), 
                    rs.getInt("addressId"),
                    rs.getBoolean("active"), 
                    rs.getString("createDate"), 
                    rs.getString("createdBy"), 
                    rs.getString("lastUpdate"), 
                    rs.getString("lastUpdateBy")
                ));
            }
        }
        catch(SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }
    
    /**
     * Create map of Addresses to Address Id's
     * @return ListMap Address (String) to Address Id (Integer)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getAddressToAddressIdMap() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String address;
        String address1;
        String address2;
//        String city;
//        String country;
        String key;
//        String phone;
//        String postalCode;
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT addressId, address, address2",
            "FROM address",
            "ORDER BY address, address2"
//            "SELECT a.addressId, a.address, a.address2, b.city, c.country, a.postalCode, a.phone, c.country",
//            "FROM address a",
//            "JOIN city b ON b.cityId = a.cityId",
//            "JOIN country c ON b.countryId = c.countryId",
//            "ORDER BY a.address, a.address2, c.country, b.city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                address1 = rs.getString("address").trim();
                address2 = rs.getString("address2").trim();
//                city = rs.getString("city").trim();
//                country = rs.getString("country").trim();
//                phone = rs.getString("phone").trim();
//                postalCode = rs.getString("postalCode").trim();
                key  = (address1 != null && !address1.isEmpty()) ? address1 : "";
                key += (address2 != null && !address2.isEmpty()) ? " " + address2 : "";
//                key += (!key.isEmpty()) ? " | " + city : city;
//                key += (!key.isEmpty()) ? " | " + country : country;
//                key += (!key.isEmpty()) ? " | " + postalCode : postalCode;
//                key += (!key.isEmpty()) ? " | " + phone : phone;
                map.put(key, rs.getInt("addressId"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Create map of Address Id's to Addresses
     * @return ListMap Address Id (Integer) to Address (String)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getAddressIdToAddressMap() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        String address1;
        String address2;
//        String city;
//        String country;
//        String phone;
//        String postalCode;
        String sql;
        String value;
        connect();
        
        sql = String.join(" ",
            "SELECT addressId, address, address2",
            "FROM address",
            "ORDER BY addressId"
//            "SELECT a.addressId, a.address, a.address2, b.city, c.country, a.postalCode, a.phone, c.country",
//            "FROM address a",
//            "JOIN city b ON b.cityId = a.cityId",
//            "JOIN country c ON b.countryId = c.countryId",
//            "ORDER BY a.addressId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                address1 = rs.getString("address").trim();
                address2 = rs.getString("address2").trim();
//                city = rs.getString("city").trim();
//                country = rs.getString("country").trim();
//                phone = rs.getString("phone").trim();
//                postalCode = rs.getString("postalCode").trim();
                value  = (address1 != null && !address1.isEmpty()) ? address1 : "";
                value += (address2 != null && !address2.isEmpty()) ? " " + address2 : "";
//                value += (!value.isEmpty()) ? " | " + city : city;
//                value += (!value.isEmpty()) ? " | " + country : country;
//                value += (!value.isEmpty()) ? " | " + postalCode : postalCode;
//                value += (!value.isEmpty()) ? " | " + phone : phone;
                map.put(rs.getInt("addressId"), value);
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Create map of Cities to City Id's
     * @return ListMap City (String) to City Id (Integer)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCityToCityIdMap() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT cityId, city",
            "FROM city",
            "ORDER BY city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getString("city"), rs.getInt("cityId"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Create map of City Id's to Cities
     * @return ListMap City Id (Integer) to City (String)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCityIdToCityMap() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT cityId, city",
            "FROM city",
            "ORDER BY cityId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getInt("cityId"), rs.getString("city"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Create map of Countries to Country Id's
     * @return ListMap Country (String) to Country Id (Integer)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCountryToCountryIdMap() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT countryId, country",
            "FROM country",
            "ORDER BY country"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getString("country"), rs.getInt("countryId"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Create map of Country Id's to Countries
     * @return ListMap Country Id (Integer) to Country (String)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCountryIdToCountryMap() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT countryId, country",
            "FROM country",
            "ORDER BY countryId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getInt("countryId"), rs.getString("country"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }

    /**
     * Create map of Customers to Customer Id's
     * @return ListMap Customer Name (String) to Customer Id (Integer)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCustomerToCustomerIdMap() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT customerId, customerName",
            "FROM customer",
            "ORDER BY customerName"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            map.put("----  Add New Customer  ----", 0);
            
            while (rs.next()) {
                map.put(rs.getString("customerName"), rs.getInt("customerId"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }

    /**
     * Create map of Customer Id's to Customers
     * @return ListMap Customer Id (Integer) to Customer Name (String)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCustomerIdToCustomerMap() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT customerId, customerName",
            "FROM customer",
            "ORDER BY customerId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            map.put(0, "----  Add New Customer  ----");
            
            while (rs.next()) {
                map.put(rs.getInt("customerId"), rs.getString("customerName"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return map;
    }
    
    /**
     * Get a City Id using a City
     * @param city
     * @return cityId (Integer)
     * @throws SQLException
     */    
    @SuppressWarnings("unchecked")
    public int getACityId(String city) throws SQLException {
        int cityId = 0;
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT cityId",
            "FROM city",
            "WHERE city = \"" + escapeTicks(city) + "\""
        );
            
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
            cityId = rs.getInt("cityId");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return cityId;
    }

    /**
     * Get a City using a City Id
     * @param cityId
     * @return City (String)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public String getACityName(int cityId) throws SQLException {
        String city = "";
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT city",
            "FROM city",
            "WHERE cityId = " + cityId,
            "ORDER BY city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
            city = rs.getString("city");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return city;
    }

    /**
     * Get a Country Id using a Country
     * @param country
     * @return countryId (Integer)
     * @throws SQLException
     */    
    @SuppressWarnings("unchecked")
    public int getACountryId(String country) throws SQLException {
        int countryId = 0;
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT countryId",
            "FROM country",
            "WHERE country = \"" + escapeTicks(country) + "\"" 
        );
            
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
            countryId = rs.getInt("countryId");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return countryId;
    }

    /**
     * Get a Country using a Country Id
     * @param countryId
     * @return Country (String)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public String getACountryName(int countryId) throws SQLException {
        String country = "";
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT country",
            "FROM country",
            "WHERE countryId=" + countryId
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.first();
            country = rs.getString("country");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return country;
    }
    
    /**
     * Get a list of Cities
     * @return List City (String)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List getCityNameList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT city",
            "FROM city",
            "ORDER BY city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("city"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }

    /**
     * Get a Country id using a City Name
     * @param city
     * @return CountryId (Integer)
     * @throws java.sql.SQLException 
     */
    @SuppressWarnings("unchecked")
    public int getCountryIdViaCity(String city) throws SQLException {
        int countryId = 0;
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT countryId",
                "FROM city",
                "WHERE city = \"" + escapeTicks(city) + "\""
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            countryId = rs.getInt("countryId");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return countryId;
    }
    
    
    /**
     * Get a Country Name using a City Name
     * @param city
     * @return Country (String)
     * @throws java.sql.SQLException 
     */
    @SuppressWarnings("unchecked")
    public String getCountryNameViaCity(String city) throws SQLException {
        String country = "";
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT b.country",
                "FROM city a",
                "JOIN country b ON b.countryId = a.countryId",
                "WHERE a.city = \"" + escapeTicks(city) + "\""
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            country = rs.getString("country");
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return country;
    }
    
    /**
     * Get a list of Countries
     * @return List Country (String)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List getCountryNameList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT country",
            "FROM country",
            "ORDER BY country"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("country"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }

    /**
     * Get a list of Customers
     * @return List Customer Name (String)
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List getCustomerNameList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT customerName",
            "FROM customer",
            "ORDER BY customerName"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("customerName"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }

    /**
     * Get a list of concatenated Addresses
     * @return List Concatenated Address (String)
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public List getFullAddressList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.address, a.address2, b.city, c.country, a.postalCode, a.phone",
            "FROM address a",
            "JOIN city b ON a.cityId = b.cityId",
            "JOIN country c ON b.countryId = c.countryId",
            "ORDER BY a.address, a.address2, b.city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("address") + " " 
                       + rs.getString("address2") + " " 
                       + rs.getString("city") + " " 
                       + rs.getString("country") + " " 
                       + rs.getString("postalCode") + " " 
                       + rs.getString("phone"));
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        return list;
    }
    
    /**
     * Execute query without result set
     * @param sql
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    protected void run(String sql) throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
        stmt.execute(sql);
    }

    /**
     * Update address table
     * @param list
     * @param userName
     * @param rightNow
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public int upsertAddress(ObservableList<AddressModel> list, String userName, String rightNow) throws SQLException{
        int cnt;
        int id;
        int rows = 0;
        String sql;
        connect();
        
        try {
            for (AddressModel a : list) {
                sql = String.join(" ",
                    "SELECT COUNT(*) AS cnt",
                    "FROM address",
                    "WHERE addressId = " + a.getAddressId()
                );
                rs = stmt.executeQuery(sql);
                rs.first();
                cnt = rs.getInt("cnt");
                
                if (cnt > 0) {  // update record
                    sql = String.join(" ",
                        "UPDATE address",
                        "SET address = \"" + a.getAddress().trim() + "\",",
                        "   address2 = \"" + a.getAddress2().trim() + "\",",
                        "   cityId = " + a.getCityId() + ",",
                        "   postalCode = \"" + a.getPostalCode().trim() + "\",",
                        "   phone = '" + a.getPhone().trim() + "',",
                        "   lastUpdate = \"" + rightNow.trim() + "\",",
                        "   lastUpdateBy  = \"" + userName.trim() + "\"",
                        "WHERE addressId = " + a.getAddressId()
                    );
                    rows += stmt.executeUpdate(sql);
                }
                else {  // insert new record
                    sql = String.join(" ",
                        "INSERT",
                        "INTO  address (addressId, address, address2, cityId, postalCode, phone,",
                        "   createDate, createdBy, lastUpdate, lastUpdateBy)",
                        "VALUES(" + 
                            a.getAddressId() + ",\"",
                            a.getAddress().trim() + "\",\"",
                            a.getAddress2().trim() + "\",\"",
                            a.getCityId() + "\",\"",
                            a.getPostalCode().trim() + "\",\"",
                            a.getPhone().trim() + "\",\"",
                            rightNow.trim() + "\",\"",
                            userName.trim() + "\",\"",
                            rightNow.trim() + "\",\"",
                            userName.trim() + "\")"
                    );
                    rows += stmt.executeUpdate(sql);
                }
            }
            return rows;
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
    }

    /**
     * Update city table
     * @param list
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public boolean updateCityTable(ObservableList<CityModel> list) throws SQLException{
        int countryId;
        String sql;
        String lookup;
        
        try {
            sql = "TRUNCATE city";
            run(sql);
            
            sql = String.join(" ", 
                "INSERT",
                "INTO city (cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            pstmt = conn.prepareStatement(sql);
            
            for (CityModel c : list) {
                lookup = String.join(" ",
                    "SELECT countryId",
                    "FROM country",
                    "WHERE country = \"" + c.getCountry() + "\""
                );
                rs = stmt.executeQuery(lookup);
                rs.beforeFirst();
                rs.next();
                countryId = rs.getInt("countryId");
                pstmt.setInt(1, c.getCityId());
                pstmt.setString(2, escapeTicks(c.getCity().trim()));
                pstmt.setInt(3, countryId);
                pstmt.setString(4, c.getCreateDate().trim());
                pstmt.setString(5, c.getCreatedBy().trim());
                pstmt.setString(6, c.getLastUpdate().trim());
                pstmt.setString(7, c.getLastUpdateBy().trim());
                pstmt.executeUpdate();
            }
            return true;
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
    }
    
    /**
     * Update country table
     * @param list
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public boolean updateCountryTable(ObservableList<CountryModel> list) throws SQLException{
        String sql;
        connect();
        
        try {
            sql = "TRUNCATE country";
            run(sql);
            
            sql = String.join(" ", 
                "INSERT",
                "INTO country (countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            
            for (CountryModel c : list) {
                pstmt.setInt(1, c.getCountryId());
                pstmt.setString(2, escapeTicks(c.getCountry().trim()));
                pstmt.setString(3, c.getCreateDate().trim());
                pstmt.setString(4, c.getCreatedBy().trim());
                pstmt.setString(5, c.getLastUpdate().trim());
                pstmt.setString(6, c.getLastUpdateBy().trim());
                pstmt.executeUpdate();
            }
            return true;
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
    }

    /**
     * Insert / Update Customer table
     * @param record (CustomerModel)
     * @param userName (String)
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public int upsertCustomer(CustomerModel record, String userName) throws SQLException{
        int id;
        int rows;
        String sql;
        connect();
        
        try {
            sql = String.join(" ",
                "SELECT COUNT(*) AS cnt",
                "FROM customer",
                "WHERE customerId = " + record.getCustomerId()
            );
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            rs.first();
            id = rs.getInt("cnt");
            
            if (id > 0) {
                // update existing record
                sql = String.join(" ",
                    "UPDATE customer",
                    "SET customerName = \"" + escapeTicks(record.getCustomerName().trim()) + "\", ",
                    "   addressId = " + record.getAddressId() + ", ",
                    "   active = " + record.getActive() + ", ",
                    "   lastUpdate = NOW(),",
                    "   lastUpdateBy = \"" + userName.trim() + "\"",
                    "WHERE customerId = " + record.getCustomerId()
                );
                rows = stmt.executeUpdate(sql);
            }
            else {
                // insert new record
                sql = String.join(" ",
                    "SELECT MAX(id) AS id",
                    "FROM customer"
                );
                rs = stmt.executeQuery(sql);
                rs.first();
                id = rs.getInt("id");
                id++;
                
                sql = String.join(" ", 
                    "INSERT",
                    "INTO customer (customerId, customerName, addressId, active, createDate, createdBy lastUpdate, lastUpdateBy",
                    "VALUES (?, ?, ?, ?, NOW(), ?, NOW(), ?)"
                );
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, escapeTicks(record.getCustomerName().trim()));
                pstmt.setInt(3, record.getAddressId());
                pstmt.setBoolean(4, record.getActive());
                pstmt.setString(5, userName.trim());
                pstmt.setString(6, userName.trim());
                rows = pstmt.executeUpdate();
            }
            return rows;
        }
        catch (SQLException ex) {
            throw new SQLException(exception(ex));
        }
    }
}  

    
    
    
    
    
    
    
    

    
    
