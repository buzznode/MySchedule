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
        driver = "com.mysql.jdbc.Driver";
        db  = "U03MuY";
        dbUser = "U03MuY";
        dbPwd = "53688020218";
        url = "jdbc:mysql://52.206.157.109/" + db;
    }
    
    /**
     * Constructor taking log parameter
     * @param _log 
     */
    public DB(Logging _log) {
        log = _log;
        conn = null;
        driver = "com.mysql.jdbc.Driver";
        db  = "U03MuY";
        dbUser = "U03MuY";
        dbPwd = "53688020218";
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
        }
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "SELECT a.addressId, a.address, a.address2, b.city, b.cityId, a.postalCode,",
            "   a.phone, c.country, c.countryId, a.createDate, a.createdBy, a.lastUpdate,",
            "   a.lastUpdateBy",
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
                list.add(new AddressModel(
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "SELECT a.cityId, a.city, b.country, a.createDate, a.createdBy, a.lastUpdate, ",
            "   a.lastUpdateBy",
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
                list.add(new CityModel(
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                list.add(new CountryModel(
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "JOIN address b ON b.addressId = a.addressId",
            "JOIN city c ON c.cityId = b.cityId",
            "JOIN country d ON d.countryId = c.countryId",
            "WHERE a.customerId = " + customerId
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            rs.next();
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                list.add(new CustomerModel(
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
        String a1;
        String a2;
        String key;
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT addressId, address, address2",
            "FROM address",
            "ORDER BY address, address2"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                a1 = rs.getString("address");
                a2 = rs.getString("address2");
                key  = (a1 != null && !a1.isEmpty()) ? a1 : "";
                key += (a2 != null && !a2.isEmpty()) ? " " + a2 : "";
                map.put(key, rs.getInt("addressId"));
            }
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
        String a1;
        String a2;
        String sql;
        String value;
        connect();
        
        sql = String.join(" ",
            "SELECT addressId, address, address2",
            "FROM address",
            "ORDER BY addressId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                a1 = rs.getString("address");
                a2 = rs.getString("address2");
                value  = (a1 != null && !a1.isEmpty()) ? a1 : "";
                value += (a2 != null && !a2.isEmpty()) ? " " + a2 : "";
                map.put(rs.getInt("addressId"), value);
            }
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "WHERE city = '?'"
        );
            
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, city);
            rs = pstmt.executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            cityId = rs.getInt("cityId");
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "WHERE cityId = ?",
            "ORDER BY city"
        );
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cityId);
            rs = pstmt.executeQuery(sql);
            rs.beforeFirst();
            rs.next();
            city = rs.getString("city");
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "WHERE country=?"
        );
            
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, country);
            rs = pstmt.executeQuery(sql);
            rs.first();
            countryId = rs.getInt("countryId");
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            "WHERE countryId=?",
            "ORDER BY country"
        );
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, countryId);
            rs = pstmt.executeQuery(sql);
            rs.first();
            country = rs.getString("country");
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
        }
        return list;
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                list.add(rs.getString("address") 
                       + " " 
                       + rs.getString("address2") 
                       + " " 
                       + rs.getString("city") 
                       + " " 
                       + rs.getString("country") 
                       + " " 
                       + rs.getString("postalCode") 
                       + " " 
                       + rs.getString("phone"));
            }
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
        }
        stmt.execute(sql);
    }

    /**
     * Update address table
     * @param list
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public int upsertAddress(ObservableList<AddressModel> list, String userName) throws SQLException{
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
                        "SET address=?,",
                        "   address2=?,",
                        "   cityId=?,",
                        "   postalCode=?,",
                        "   phone=?,",
                        "   lastUpdate=NOW(),",
                        "   lastUpdateBy=?",
                        "WHERE addressId=?"
                    );
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, a.getAddress());
                    pstmt.setString(2, a.getAddress2());
                    pstmt.setInt(3, a.getCityId());
                    pstmt.setString(4, a.getPostalCode());
                    pstmt.setString(5, a.getPhone());
                    pstmt.setString(6, userName);
                    pstmt.setInt(7, a.getAddressId());
                    rows += pstmt.executeUpdate();
                }
                else {  // insert new record
                    sql = String.join(" ",
                        "INSERT",
                        "INTO address (addressId, address, address2, cityId,",
                        "   postalCode, phone, createDate, createdBy, lastUpdate,",
                        "   lastUpdateBy)",
                        "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)"
                    );
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, a.getAddressId());
                    pstmt.setString(2, a.getAddress());
                    pstmt.setString(3, a.getAddress2());
                    pstmt.setInt(4, a.getCityId());
                    pstmt.setString(5, a.getPostalCode());
                    pstmt.setString(6, a.getPhone());
                    pstmt.setString(7, userName);
                    pstmt.setString(8, userName);
                    rows += pstmt.executeUpdate(sql);
                    
                }
            }
            return rows;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                
                pstmt.setInt(1,    c.getCityId());
                pstmt.setString(2, c.getCity());
                pstmt.setInt(3,    countryId);
                pstmt.setString(4, c.getCreateDate());
                pstmt.setString(5, c.getCreatedBy());
                pstmt.setString(6, c.getLastUpdate());
                pstmt.setString(7, c.getLastUpdateBy());
                pstmt.executeUpdate();
            }
            return true;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                pstmt.setInt(1,    c.getCountryId());
                pstmt.setString(2, c.getCountry());
                pstmt.setString(3, c.getCreateDate());
                pstmt.setString(4, c.getCreatedBy());
                pstmt.setString(5, c.getLastUpdate());
                pstmt.setString(6, c.getLastUpdateBy());
                pstmt.executeUpdate();
            }
            return true;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
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
                    "SET customerName=?,",
                    "   addressId=?,",
                    "   active=?,",
                    "   lastUpdate=NOW(),",
                    "   lastUpdateBy=?",
                    "WHERE customerId=?"
                );
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, record.getCustomerName());
                pstmt.setInt(2, record.getAddressId());
                pstmt.setBoolean(3, record.getActive());
                pstmt.setString(4, userName);
                pstmt.setInt(5, record.getCustomerId());
                rows = pstmt.executeUpdate();
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
                    "INTO customer (customerId, customerName, addressId, active, createDate, createdBy",
                    "   lastUpdate, lastUpdateBy",
                    "VALUES (?, ?, ?, ?, NOW(), ?, NOW(), ?)"
                );
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, record.getCustomerName());
                pstmt.setInt(3, record.getAddressId());
                pstmt.setBoolean(4, record.getActive());
                pstmt.setString(5, userName);
                pstmt.setString(6, userName);
                rows = pstmt.executeUpdate();
            }
            return rows;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, ex.toString(), ex);
            log.write(Level.SEVERE, "SQLException: {0}", ex.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", ex.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", ex.getErrorCode());
            String msg = ex.getMessage() + " : " + ex.getSQLState() + " : " + ex.getErrorCode();
            throw new SQLException(msg);
        }
    }
}  

    
    
    
    
    
    
    
    

    
    
