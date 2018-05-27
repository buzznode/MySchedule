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
import java.util.List;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import myschedule.model.AddressModel;
import myschedule.model.CityModel;
import myschedule.model.CountryModel;
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
     * @return ResultSet
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
     * Get list of addresses
     * @return AddressModel ObservableList
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AddressModel> getAddresses() throws SQLException {
        ObservableList<AddressModel> list = FXCollections.observableArrayList();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.addressId, a.address, a.address2, b.city, a.postalCode, a.phone, a.createDate,",
            "               a.createdBy, a.lastUpdate, a.lastUpdateBy",
            "  FROM address a",
            "  JOIN city b ON a.cityId = b.cityId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AddressModel(
                    rs.getInt("addressId"), rs.getString("address"), rs.getString("address2"), rs.getString("city"), 
                    rs.getString("postalCode"), rs.getString("phone"), rs.getString("createDate"), rs.getString("createdBy"), 
                    rs.getString("lastUpdate"), rs.getString("lastUpdateBy")
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
     * Get City list
     * @return CityModel OberservableList
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CityModel> getCities() throws SQLException {
        ObservableList<CityModel> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT a.cityId, a.city, b.country, a.createDate, a.createdBy, a.lastUpdate, ",
            "               a.lastUpdateBy",
            "  FROM city a",
            "  JOIN country b ON a.countryId = b.countryId"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CityModel(
                    rs.getInt("cityId"), rs.getString("city"),  rs.getString("country"), rs.getString("createDate"), 
                    rs.getString("createdBy"), rs.getString("lastUpdate"), rs.getString("lastUpdateBy")
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
     * @param city
     * @return cityId
     * @throws SQLException
     */    
    @SuppressWarnings("unchecked")
    public int getCityId(String city) throws SQLException {
        int cityId = 0;
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT cityId",
            "  FROM city",
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
     * @param cityId
     * @return city
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public String getCityName(int cityId) throws SQLException {
        String city = "";
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT city",
            "  FROM city",
            "WHERE cityId = ?"
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
     * @return City names list
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List getCityNames() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT city",
            "  FROM city",
            " ORDER BY city"
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
     * Get Country list
     * @return CountryModel OberservableList
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CountryModel> getCountries() throws SQLException {
        ObservableList<CountryModel> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT countryId, country, createDate, createdBy, lastUpdate,",
            "       lastUpdateBy",
            "  FROM country"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CountryModel(
                    rs.getInt("countryId"), rs.getString("country"),
                    rs.getString("createDate"), rs.getString("createdBy"),
                    rs.getString("lastUpdate"), rs.getString("lastUpdateBy")
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
     * @param country
     * @return countryId
     * @throws SQLException
     */    
    @SuppressWarnings("unchecked")
    public int getCountryId(String country) throws SQLException {
        int countryId = 0;
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT countryId",
            "  FROM country",
            "WHERE country = '?'"
        );
            
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, country);
            rs = pstmt.executeQuery(sql);
            rs.beforeFirst();
            rs.next();
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
     * @param countryId
     * @return country
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public String getCountryName(int countryId) throws SQLException {
        String country = "";
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT country",
            "  FROM country",
            "WHERE countryId = ?"
        );
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, countryId);
            rs = pstmt.executeQuery(sql);
            rs.beforeFirst();
            rs.next();
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
     * @return Country names list
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List getCountryNames() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();

        sql = String.join(" ",
            "SELECT country",
            "  FROM country",
            " ORDER BY country"
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
    
    @SuppressWarnings("unchecked")
    public List getCustomers() throws SQLException {
    }
    
    @SuppressWarnings("unchecked")
    public List getFullAddresses() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        connect();
        
        sql = String.join(" ",
            "SELECT a.address, a.address2, b.city, c.country, a.postalCode, a.phone",
            "  FROM address a",
            "  JOIN city b ON a.cityId = b.cityId",
            "  JOIN country c ON b.countryId = c.countryId",
            " ORDER BY a.address, a.address2, b.city"
        );
        
        try {
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("address") + " " + rs.getString("address2") + " " + rs.getString("city") + " " +
                    rs.getString("country") + " " + rs.getString("postalCode") + " " + rs.getString("phone"));
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
     * Update address
     * @param list
     * @return boolean
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public boolean updateAddresses(ObservableList<AddressModel> list) throws SQLException{
        int cityId;
        String sql;
        String lookup;
        
        try {
            sql = "TRUNCATE address";
            run(sql);
            
            sql = String.join(" ", 
                "INSERT ",
                "  INTO address (addressId, address, address2, cityId, postalCode, phone, createDate, createdBy,",
                "             lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            
            for (AddressModel a : list) {
                lookup = String.join(" ",
                    "SELECT cityId",
                    "  FROM city",
                    " WHERE city = \"" + a.getCity()+ "\""
                );
                rs = stmt.executeQuery(lookup);
                rs.beforeFirst();
                rs.next();
                cityId = rs.getInt("cityId");
                
                pstmt.setInt(1, a.getAddressId());
                pstmt.setString(2, a.getAddress());
                pstmt.setString(3, a.getAddress2());
                pstmt.setInt(4, cityId);
                pstmt.setString(5, a.getPostalCode());
                pstmt.setString(6, a.getPhone());
                pstmt.setString(7, a.getCreateDate());
                pstmt.setString(8, a.getCreatedBy());
                pstmt.setString(9, a.getLastUpdate());
                pstmt.setString(10, a.getLastUpdateBy());
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
     * Update city
     * @param list
     * @return boolean
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public boolean updateCities(ObservableList<CityModel> list) throws SQLException{
        int countryId;
        String sql;
        String lookup;
        
        try {
            sql = "TRUNCATE city";
            run(sql);
            
            sql = String.join(" ", 
                "INSERT ",
                "  INTO city (cityId, city, countryId, createDate, createdBy,",
                "             lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            
            for (CityModel c : list) {
                lookup = String.join(" ",
                    "SELECT countryId",
                    "  FROM country",
                    " WHERE country = \"" + c.getCountry() + "\""
                );
                rs = stmt.executeQuery(lookup);
                rs.beforeFirst();
                rs.next();
                countryId = rs.getInt("countryId");
                
                pstmt.setInt(1, c.getCityId());
                pstmt.setString(2, c.getCity());
                pstmt.setInt(3, countryId);
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
     * Update country
     * @param list
     * @return boolean
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public boolean updateCountries(ObservableList<CountryModel> list) throws SQLException{
        String sql;
        connect();
        
        try {
            sql = "TRUNCATE country";
            run(sql);
            
            sql = String.join(" ", 
                "INSERT ",
                "  INTO country (countryId, country, createDate, createdBy,",
                "                              lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?,?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            
            for (CountryModel c : list) {
                pstmt.setInt(1, c.getCountryId());
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
}  
