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
package myschedule;

/**
 * @author bradd
 * @version 0.5.0
 */
public class Report {
    String description;
    String name;
    
    public Report(String _description, String _name) {
        this.description = _description;
        this.name = _name;
    }

    /**
    * Get description value
    * @return 
    */    
    @SuppressWarnings("unchecked")
    public String getDescription() {
        return description;
    }
    
    /**
     * Set description value
     * @param _description 
     */
    @SuppressWarnings("unchecked")
    public void setDescription(String _description) {
        this.description = _description;
    }
    
    /**
     * Get name value
     * @return 
     */
    @SuppressWarnings("unchecked")
    public String getName() {
        return name;
    }
    
    /**
     * Set name value
     * @param _name 
     */
    @SuppressWarnings("unchecked")
    public void setName(String _name) {
        this.name = _name;
    }
    
    /**
     * Override toString method
     * @return 
     */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        return description;
    }
}

