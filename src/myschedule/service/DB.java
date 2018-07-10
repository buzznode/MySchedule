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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import myschedule.model.AddressModel;
import myschedule.model.AppointmentModel;
import myschedule.model.AppointmentTypeCountModel;
import myschedule.model.CityModel;
import myschedule.model.ConsultantScheduleModel;
import myschedule.model.CountryModel;
import myschedule.model.CustomerModel;
import myschedule.model.TotalAppointmentsModel;
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
    @SuppressWarnings("unchecked")
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
     */
    @SuppressWarnings("unchecked")
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
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.connect: " + ex.getMessage());
                }
            }));
        }
    }

    /**
     * Delete Customer
     * @param customerId 
     */
    @SuppressWarnings("unchecked")
    public void deleteCustomer(int customerId) {
        String sql;
        
        try {
            sql = "DELETE FROM customer WHERE customerId =" + customerId;
            assert exec(sql) : "There was an error deleting the customer with customerId: " + customerId;
        }
        catch (AssertionError ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.deleteCustomer: " + ex.getMessage());
                }
            }));
        }
    }
    
    /**
     * Replace tick-marks with escaped flavor
     * @param str (String)
     * @return String containing escaped tick-marks
     */
    @SuppressWarnings("unchecked")
    private String escapeTicks(String str) {
        return str.replaceAll("'", "\'");
    }

    /**
     * Creates an Exception Message
     * @param ex (SQLException)
     * @return Exception message
     */
    @SuppressWarnings("unchecked")
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
     */
    @SuppressWarnings("unchecked")
    protected boolean exec(String sql) {
        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
            stmt.executeQuery(sql);
            return true;
        }
        catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Execute SQL and return result
     * @param sql
     * @return Query ResultSet
     */
    @SuppressWarnings("unchecked")
    protected ResultSet execWithResultSet(String sql) {
        ResultSet rset = null;

        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
            rset =  stmt.executeQuery(sql);
        }
        catch (SQLException ex) {
            String msg = "There was an error executing a SQL statement.\n\n";
            msg += "SQL Statement: " + sql + "\n\n";
            msg += "SQLState: " + ex.getSQLState();
            msg += "ErrorCode: " + ex.getErrorCode();
            msg += "Message: " + ex.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("SQL Error");
            alert.setContentText(msg);
            alert.showAndWait().ifPresent((new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType response) {
                    if (response == ButtonType.OK) {
                        log.write(Level.SEVERE, "DB.deleteCustomer: " + ex.getMessage());
                    }
                }
            }));
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
     */
    @SuppressWarnings("unchecked")
    public ResultSet getAddressData(int addressId) {
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT a.address, a.address2, b.cityId, b.city, a.postalCode, a.phone, c.countryId, c.country",
                "FROM address a",
                "JOIN city b ON b.cityId = a.cityId",
                "JOIN country c ON c.countryId = b.countryId",
                "WHERE a.addressId = " + addressId
            );
            rs = stmt.executeQuery(sql);
            rs.first();
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAddressData: " + ex.getMessage());
                }
            }));
        }
        return rs;
    }
    
    /**
     * Get list of Addresses
     * @param sortColumn
     * @param direction
     * @return ObservableList (AddressModel)
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AddressModel> getAddressModelList(String sortColumn, String direction)  {
        ObservableList<AddressModel> list = FXCollections.observableArrayList();
        String sql;
        
        try {
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
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AddressModel (
                    rs.getInt("addressId"), 
                    rs.getString("address").trim(), 
                    rs.getString("address2").trim(), 
                    rs.getString("city").trim(),
                    rs.getInt("cityId"),
                    rs.getString("postalCode").trim(), 
                    rs.getString("phone").trim(),
                    rs.getString("country").trim(),
                    rs.getInt("countryId"),
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAddressModelList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get Appointments for a given month / year
     * @param appointmentId
     * @param localTZ
     * @return AppointmentModel
     */
    @SuppressWarnings("unchecked")
    public AppointmentModel getAppointment(int appointmentId, String localTZ) {
        AppointmentModel appt = new AppointmentModel();
        int cnt;
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT a.appointmentId, a.customerId, b.customerName, a.title, a.description, a.location, a.contact, a.url,",
                "CONVERT_TZ(a.start, ?, ?) AS startOffset, CONVERT_TZ(a.end, ?, ?) AS endOffset, a.createDate, a.createdBy,",
                "a.lastUpdate, a.lastUpdateBy",
                "FROM appointment a",
                "JOIN customer b ON b.customerId = a.customerId",
                "WHERE a.appointmentId = ?"
            );

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "+00:00");
            pstmt.setString(2, localTZ);
            pstmt.setString(3, "+00:00");
            pstmt.setString(4, localTZ);
            pstmt.setInt(5, appointmentId);
        
            rs = pstmt.executeQuery();
            rs.first();
            appt.setAppointmentId(rs.getInt("appointmentId"));
            appt.setCustomerId(rs.getInt("customerId"));
            appt.setCustomerName(rs.getString("customerName").trim());
            appt.setTitle(rs.getString("title").trim());
            appt.setDescription(rs.getString("description").trim());
            appt.setLocation(rs.getString("location").trim());
            appt.setContact(rs.getString("contact").trim());
            appt.setUrl(rs.getString("url").trim()); 
            appt.setStart(rs.getString("startOffset").trim());
            appt.setEnd(rs.getString("endOffset").trim());
            appt.setCreateDate(rs.getString("createDate").trim()); 
            appt.setCreatedBy(rs.getString("createdBy").trim()); 
            appt.setLastUpdate(rs.getString("lastUpdate").trim());
            appt.setLastUpdateBy(rs.getString("lastUpdateBy").trim());
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAppointment: " + ex.getMessage());
                }
            }));
        }
        return appt;
    }
    
    /**
     * Get Appointments for a given month / year
     * @param mm
     * @param yyyy
     * @param localTZ
     * @return List of AppointmentModel
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AppointmentModel> getAppointmentsByMonth(String mm, String yyyy, String localTZ) {
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        int cnt;
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT a.appointmentId, a.customerId, b.customerName, a.title, a.description, a.location, a.contact, a.url,",
                "CONVERT_TZ(a.start, ?, ?) AS startOffset, CONVERT_TZ(a.end, ?, ?) AS endOffset, a.createDate, a.createdBy,",
                "a.lastUpdate, a.lastUpdateBy",
                "FROM appointment a",
                "JOIN customer b ON b.customerId = a.customerId",
                "WHERE (month(a.start) = ? AND year(a.start) = ?)",
                "OR (month(a.end) = ? AND year(a.end) = ?)",
                "ORDER BY a.start, a.end"
            );
        
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "+00:00");
            pstmt.setString(2, localTZ);
            pstmt.setString(3, "+00:00");
            pstmt.setString(4, localTZ);
            pstmt.setString(5, mm.trim());
            pstmt.setString(6, yyyy.trim());
            pstmt.setString(7, mm.trim());
            pstmt.setString(8, yyyy.trim());
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AppointmentModel (
                    rs.getInt("appointmentId"), 
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getString("title").trim(), 
                    rs.getString("description").trim(), 
                    rs.getString("location").trim(),
                    rs.getString("contact"),
                    rs.getString("url").trim(), 
                    rs.getString("startOffset").trim(),
                    rs.getString("endOffset").trim(),
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAppointmentsByMonth: " + ex.getMessage());
                }
            }));
        }
        return list;
    }
    
    /**
     * Get Appointments for a given week
     * @param startDate
     * @param endDate
     * @param LocalTZ
     * @return List AppointmentModel
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AppointmentModel> getAppointmentsByWeek(String startDate, String endDate, String localTZ) {
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        int cnt;
        String sql;
        
        try {
            connect();
            startDate += " 00:00:00";
            endDate += " 23:59:59";

            sql = String.join(" ",
                "SELECT a.appointmentId, a.customerId, b.customerName, a.title, a.description, a.location, a.contact, a.url,",
                "CONVERT_TZ(a.start, ?, ?) AS startOffset, CONVERT_TZ(a.end, ?, ?) AS endOffset, a.createDate, a.createdBy,",
                "a.lastUpdate, a.lastUpdateBy",
                "FROM appointment a",
                "JOIN customer b ON b.customerId = a.customerId",
                "WHERE CONVERT_TZ(a.start, ?, ?) BETWEEN ? AND ?",
                "OR CONVERT_TZ(a.end, ?, ?) BETWEEN ? AND ?",
                "ORDER BY a.start, a.end"
            );

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "+00:00");
            pstmt.setString(2, localTZ);
            pstmt.setString(3, "+00:00");
            pstmt.setString(4, localTZ);
            pstmt.setString(5, "+00:00");
            pstmt.setString(6, localTZ);
            pstmt.setString(7, startDate.trim());
            pstmt.setString(8, endDate.trim());
            pstmt.setString(9, "+00:00");
            pstmt.setString(10, localTZ);
            pstmt.setString(11, startDate.trim());
            pstmt.setString(12, endDate.trim());
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AppointmentModel (
                    rs.getInt("appointmentId"), 
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getString("title").trim(), 
                    rs.getString("description").trim(), 
                    rs.getString("location").trim(),
                    rs.getString("contact"),
                    rs.getString("url").trim(), 
                    rs.getString("startOffset").trim(),
                    rs.getString("endOffset").trim(),
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAppointmentsByWeek: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get Appointment-Type-Count report
     * @return List of AppointmentTypeCountModel
     */
    @SuppressWarnings("unchecked")
    public ObservableList<AppointmentTypeCountModel> getAppointmentsTypeCountReport() {
        ObservableList<AppointmentTypeCountModel> list = FXCollections.observableArrayList();
        int cnt;
        String sql;
        
        try {
            connect();
            sql = "SELECT description, COUNT(*) AS cnt, MONTH(start) AS month, MONTHNAME(STR_TO_DATE(MONTH(start), '%m')) AS monthName " +
                      "FROM appointment " +
                      "GROUP BY description, MONTH(start) " +
                      "HAVING cnt > 0 " +
                      "ORDER BY MONTH(start), description";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new AppointmentTypeCountModel (
                    rs.getString("description"), 
                    rs.getInt("cnt"),
                    rs.getInt("month"),
                    rs.getString("monthName").trim()
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAppointmentsTypeCountReport: " + ex.getMessage());
                }
            }));
        }
        return list;
    }
    
    /**
     * Get list of Cities
     * @param sortColumn
     * @param direction
     * @return ObservableList (CityModel)
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CityModel> getCityModelList(String sortColumn, String direction) {
        ObservableList<CityModel> list = FXCollections.observableArrayList();
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT a.cityId, a.city, b.country, a.createDate, a.createdBy, a.lastUpdate, a.lastUpdateBy",
                "FROM city a",
                "JOIN country b ON a.countryId = b.countryId",
                "ORDER BY",
                sortColumn,
                direction
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CityModel (
                    rs.getInt("cityId"), 
                    rs.getString("city").trim(),  
                    rs.getString("country").trim(), 
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch(SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCityModelList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get Consultant-Schedule report
     * @param localTZ
     * @return List of ConsultantScheduleModel
     */
    @SuppressWarnings("unchecked")
    public ObservableList<ConsultantScheduleModel> getConsultantScheduleReport(String localTZ) {
        ObservableList<ConsultantScheduleModel> list = FXCollections.observableArrayList();
        int cnt;
        String sql;
        
        try {
            connect();
            sql = "SELECT a.customerId, b.customerName, a.title, a.description, a.location, a.contact, CONVERT_TZ(a.start, ?, ?) AS startOffset, " +
                      "MONTH(a.start) AS month, MONTHNAME(STR_TO_DATE(MONTH(a.start), '%m')) as monthName, CONVERT_TZ(a.end, ?, ?) AS endOffset " +
                      "FROM appointment a " +
                      "JOIN customer b on b.customerId = a.customerId " +
                      "ORDER BY a.contact, MONTH(a.start), a.start, a.description";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "+00:00");
            pstmt.setString(2, localTZ);
            pstmt.setString(3, "+00:00");
            pstmt.setString(4, localTZ);
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new ConsultantScheduleModel (
                    rs.getInt("customerId"), 
                    rs.getString("customerName").trim(),
                    rs.getString("title").trim(),
                    rs.getString("description").trim(),
                    rs.getString("location").trim(),
                    rs.getString("contact").trim(),
                    rs.getString("startOffset").trim(),
                    rs.getInt("month"), 
                    rs.getString("monthName").trim(),
                    rs.getString("endOffset").trim()
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getConsultantScheduleReport: " + ex.getMessage());
                }
            }));
        }
        return list;
    }
    
    /**
     * Get list of Countries
     * @param sortColumn
     * @param direction
     * @return OberservableList (CountryModel)
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CountryModel> getCountryModelList(String sortColumn, String direction) {
        ObservableList<CountryModel> list = FXCollections.observableArrayList();
        String sql;

        try {
            connect();

            sql = String.join(" ",
                "SELECT countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy",
                "FROM country",
                "ORDER BY",
                sortColumn,
                direction
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CountryModel (
                    rs.getInt("countryId"), 
                    rs.getString("country").trim(), 
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch(SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCountryModelList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get Customer Data for maintenance
     * @param customerId
     * @return ResultSet
     */
    @SuppressWarnings("unchecked")
    public ResultSet getCustomerData(int customerId) {
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerName, active, addressId",
                "FROM customer",
                "WHERE customerId = " + customerId
            );
            rs = stmt.executeQuery(sql);
            rs.first();
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerData: " + ex.getMessage());
                }
            }));
        }
        return rs;
    }
    
    /**
     * Get list of Customers
     * @param sortColumn
     * @param direction
     * @return OberservableList (CustomerModel)
     */
    @SuppressWarnings("unchecked")
    public ObservableList<CustomerModel> getCustomerModelList(String sortColumn, String direction) {
        ObservableList<CustomerModel> list = FXCollections.observableArrayList();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy",
                "FROM customer",
                "ORDER BY",
                sortColumn,
                direction
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new CustomerModel (
                    rs.getInt("customerId"), 
                    rs.getString("customerName").trim(), 
                    rs.getInt("addressId"),
                    rs.getBoolean("active"), 
                    rs.getString("createDate").trim(), 
                    rs.getString("createdBy").trim(), 
                    rs.getString("lastUpdate").trim(), 
                    rs.getString("lastUpdateBy").trim()
                ));
            }
        }
        catch(SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerModelList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }
    
    /**
     * Create map of Addresses to Address Id's
     * @return ListMap Address (String) to Address Id (Integer)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getAddressToAddressIdMap() {
        Map<String, Integer> map = new HashMap<>();
        String address;
        String address1;
        String address2;
        String city;
        String country;
        String key;
        String phone;
        String postalCode;
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT a.addressId, a.address, a.address2, b.city, c.country, a.postalCode, a.phone, c.country",
                "FROM address a",
                "JOIN city b ON b.cityId = a.cityId",
                "JOIN country c ON b.countryId = c.countryId",
                "ORDER BY a.address, a.address2, c.country, b.city"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                address1 = rs.getString("address").trim();
                address2 = rs.getString("address2").trim();
                city = rs.getString("city").trim();
                country = rs.getString("country").trim();
                phone = rs.getString("phone").trim();
                postalCode = rs.getString("postalCode").trim();
                key  = (address1 != null && !address1.isEmpty()) ? address1 : "";
                key += (address2 != null && !address2.isEmpty()) ? " " + address2 : "";
                key += (!key.isEmpty()) ? " | " + city : city;
                key += (!key.isEmpty()) ? " | " + country : country;
                key += (!key.isEmpty()) ? " | " + postalCode : postalCode;
                key += (!key.isEmpty()) ? " | " + phone : phone;
                map.put(key, rs.getInt("addressId"));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAddressToAddressIdMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of Address Id's to Addresses
     * @return ListMap Address Id (Integer) to Address (String)
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getAddressIdToAddressMap() {
        Map<Integer, String> map = new HashMap<>();
        String address1;
        String address2;
        String city;
        String country;
        String phone;
        String postalCode;
        String sql;
        String value;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT a.addressId, a.address, a.address2, b.city, c.country, a.postalCode, a.phone, c.country",
                "FROM address a",
                "JOIN city b ON b.cityId = a.cityId",
                "JOIN country c ON b.countryId = c.countryId",
                "ORDER BY a.addressId"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                address1 = rs.getString("address").trim();
                address2 = rs.getString("address2").trim();
                city = rs.getString("city").trim();
                country = rs.getString("country").trim();
                phone = rs.getString("phone").trim();
                postalCode = rs.getString("postalCode").trim();
                value  = (address1 != null && !address1.isEmpty()) ? address1 : "";
                value += (address2 != null && !address2.isEmpty()) ? " " + address2 : "";
                value += (!value.isEmpty()) ? " | " + city : city;
                value += (!value.isEmpty()) ? " | " + country : country;
                value += (!value.isEmpty()) ? " | " + postalCode : postalCode;
                value += (!value.isEmpty()) ? " | " + phone : phone;
                map.put(rs.getInt("addressId"), value);
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getAddressIdToAddressMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of Cities to City Id's
     * @return ListMap City (String) to City Id (Integer)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCityToCityIdMap() {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT cityId, city",
                "FROM city",
                "ORDER BY city"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getString("city").trim(), rs.getInt("cityId"));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCityToCityIdMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of City Id's to Cities
     * @return ListMap City Id (Integer) to City (String)
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCityIdToCityMap() {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT cityId, city",
                "FROM city",
                "ORDER BY cityId"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getInt("cityId"), rs.getString("city").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCityIdToCityMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of Countries to Country Id's
     * @return ListMap Country (String) to Country Id (Integer)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCountryToCountryIdMap() {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT countryId, country",
                "FROM country",
                "ORDER BY country"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getString("country").trim(), rs.getInt("countryId"));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCountryToCountryIdMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of Country Id's to Countries
     * @return ListMap Country Id (Integer) to Country (String)
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCountryIdToCountryMap() {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT countryId, country",
                "FROM country",
                "ORDER BY countryId"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            while (rs.next()) {
                map.put(rs.getInt("countryId"), rs.getString("country").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCountryIdToCountryMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }

    /**
     * Create map of Customers to Customer Id's
     * @return ListMap Customer Name (String) to Customer Id (Integer)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCustomerToCustomerIdMap() {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerId, customerName",
                "FROM customer",
                "ORDER BY customerName"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            map.put("----  Add New Customer  ----", 0);
            
            while (rs.next()) {
                map.put(rs.getString("customerName").trim(), rs.getInt("customerId"));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerToCustomerIdMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }

    /**
     *  Create map of Customers to Customer Id's
     * @param addCustomer flag
     * @return Map Customer Name (String) to Customer Id (Integer)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> getCustomerToCustomerIdMap(boolean addCustomer) {
        Map<String, Integer> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerId, customerName",
                "FROM customer",
                "ORDER BY customerName"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            
            if (addCustomer) {
                map.put("----  Add New Customer  ----", 0);
            }
            
            while (rs.next()) {
                map.put(rs.getString("customerName").trim(), rs.getInt("customerId"));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerToCustomerIdMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Create map of Customer Id's to Customers
     * @return ListMap Customer Id (Integer) to Customer Name (String)
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getCustomerIdToCustomerMap() {
        Map<Integer, String> map = new HashMap<>();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerId, customerName",
                "FROM customer",
                "ORDER BY customerId"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            map.clear();
            map.put(0, "----  Add New Customer  ----");
            
            while (rs.next()) {
                map.put(rs.getInt("customerId"), rs.getString("customerName").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Deleting Customer");
            alert.setContentText(ex.getMessage());
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerIdToCustomerMap: " + ex.getMessage());
                }
            }));
        }
        return map;
    }
    
    /**
     * Get a City Id using a City
     * @param city
     * @return cityId (Integer)
     */    
    @SuppressWarnings("unchecked")
    public int getACityId(String city) {
        int cityId = 0;
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT cityId",
                "FROM city",
                "WHERE city = \"" + escapeTicks(city) + "\""
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            cityId = rs.getInt("cityId");
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getACity: " + ex.getMessage());
                }
            }));
        }
        return cityId;
    }

    /**
     * Get a City using a City Id
     * @param cityId
     * @return City (String)
     */
    @SuppressWarnings("unchecked")
    public String getACityName(int cityId) {
        String city = "";
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT city",
                "FROM city",
                "WHERE cityId = " + cityId,
                "ORDER BY city"
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            city = rs.getString("city").trim();
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getACityName: " + ex.getMessage());
                }
            }));
        }
        return city;
    }

    /**
     * Get a Country Id using a Country
     * @param country
     * @return countryId (Integer)
     */    
    @SuppressWarnings("unchecked")
    public int getACountryId(String country) {
        int countryId = 0;
        String sql;
            
        try {
            connect();
            sql = String.join(" ",
                "SELECT countryId",
                "FROM country",
                "WHERE country = \"" + escapeTicks(country) + "\"" 
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            countryId = rs.getInt("countryId");
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getACountryId: " + ex.getMessage());
                }
            }));
        }
        return countryId;
    }

    /**
     * Get a Country using a Country Id
     * @param countryId
     * @return Country (String)
     */
    @SuppressWarnings("unchecked")
    public String getACountryName(int countryId) {
        String country = "";
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT country",
                "FROM country",
                "WHERE countryId=" + countryId
            );
            rs = stmt.executeQuery(sql);
            rs.first();
            country = rs.getString("country").trim();
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getACountryName: " + ex.getMessage());
                }
            }));
        }
        return country;
    }
    
    /**
     * Get a list of Cities
     * @return List City (String)
     */
    @SuppressWarnings("unchecked")
    public List getCityNameList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT city",
                "FROM city",
                "ORDER BY city"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            list.add("");
            
            while (rs.next()) {
                list.add(rs.getString("city").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCityNameList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get a Country id using a City Name
     * @param city
     * @return CountryId (Integer)
     */
    @SuppressWarnings("unchecked")
    public int getCountryIdViaCity(String city) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCountryIdViaCity: " + ex.getMessage());
                }
            }));
        }
        return countryId;
    }
    
    
    /**
     * Get a Country Name using a City Name
     * @param city
     * @return Country (String)
     */
    @SuppressWarnings("unchecked")
    public String getCountryNameViaCity(String city) {
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
            country = rs.getString("country").trim();
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "Error in DB.getCountryNameViaCity routine: " + ex.getMessage());
                }
            }));
        }
        return country;
    }
    
    /**
     * Get a list of Countries
     * @return List Country (String)
     */
    @SuppressWarnings("unchecked")
    public List getCountryNameList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT country",
                "FROM country",
                "ORDER BY country"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("country").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCountryNameList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get a list of Customers
     * @return List Customer Name (String)
     */
    @SuppressWarnings("unchecked")
    public List getCustomerNameList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql;
        
        try {
            connect();
            sql = String.join(" ",
                "SELECT customerName",
                "FROM customer",
                "ORDER BY customerName"
            );
            rs = stmt.executeQuery(sql);
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(rs.getString("customerName").trim());
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getCustomerNameList: " + ex.getMessage());
                }
            }));
        }
        return list;
    }

    /**
     * Get Total-Appointments report
     * @return List of TotalAppointmentsModel
     */
    @SuppressWarnings("unchecked")
    public ObservableList<TotalAppointmentsModel> getTotalAppointmentsReport() {
        ObservableList<TotalAppointmentsModel> list = FXCollections.observableArrayList();
        int cnt;
        String sql;
        
        try {
            connect();
            sql = "SELECT contact, description, MONTH(start) AS month, MONTHNAME(STR_TO_DATE(MONTH(start), '%m')) AS monthName, COUNT(*) AS cnt " +
                      "FROM appointment " +
                      "GROUP BY MONTH(start), description, contact " +
                      "ORDER BY MONTH(start), description, contact ";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            
            while (rs.next()) {
                list.add(new TotalAppointmentsModel (
                    rs.getString("contact").trim(), 
                    rs.getString("description").trim(),
                    rs.getInt("month"),
                    rs.getString("monthName").trim(),
                    rs.getInt("cnt")
                ));
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.getTotalAppointmentsReport: " + ex.getMessage());
                }
            }));
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.run: " + ex.getMessage());
                }
            }));
        }
        stmt.execute(sql);
    }

    /**
     * Update address table
     * @param list
     * @param userName
     * @return boolean result
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public int upsertAddress(ObservableList<AddressModel> list, String userName) throws SQLException{
        int cnt;
        int id;
        int rows = 0;
        String sql;
        
        try {
            connect();
            
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
                        "   lastUpdate = \"" + LocalDateTime.now().toString() + "\",",
                        "   lastUpdateBy  = \"" + userName.trim() + "\"",
                        "WHERE addressId = " + a.getAddressId()
                    );
                    rows += stmt.executeUpdate(sql);
                }
                else {  // insert new record
                    sql = String.join(" ",
                        "INSERT",
                        "INTO  address (address, address2, cityId, postalCode, phone,",
                        "   createDate, createdBy, lastUpdate, lastUpdateBy)",
                        "VALUES(\"" + 
                            a.getAddress().trim() + "\",\"",
                            a.getAddress2().trim() + "\",\"",
                            a.getCityId() + "\",\"",
                            a.getPostalCode().trim() + "\",\"",
                            a.getPhone().trim() + "\",\"",
                            LocalDateTime.now().toString() + "\",\"",
                            userName.trim() + "\",\"",
                            LocalDateTime.now().toString() + "\",\"",
                            userName.trim() + "\")"
                    );
                    rows += stmt.executeUpdate(sql);
                }
            }
            return rows;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, "DB.upsertAddress: " + ex.getMessage());
            throw new SQLException(exception(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public int upsertAddress(AddressModel obj) throws SQLException{
        int cnt;
        int id;
        String sql;

        try {
            connect();
            sql = String.join(" ",
                "SELECT addressId",
                "FROM address",
                "WHERE cityId = " + obj.getCityId(),
                "AND postalCode = \"" + obj.getPostalCode() + "\"",
                "AND phone = \"" + obj.getPhone() + "\""
            );
            rs = stmt.executeQuery(sql);

            if (rs.first()) {
                return rs.getInt("addressId");
            }
            
            sql = String.join(" ",
                "INSERT",
                "INTO  address (address, address2, cityId, postalCode, phone,",
                "   createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, obj.getAddress().trim());
            pstmt.setString(2, obj.getAddress2().trim());
            pstmt.setInt(3, obj.getCityId());
            pstmt.setString(4, obj.getPostalCode().trim());
            pstmt.setString(5, obj.getPhone().trim());
            pstmt.setString(6, obj.getCreateDate().trim());
            pstmt.setString(7, obj.getCreatedBy().trim());
            pstmt.setString(8, obj.getLastUpdate().trim());
            pstmt.setString(9, obj.getLastUpdateBy().trim());
            pstmt.executeUpdate();
            return 1;
        }
        catch (SQLException ex) {
            log.write(Level.SEVERE, "DB.upsertAddress: " + ex.getMessage());
            throw new SQLException(exception(ex));
        }
    }
    
    /**
     * Insert Appointment record
     * @param appt
     * @param userName
     * @param localTZ
     * @throws Exception 
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public void upsertAppointment(AppointmentModel appt, String userName, String localTZ) throws Exception, SQLException {
        int cnt;
        int id;
        int rows = 0;
        String sql;
        
        try {
            connect();

            // Check to ensure this won't create an overlapping appointment
            sql = String.join(" ",
                "SELECT COUNT(*) AS cnt",
                "FROM appointment",
                "WHERE contact = ?",
                "AND ((CONVERT_TZ(?, ?, ?) BETWEEN start AND end)",
                "OR (CONVERT_TZ(?, ?, ?) BETWEEN start AND end))"
            );
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, appt.getStart());
            pstmt.setString(3, localTZ);
            pstmt.setString(4, "+00:00");
            pstmt.setString(5, appt.getEnd());
            pstmt.setString(6, localTZ);
            pstmt.setString(7, "+00:00");
            rs = pstmt.executeQuery();
            rs.first();
            cnt = rs.getInt("cnt");
        
            if (cnt > 0) {
                throw new Exception("Creating this appointment would create overlapping appointments for " + userName);
            }
        
            if (appt.getAppointmentId() > 0) {  // update record
                sql = String.join(" ",
                    "UPDATE appointment",
                    "SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, ur l= ?, start = CONVERT_TZ(?, ?, ?),",
                    "end = CONVERT_TZ(?, ?, ?), createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ?",
                    "WHERE appointmentId=? "
                );
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, appt.getCustomerId());
                pstmt.setString(2, appt.getTitle().trim());
                pstmt.setString(3, appt.getDescription().trim());
                pstmt.setString(4, appt.getLocation().trim());
                pstmt.setString(5, appt.getContact().trim());
                pstmt.setString(6, appt.getUrl().trim());
                pstmt.setString(7, appt.getStart().trim());
                pstmt.setString(8, localTZ);
                pstmt.setString(9, "+00:00");
                pstmt.setString(10, appt.getEnd().trim());
                pstmt.setString(11, localTZ);
                pstmt.setString(12, "+00:00");
                pstmt.setString(13, LocalDateTime.now().toString());
                pstmt.setString(14, userName.trim());
                pstmt.setString(15, LocalDateTime.now().toString());
                pstmt.setString(16, userName.trim());
                pstmt.setInt(17, appt.getAppointmentId());
                pstmt.executeUpdate();
            }
            else {  // insert new record
                sql = String.join(" ",
                    "INSERT",
                    "INTO appointment (customerId, title, description, location, contact, url, start, end, createDate,",
                    "   createdBy, lastUpdate, lastUpdateBy)",
                    "VALUES (?, ?, ?, ?, ?, ?, CONVERT_TZ(?, ?, ?), CONVERT_TZ(?, ?, ?), ?, ?, ?, ?)"
                );
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, appt.getCustomerId());
                pstmt.setString(2, appt.getTitle().trim());
                pstmt.setString(3, appt.getDescription().trim());
                pstmt.setString(4, appt.getLocation().trim());
                pstmt.setString(5, appt.getContact().trim());
                pstmt.setString(6, appt.getUrl().trim());
                pstmt.setString(7, appt.getStart().trim());
                pstmt.setString(8, localTZ);
                pstmt.setString(9, "+00:00");
                pstmt.setString(10, appt.getEnd().trim());
                pstmt.setString(11, localTZ);
                pstmt.setString(12, "+00:00");
                pstmt.setString(13, LocalDateTime.now().toString());
                pstmt.setString(14, userName.trim());
                pstmt.setString(15, LocalDateTime.now().toString());
                pstmt.setString(16, userName.trim());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.upsertAppointment: " + ex.getMessage());
                }
            }));
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
                "INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?)"
            );

            pstmt = conn.prepareStatement(sql);
            
            for (CityModel c : list) {
                lookup = String.join(" ",
                    "SELECT countryId",
                    "FROM country",
                    "WHERE country = \"" + c.getCountry() + "\""
                );
                rs = stmt.executeQuery(lookup);
                rs.first();
                countryId = rs.getInt("countryId");
                pstmt.setString(1, escapeTicks(c.getCity().trim()));
                pstmt.setInt(2, countryId);
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
                "INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?)"
            );
            
            pstmt = conn.prepareStatement(sql);
            
            for (CountryModel c : list) {
                pstmt.setString(1, escapeTicks(c.getCountry().trim()));
                pstmt.setString(2, c.getCreateDate().trim());
                pstmt.setString(3, c.getCreatedBy().trim());
                pstmt.setString(4, c.getLastUpdate().trim());
                pstmt.setString(5, c.getLastUpdateBy().trim());
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
     * @param customer (CustomerModel)
     * @param userName (String)
     */
    @SuppressWarnings("unchecked")
    public void upsertCustomer(CustomerModel customer, String userName) {
        int id;
        int rows;
        String sql;
        
        try {
        connect();
            sql = String.join(" ",
                "SELECT COUNT(*) AS cnt",
                "FROM customer",
                "WHERE customerId = " + customer.getCustomerId()
            );
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            rs.first();
            id = rs.getInt("cnt");
            
            if (id > 0) {
                // update existing record
                sql = String.join(" ",
                    "UPDATE customer",
                    "SET customerName = \"" + escapeTicks(customer.getCustomerName().trim()) + "\", ",
                    "   addressId = " + customer.getAddressId() + ", ",
                    "   active = " + customer.getActive() + ", ",
                    "   lastUpdate = NOW(),",
                    "   lastUpdateBy = \"" + userName.trim() + "\"",
                    "WHERE customerId = " + customer.getCustomerId()
                );
                stmt.executeUpdate(sql);
            }
            else {
                // insert new record
                sql = String.join(" ", 
                    "INSERT",
                    "INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)",
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, escapeTicks(customer.getCustomerName().trim()));
                pstmt.setInt(2, customer.getAddressId());
                pstmt.setBoolean(3, customer.getActive());
                pstmt.setString(4, LocalDateTime.now().toString());
                pstmt.setString(5, userName.trim());
                pstmt.setString(6, LocalDateTime.now().toString());
                pstmt.setString(7, userName.trim());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    log.write(Level.SEVERE, "DB.upsertCustomer: " + ex.getMessage());
                }
            }));
        }
    }
}  

    
    
    
    
    
    
    
    

    
    
