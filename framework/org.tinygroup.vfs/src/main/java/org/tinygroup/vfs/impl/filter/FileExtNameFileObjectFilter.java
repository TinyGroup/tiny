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
package org.tinygroup.vfs.impl.filter;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;

/**
 * 根据文件扩展名进行文件过滤
 * Created by luoguo on 14-2-26.
 */
public class FileExtNameFileObjectFilter implements FileObjectFilter {
    private String fileExtName;
    /**
     * 是否大小写敏感，默认不敏感
     */
    private boolean caseSensitive = false;

    public FileExtNameFileObjectFilter(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public FileExtNameFileObjectFilter(String fileExtName, boolean caseSensitive) {
        this(fileExtName);
        this.caseSensitive = caseSensitive;
    }

    public boolean accept(FileObject fileObject) {
        String extName = fileObject.getExtName();
        if (extName != null) {
            if (caseSensitive) {
                return extName.equals(fileExtName);
            } else {
                return extName.equalsIgnoreCase(fileExtName);
            }
        }
        return false;
    }
}
