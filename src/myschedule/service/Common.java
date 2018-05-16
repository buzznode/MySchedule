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
import java.util.Map;
import javafx.application.Platform;

/**
 *
 * @author bradd
 */
public class Common  {
//    private final static Logger LOGGER = Logger.getLogger( Common.class.getName() );
    public enum Auth { SUCCESS, INVALID_USER, INVALID_PASSWORD };
    public Map<Integer, String> authMsg = new HashMap<Integer, String>() {{
        put(0, "Successful Login");
        put(1, "Invalid Username");
        put(2, "Invalid Password");
    }};
    public HashMap<String, String> USERS = new HashMap<>();
    public static String currentUser;
    public static int currentUserId;
    public static String currentLangCode;
    public static String currentLangName;
    
    public static void changeCursor(boolean busy) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                setCursor(busy);
            }
        };

        Thread bgThread = new Thread(task);
        bgThread.setDaemon(true);
        bgThread.start();
    }
    
    public static String currDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    private String getAuthMsg(Auth authCode) {
        return authMsg.get(authCode);
    }
    
    public void loadUsers() {
        try {
            DB db = new DB();
            db.connect();
            String sql = "select userName, password from user";
            ResultSet results =  db.exec( sql );
            int recordCount = 0;
           
            while (results.next()) {
                String user = results.getString("userName");
                String pwd = results.getString("password");
                System.out.println("adding {serName: " + user + "; password: " + pwd + " to USERS");
                USERS.put(user, pwd);
                recordCount++;
            }
            System.out.println(recordCount + "records added to USERS");
        }
        catch ( SQLException e )  {
//            LOGGER.log(Level.SEVERE, e.toString(), e);
//            LOGGER.log(Level.SEVERE, "SQLException: {0}", e.getMessage());
//            LOGGER.log(Level.SEVERE, "SQLState: {0}", e.getSQLState());
//            LOGGER.log(Level.SEVERE, "VendorError: {0}", e.getErrorCode());
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
    
    public static void setCursor(boolean busy) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (busy) {
//                        Main.getRoot().sceneProperty().get().setCursor(Cursor.WAIT);
                    }
                    else {
//                        Main.getRoot().sceneProperty().get().setCursor(Cursor.DEFAULT);
                    }
                }
                catch (Exception e) {
                }
            }
        });
    }
    
    public String validateUser(String user, String password) {
        if (!USERS.containsKey(user)) {
            return getAuthMsg(Auth.INVALID_USER);
        }
        else if (!(USERS.get(user).equals(password))) {
            return getAuthMsg(Auth.INVALID_PASSWORD);
        }
        else {
            return getAuthMsg(Auth.SUCCESS);
        }
    }
}