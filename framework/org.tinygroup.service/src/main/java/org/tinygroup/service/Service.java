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
package org.tinygroup.service;

import org.tinygroup.context.Context;

/**
 * 服务接口，所有服务都必须实现此接口
 * 
 * @author luoguo
 * 
 */
public interface Service {
	String SERVICE_XSTREAM_PACKAGENAME = "service";

	/**
	 * 执行服务
	 * 
	 * @param context
	 *            环境变量
	 */

	void execute(Context context);
}
