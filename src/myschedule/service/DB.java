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
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import myschedule.model.CountryModel;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * @author bradd
 * @version 0.5.0
 */
public class DB {

//    private static final Logger LOGGER = Logger.getLogger( DB.class.getName() );
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
    
    public DB() {

        conn = null;
        driver = "com.mysql.jdbc.Driver";
        db  = "U03MuY";
        dbUser = "U03MuY";
        dbPwd = "53688020218";
        url = "jdbc:mysql://52.206.157.109/" + db;

    }
    
    public DB(Logging _log) {

        log = _log;
        conn = null;
        driver = "com.mysql.jdbc.Driver";
        db  = "U03MuY";
        dbUser = "U03MuY";
        dbPwd = "53688020218";
        url = "jdbc:mysql://52.206.157.109/" + db;

    }
    
    protected void connect() {

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
        catch (SQLException e) {
            log.write(Level.SEVERE, e.toString(), e);
            log.write(Level.SEVERE, "SQLException: {0}", e.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", e.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
        }

    }

    protected ResultSet exec(String sql) throws SQLException {

        ResultSet rset = null;

        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
            rset =  stmt.executeQuery(sql);
        }
        catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, e.toString(), e);
//            LOGGER.log(Level.SEVERE, "SQLException: {0}", e.getMessage());
//            LOGGER.log(Level.SEVERE, "SQLState: {0}", e.getSQLState());
//            LOGGER.log(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
        }
        return rset;

    }

    @Override
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
    
    public ObservableList<CountryModel>  getCountries() {

        ObservableList<CountryModel> list = FXCollections.observableArrayList();
        connect();
        
        try {
            rs = stmt.executeQuery("SELECT countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy " +
                                                           "FROM country ");
           rs.beforeFirst();
           while (rs.next()) {
                boolean add = list.add(new CountryModel(
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
            
        }
        
        return list;

    }
    
    protected void run(String sql) throws SQLException {

        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
        }
        catch (SQLException e) {
//            LOGGER.log(Level.SEVERE, e.toString(), e);
//            LOGGER.log(Level.SEVERE, "SQLException: {0}", e.getMessage());
//            LOGGER.log(Level.SEVERE, "SQLState: {0}", e.getSQLState());
//            LOGGER.log(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
        }
        stmt.execute(sql);

    }
    
    public boolean updateCountries(ObservableList<CountryModel> list) throws SQLException{
        
        try {
            String sql = "TRUNCATE country";
            run(sql);
            
            sql = "INSERT INTO country (countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?,?, ?, ?)";
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
            throw new SQLException(ex.getMessage());
        }
    }
}  
