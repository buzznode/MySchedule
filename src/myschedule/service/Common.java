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

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Common  {
    
    public HashMap<String, String> USERS = new HashMap<>();
    public Map<String, Integer> MENUS = new HashMap<>();
    public Map<String, Integer> MENUITEMS = new HashMap<>();
    
    public static String currentUser;
    public static int currentUserId;
    public static String currentLangCode;
    public static String currentLangName;
    
    private final Logging log;
    
    /**
     * Common constructor with Logging param
     * @param _log 
     */
    public Common(Logging _log) {
        log = _log;
    }

    /**
     * Alert status
     * @param status 
     */
    @SuppressWarnings("unchecked")
    public void alertStatus(int status) {
        if (status == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Database commit was successful. Record(s) added.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error processing request.");
            alert.setContentText("There was an error processing your request. Please try again.");
            alert.showAndWait();
        }
    }
    
    /**
     * Convert Map (Integer, String) to list
     * @param map
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertISMapToList(Map<Integer, String> map) {
        List list = new ArrayList(map.keySet());
        Collections.sort(list);
        return list;
    }
    
    /**
     * Convert Map (String, Integer) to list
     * @param map
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertSIMapToList(Map<String, Integer> map) {
        List list = new ArrayList(map.keySet());
        Collections.sort(list);
        return list;
    }
    
    /**
     * Convert Map (String, String) to list
     * @param map
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertSSMapToList(Map<String, String> map) {
        List list = new ArrayList(map.keySet());
        Collections.sort(list);
        return list;
    }
    
    /**
     * Display Confirmation Dialog using passed header and message
     * and wait for user's response then return result
     * @param hdr
     * @param msg
     * @return Boolean
     */
    @SuppressWarnings("unchecked")
    public boolean displayConfirmation(String hdr, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(hdr);
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    /**
     * Load users from database
     */
    @SuppressWarnings("unchecked")
    public void loadUsers() {
        if (USERS.size() > 0) {
            return;
        }
        
        try {
            DB db = new DB(log);
            db.connect();
            String sql = "select userName, password from user";
            ResultSet results =  db.exec( sql );
            int recordCount = 0;
           
            while (results.next()) {
                String user = results.getString("userName");
                String pwd = results.getString("password");
                USERS.put(user, pwd);
                recordCount++;
            }
        }
        catch (SQLException e)  {
            log.write(Level.SEVERE, e.toString());
            log.write(Level.SEVERE, "SQLException: {0}", e.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", e.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
        }
    }

    @SuppressWarnings("unchecked")
    public String rightNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
    
    
//    public static void setCursor(boolean busy) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (busy) {
////                        Main.getRoot().sceneProperty().get().setCursor(Cursor.WAIT);
//                    }
//                    else {
////                        Main.getRoot().sceneProperty().get().setCursor(Cursor.DEFAULT);
//                    }
//                }
//                catch (Exception e) {
//                }
//            }
//        });
//    }

    @SuppressWarnings("unchecked")
    public boolean validateUser(String user, String password) {
        if (!USERS.containsKey(user)) {
            return false;
        }
        else {
            return USERS.get(user).equals(password);
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean isNumber(String str) throws NumberFormatException {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException ex) {
            throw new NumberFormatException("Error parsing " + str + "; " + ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean isNumber(String str, boolean zeroOK) {
        try {
            int result = Integer.parseInt(str);
            return result == 0 && zeroOK;
        }
        catch (NumberFormatException ex) {
            throw new NumberFormatException("Error parsing " + str + "; " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean isString(String str) {
        if (str.getClass().getTypeName().equals(Type.STRING)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean isString(String str, boolean emptyNullOK) {
        if (str.length() > 0) {
            return true;
        }
        else {
            return !str.isEmpty();
        }
    }
}
