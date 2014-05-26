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
package org.tinygroup.weblayer.webcontext.parser.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.weblayer.webcontext.parser.upload.FileUploadReName;

public class DiskFileItem extends AbstractFileItem {
    private static final long serialVersionUID = 4225039123863446602L;

    public DiskFileItem(String fieldName, String contentType, boolean isFormField, boolean saveInFile,String fileName, int sizeThreshold,
                        boolean keepFormFieldInMemory, File repository,HttpServletRequest request,FileUploadReName rename) {
        super(fieldName, contentType, isFormField,saveInFile, fileName, sizeThreshold, keepFormFieldInMemory, repository,request,rename);
    }

    /** Removes the file contents from the temporary storage. */
    
    protected void finalize() throws Throwable {
        try {
            File outputFile = dfos.getFile();

            if (outputFile != null && outputFile.exists()) {
                outputFile.delete();
            }
        } finally {
            super.finalize();
        }
    }
}
