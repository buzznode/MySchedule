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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javafx.application.Platform;
import static myschedule.service.Logging.LOGGER;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Common  {
//    private final static Logger LOGGER = Logger.getLogger( Common.class.getName() );
    public HashMap<String, String> USERS = new HashMap<>();
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
    
//    public static void changeCursor(boolean busy) {
//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                setCursor(busy);
//            }
//        };
//
//        Thread bgThread = new Thread(task);
//        bgThread.setDaemon(true);
//        bgThread.start();
//    }
    
//    public static String currDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        return dateFormat.format(date);
//    }

    /**
     * Load users from database
     */
    public void loadUsers() {
        if (USERS.size() > 0) {
            log.write(Level.INFO, "USERS already loaded");
            return;
        }
        
        try {
            DB db = new DB(log);
            db.connect();
            String sql = "select userName, password from userhoser";
            ResultSet results =  db.exec( sql );
            int recordCount = 0;
           
            while (results.next()) {
                String user = results.getString("userName");
                String pwd = results.getString("password");
                log.write(Level.INFO, "adding (Username " + user + "; password: " + pwd + " to USERS");
                USERS.put(user, pwd);
                recordCount++;
            }
            
            log.write(Level.INFO, recordCount + " records added to USERS");
        }
        catch (SQLException e)  {
            log.write(Level.SEVERE, e.toString());
            log.write(Level.SEVERE, "SQLException: {0}", e.getMessage());
            log.write(Level.SEVERE, "SQLState: {0}", e.getSQLState());
            log.write(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
        }
    }
    
//    public static void disableMenu() {
//        menuBar.getMenus().forEach(( m ) -> {
//            m.getItems().forEach(( mi ) -> {
//                mi.setDisable( true );
//            });
//        });
//    }
    
//    public static void enableMenuItem( int menu, int menuItem) {
//        menuBar.getMenus().get( menu ).getItems().get( menuItem ).setDisable( false );
//    }
    
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
    
    public boolean validateUser(String user, String password) {
        if (!USERS.containsKey(user)) {
            return false;
        }
        else if (!(USERS.get(user).equals(password))) {
            return false;
        }
        else {
            return true;
        }
    }
}
