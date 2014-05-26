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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.tinygroup.commons.tools.BasicConstant;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.support.BeanSupport;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadedFileFilter;

/**
 * 根据文件名后缀过滤上传文件。
 *
 * @author Michael Zhou
 */
public class UploadedFileExtensionWhitelist extends BeanSupport implements UploadedFileFilter {
    
	private  static Logger logger = LoggerFactory.getLogger(AbstractTinyFilter.class);
    private String[] extensions;

    public void setAllowedExtensions(String[] extensions) {
        this.extensions = extensions;
    }


    public boolean isFiltering(HttpServletRequest request) {
        return true;
    }

    
    protected void init() throws Exception {
        if (extensions == null) {
            extensions = BasicConstant.EMPTY_STRING_ARRAY;
        }

        for (int i = 0; i < extensions.length; i++) {
            extensions[i] = FileUtil.normalizeExtension(extensions[i]);
        }
    }

    public FileItem filter(String key, FileItem file) {
        if (file == null) {
            return null;
        }

        boolean allowed = false;

        // 未指定文件名 - 返回null
        // 文件名没有后缀 - 返回字符串“null”
        // 后缀被规格化为小写字母
        String ext = FileUtil.getExtension(file.getName(), "null", true);

        if (ext != null) {
            for (String allowedExtension : extensions) {
                if (allowedExtension.equals(ext)) {
                    allowed = true;
                    break;
                }
            }
        }

        if (!allowed) {
        	logger.logMessage(LogLevel.WARN,"Uploaded file type \"{}\" is denied: {}", ext, file.getName());
            return null;
        } else {
            return file;
        }
    }


}
