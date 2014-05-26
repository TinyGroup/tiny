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

import org.tinygroup.vfs.FileObject;
import org.apache.commons.fileupload.FileItem;

/**
 * 
 * 功能说明:获取文件对象关联的fileitem

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-1 <br>
 * <br>
 */
public interface ItemFileObject extends FileObject {
	
	/**
	 * 
	 * 文件对象关联的fileitem对象
	 * @return
	 */
	FileItem getFileItem();

}
