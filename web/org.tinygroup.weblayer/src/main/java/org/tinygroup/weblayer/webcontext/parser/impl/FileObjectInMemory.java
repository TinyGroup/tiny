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

import org.apache.commons.fileupload.FileItem;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.impl.AbstractFileObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

public class FileObjectInMemory extends AbstractFileObject implements ItemFileObject {

    private InMemoryFormFieldItem fileItem;

    public FileObjectInMemory(InMemoryFormFieldItem fileItem) {
        super(null);
        this.fileItem = fileItem;
    }

    public String getFileName() {
        return fileItem.getName();
    }

    public String getPath() {
        return null;
    }

    public String getAbsolutePath() {
        return null;
    }

    public String getExtName() {
        return FileUtil.getExtension(getFileName());
    }

    public boolean isExist() {
        return true;
    }

    public long getSize() {
        return fileItem.getSize();
    }

    public InputStream getInputStream() {
        try {
            return fileItem.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("获取上传文件输入流出错", e);
        }
    }

    public boolean isFolder() {
        return false;
    }


    public List<FileObject> getChildren() {
        return null;
    }

    public FileObject getChild(String fileName) {
        return null;
    }

    public long getLastModifiedTime() {
        return 0;
    }


    public boolean isInPackage() {
        return false;
    }

    public URL getURL() {
        return null;
    }

    public OutputStream getOutputStream() {
        try {
            return fileItem.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException("获取上传文件输出流出错", e);
        }
    }

    public FileItem getFileItem() {
        return fileItem;
    }

}
