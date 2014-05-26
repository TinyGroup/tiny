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
package org.tinygroup.imda;

import org.tinygroup.context.Context;

/**
 * 检查是否有权限
 * 
 * @author luoguo
 * 
 */
public interface ModelPermissionChecker {
	/**
	 * 
	 * @param modelId
	 *            模型标识
	 * @param operationName
	 *            操作类型
	 * @param context
	 * @return
	 */
	boolean isHasPermission(String modelId, String operationName,
			Context context);
}
