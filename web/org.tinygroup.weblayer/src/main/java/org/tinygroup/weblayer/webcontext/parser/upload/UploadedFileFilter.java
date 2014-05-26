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
package org.tinygroup.weblayer.webcontext.parser.upload;

import org.apache.commons.fileupload.FileItem;

/**
 * 过滤用户上传的文件。
 *
 * @author renhui
 */
public interface UploadedFileFilter extends ParameterParserFilter {
    /**
     * 过滤指定文件，如果返回<code>null</code>表示忽略该文件。
     * <p>
     * 注意，<code>file</code>可能是<code>null</code>。
     * </p>
     */
    FileItem filter(String key, FileItem file);
}
