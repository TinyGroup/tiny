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
package org.tinygroup.el.impl;
/**
 *  Copyright (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
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
 * --------------------------------------------------------------------------
 *  版权 (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  本开源软件遵循 GPL 3.0 协议;
 *  如果您不遵循此协议，则不被允许使用此文件。
 *  你可以从下面的地址获取完整的协议文本
 *
 *       http://www.gnu.org/licenses/gpl.html
 */


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.context.Context2Map;
import org.tinygroup.el.EL;
import org.mvel.MVEL;

/**
 * 默认基于MVEL实现
 * 
 * @author luoguo
 * 
 */
public class MvelImpl implements EL {
	private static Map<String, Serializable> cache = new HashMap<String, Serializable>();

	@SuppressWarnings("unchecked")
	public Object execute(String expression, Context context) {
		Map<String, Object> vars = new Context2Map(context);
		Serializable compiled = cache.get(expression);
		if (compiled == null) {
			compiled = MVEL.compileExpression(expression);
			cache.put(expression, compiled);
		}

		return MVEL.eval(expression, vars);
	}

}
