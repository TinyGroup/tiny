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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;

/**
 * 
 * 功能说明: https协议提供

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-30 <br>
 * <br>
 */
public class HttpsSchemaProvider implements SchemaProvider {

	public static final String HTTPS_PROTOCAL = "https:";

	public boolean isMatch(String resource) {
		String lowerCase = resource.toLowerCase();
		return lowerCase.startsWith(HTTPS_PROTOCAL) ;
	}

	public String getSchema() {
		return HTTPS_PROTOCAL;
	}

	public FileObject resolver(String resource) {
		return new HttpsFileObject(this, resource);
	}

}
