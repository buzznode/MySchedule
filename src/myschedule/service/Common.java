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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
     * Converts a list map (Integer, String) to list
     * @param listMap
     * @param list
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertISArrayMapToList(List<Map<Integer, String>> listMap, List list) {
        list.clear();
        listMap.forEach((entry) -> {
            entry.keySet().stream().map((key) -> {
                String value = entry.get(key);
                return key;
            }).forEachOrdered((key) -> {
                list.add(key);
            });
        });
        return list;
    }
    
    /**
     * Converts a list map (String, Integer) to list
     * @param listMap
     * @param list
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertSIArrayMapToList(List<Map<String, Integer>> listMap, List list) {
        list.clear();
        listMap.forEach((entry) -> {
            entry.keySet().stream().map((key) -> {
                Integer value = entry.get(key);
                return key;
            }).forEachOrdered((key) -> {
                list.add(key);
            });
        });
        return list;
    }
    
    /**
     * Converts a list map (String, String) to list
     * @param listMap
     * @param list
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List convertSSArrayMapToList(List<Map<String, String>> listMap, List list) {
        list.clear();
        listMap.forEach((entry) -> {
            entry.keySet().stream().map((key) -> {
                String value = entry.get(key);
                return key;
            }).forEachOrdered((key) -> {
                list.add(key);
            });
        });
        return list;
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
    public String now() {
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
        return str.length() > 0;
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
