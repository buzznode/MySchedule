/*
 * The MIT License
 *
 * Copyright 2018 bradd.
 *
 * Permission is hereby , free of charge, to any person obtaining a copy
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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRgrantedACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package myschedule;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myschedule.service.Common;
import myschedule.service.DB;
import myschedule.service.Logging;

/**
 * @author bradd
 * @version 0.5.0
 */
public class App extends Application {
    
    // Private general variables   
//    private boolean loggedIn = false;
//    private String userName = "";
    
    private boolean loggedIn = true;
    private String userName = "bradd";
    
    protected Stage mainStage;

    // Classes
    protected ResourceBundle rb;
    protected final Logging log = new Logging();
    protected final Common common = new Common(log);
    protected final DB db = new DB(log);

    /**
     * Localize a string
     * @param str
     * @return 
     */
    public String localize(String str) {
        return rb.getString(str).trim();
    }
    
    /**
     * Get loggedIn value
     * @return loggedIn as boolean
     */
    protected boolean loggedIn() {
        return loggedIn;
    }
    
    /**
     * Set loggedIn value
     * @param _loggedIn
     * @return  _loggedIn as boolean
     */
    protected boolean loggedIn(boolean _loggedIn) {
        return loggedIn = _loggedIn;
    }
    
    /**
     * Main routine
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Start routine for class
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        log.write(Level.INFO, "Starting application...");
        Locale.setDefault(new Locale("en", "EN"));
//        Locale.setDefault(new Locale("de", "DE"));
        rb = ResourceBundle.getBundle("language_files/rb");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainContainer.fxml"));
        Parent node = loader.load();
        MainController main = loader.getController();
        main.injectApp(this);
        main.start();
        Scene scene = new Scene(node);
        stage.setScene(scene);
        mainStage = stage;
        stage.show();
    }
    
    /**
     * Get userName value
     * @return userName
     */
    protected String userName() {
        return userName;
    }

    /**
     * Set userName value
     * @param _userName
     * @return userName 
     */
    protected String userName(String _userName) {
        return userName = _userName;
    }
}
