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
package org.tinygroup.metadata.constants;

import org.tinygroup.metadata.config.constants.Constants;

public interface ConstantProcessor {
	/**
	 * 返回常量值
	 * @param packageName
	 * @param name
	 * @return
	 */
//	int getIntValue(String packageName, String name);
//
//	boolean getBooleanValue(String packageName, String name);
//
//	double getDoubleValue(String packageName, String name);
//
//	float getFloatValue(String packageName, String name);
//
//	char getCharValue(String packageName, String name);
//
//	short getShortValue(String packageName, String name);
//
//	byte getByteValue(String packageName, String name);
//
//	long getLongValue(String packageName, String name);
//
//	String getStringValue(String packageName, String name);

	int getIntValue(String id);

	boolean getBooleanValue(String id);

	double getDoubleValue(String id);

	float getFloatValue(String id);

	char getCharValue(String id);

	short getShortValue(String id);

	byte getByteValue(String id);

	long getLongValue(String id);

	String getStringValue(String id);

	/**
	 * 添加常量 
	 * @param constants
	 */
	void addConstants(Constants constants);
	/**
	 * 移除常量 
	 * @param constants
	 */
	void removeConstants(Constants constants);
}
