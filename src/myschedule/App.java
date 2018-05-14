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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myschedule.service.DB;

/**
 * @author bradd
 * @version 0.5.0
 */
public class App extends Application {
    
    // Private general variables
    private boolean loggedIn;
    private String userName;

    private static final Logger LOGGER = Logger.getLogger("myschedule.log");
    protected DB db = new DB();

    /**
     * @return loggedIn as boolean
     */
    protected boolean loggedIn() {
        return loggedIn;
    }
    
    /**
     * @param _loggedIn
     * @return  _loggedIn as boolean
     */
    protected boolean loggedIn(boolean _loggedIn) {
        return loggedIn = _loggedIn;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainContainer.fxml"));
        Parent node = loader.load();
        MainController controller = loader.getController();
        controller.injectApp(this);
        Scene scene = new Scene(node);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * @return userName
     */
    protected String userName() {
        return userName;
    }

    /**
     * @param _userName
     * @return userName 
     */
    protected String userName(String _userName) {
        return userName = _userName;
    }
    
    /**
     * @param level
     * @param msg 
     */
    protected void writeLog(Level level, String msg) {
        LOGGER.log(level, msg);
    }
}
