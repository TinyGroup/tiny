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
package org.tinygroup.velocity;

import org.apache.velocity.context.Context;

/**
 * 把Tiny的环境包装成Velocity的环境
 * 
 * @author luoguo
 * 
 */
public class TinyVelocityContext implements Context {
	private org.tinygroup.context.Context context;

	public org.tinygroup.context.Context getContext() {
		return context;
	}

	public TinyVelocityContext(org.tinygroup.context.Context context) {
		this.context = context;
	}

	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	public Object get(String key) {
		return context.get(key);
	}

	public boolean containsKey(Object key) {
		return context.exist(key.toString());
	}

	public Object[] getKeys() {
		return context.getItemMap().keySet().toArray();
	}

	public Object remove(Object key) {
		return context.remove(key.toString());
	}

}
