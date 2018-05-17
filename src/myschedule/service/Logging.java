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

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Logging {
    
    protected static final Logger LOGGER = Logger.getLogger("myschedule");
    
    public Logging() {
        
        Handler consoleHandler;
        Handler fileHandler;

        try {
            consoleHandler = new ConsoleHandler();
            fileHandler = new FileHandler("./myschedule.log");
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        }
        catch(IOException ex) {
        }
    }
    
    /**
     * @param level
     * @param msg 
     */
    public void write(Level level, String msg) {
        LOGGER.log(level, msg);
    }
    
    public void write(Level level, String msg, Object param) {
        LOGGER.log(level, msg, param);
    }
    
    public void write(Level level, String msg, Object[] params) {
        LOGGER.log(level, msg, params);
    }
}

