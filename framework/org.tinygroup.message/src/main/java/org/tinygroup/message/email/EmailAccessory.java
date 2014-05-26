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
package org.tinygroup.message.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 邮件的附件
 * Created by luoguo on 2014/4/18.
 */
public class EmailAccessory {
    private String fileName;
    private byte[] content;

    public EmailAccessory() {

    }

    public EmailAccessory(File file) throws IOException {
        this.fileName = file.getName();
        FileInputStream inputStream = new FileInputStream(file);
        content = new byte[(int) file.length()];
        inputStream.read(content);
        inputStream.close();
    }

    public EmailAccessory(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content.clone();
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content.clone();
    }
}
