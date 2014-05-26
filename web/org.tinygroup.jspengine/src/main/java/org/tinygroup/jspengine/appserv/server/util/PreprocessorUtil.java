/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.jspengine.appserv.server.util;

import org.tinygroup.jspengine.appserv.BytecodePreprocessor;
import org.tinygroup.jspengine.common.util.logging.LogDomains;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PreprocessorUtil is a utility class for managing the bytecode 
 * preprocessor(s). The list of preprocessors are passed in as a string array
 * to the initialize method.  If there is a problem initialize any of the
 * preprocessors, all preprocessing is disabled. 
 */
public class PreprocessorUtil {
    
    private static boolean _preprocessorEnabled = false;
    private static BytecodePreprocessor[] _preprocessor;             
    
    /**
     * Initializes the preprocessor utility with the associated class names 
     * array arugment.
     * @param ppClassNames - the String array of preprocessor class names.
     * @return - true if successful, otherwise false.  All preprocessors must 
     * successfully initialize for true to be returned.
     */    
    public static boolean init (String[] ppClassNames) {        
        if (ppClassNames != null) {
            setupPreprocessor(ppClassNames);
        }
        return _preprocessorEnabled;
    }

    /**
     * Processes a class through the preprocessor.
     * @param className - the class name.
     * @param classBytes - the class byte array.
     * @return - the processed class byte array.
     */    
    public static byte[] processClass (String className, byte[] classBytes) {
        Logger _logger = LogDomains.getLogger(LogDomains.CMN_LOGGER);
        byte[] goodBytes = classBytes;
        if (_preprocessorEnabled) {            
            if (_preprocessor != null) {
                // Loop through all of the defined preprocessors...
                for (int i=0; i < _preprocessor.length; i++) {
                    classBytes = 
                        _preprocessor[i].preprocess(className, classBytes);
                    _logger.fine("[PreprocessorUtil.processClass] Preprocessor "
                        + i + " Processed Class: " + className); 
                    // Verify the preprocessor returned some bytes
                    if (classBytes != null){                           
                        goodBytes = classBytes;
                    }
                    else{
                        _logger.log(Level.SEVERE, 
                            "bytecodepreprocessor.preprocess_failed", 
                            new String[] {className,
                                          _preprocessor[i].getClass().getName()});
                            
                        // If were on the 1st preprocessor
                        if (i == 0){
                            _logger.log(
                                Level.SEVERE, 
                                "bytecodepreprocessor.resetting_original", 
                                className);
                        }
                        // We're on the 2nd or nth preprocessor.
                        else{
                            _logger.log(
                                Level.SEVERE, 
                                "bytecodepreprocessor.resetting_last_good",
                                className);
                        }                        
                    }                        
                }
            }
        }
        return goodBytes;               
    }
    
    private synchronized static void setupPreprocessor(String[] ppClassNames) {
        Logger _logger = LogDomains.getLogger(LogDomains.CMN_LOGGER);

        if (_preprocessor != null)
            // The preprocessors have already been set up.
            return;

        try {            
            _preprocessor = new BytecodePreprocessor[ppClassNames.length];
            for (int i = 0; i < ppClassNames.length; i++) {                
                String ppClassName = ppClassNames[i].trim();            
                Class ppClass = Class.forName(ppClassName);
                if (ppClass != null){
                    _preprocessor[i] = (BytecodePreprocessor)
                                                        ppClass.newInstance();
                    if (_preprocessor[i] instanceof BytecodePreprocessor){
                        _preprocessor[i] = 
                            (BytecodePreprocessor)_preprocessor[i];
                        _preprocessorEnabled = true;
                    } else {                    
                        _logger.log(Level.SEVERE, 
                            "bytecodepreprocessor.invalid_type", 
                            ppClassName);     
                        _logger.log(Level.SEVERE, 
                            "bytecodepreprocessor.disabled");                 
                        _preprocessorEnabled = false;
                    }
                }
                if (_preprocessor[i] != null){
                    if (!_preprocessor[i].initialize(new Hashtable())) {
                        _logger.log(Level.SEVERE, 
                            "bytecodepreprocessor.failed_init", 
                            ppClassName); 
                        _logger.log(Level.SEVERE, 
                            "bytecodepreprocessor.disabled");
                        _preprocessorEnabled = false;
                    }                    
                } else {
                    _logger.log(Level.SEVERE, 
                        "bytecodepreprocessor.failed_init", 
                        ppClassName); 
                    _logger.log(Level.SEVERE, 
                        "bytecodepreprocessor.disabled"); 
                    _preprocessorEnabled = false;
                }
            }
        } catch (Throwable t) {            
            _logger.log(Level.SEVERE, "bytecodepreprocessor.setup_ex", t); 
            _logger.log(Level.SEVERE, "bytecodepreprocessor.disabled"); 
            _preprocessorEnabled = false;
            return;
        }
    }        

    /**
     * Indicates whether or not the preprocessor is enabled
     * @return - true of the preprocessor is enabled, otherwise false.
     */    
    public static boolean isPreprocessorEnabled() {
        return _preprocessorEnabled;
    }    
}
