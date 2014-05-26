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

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 功能说明: 上传文件命名接口

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-27 <br>
 * <br>
 */
public interface FileUploadReName {

	/**
	 * 
	 * 对上传过来的本地文件进行重新命名,返回的是重新命名后的文件全路径
	 * @param localFileName
	 * @param request
	 * @return
	 */
	String reName(String localFileName,HttpServletRequest request);
	
}
