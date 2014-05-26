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
package org.tinygroup.format;

import org.tinygroup.context.Context;
import org.tinygroup.format.exception.FormatException;

/**
 * 格式化提供者，把指定的内容换成另外的内容
 * 
 * @author luoguo
 * 
 */
public interface FormatProvider {
	/**
	 * 把指定的值进行处理后返回
	 * 
	 * @param string
	 *            要进行格式化的值
	 * @return 格式化好的值
	 * @throws FormatException
	 */
	String format(Context context, String string) throws FormatException;

}
