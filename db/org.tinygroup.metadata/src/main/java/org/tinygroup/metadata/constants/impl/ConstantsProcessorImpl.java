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
package org.tinygroup.metadata.constants.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.metadata.config.constants.Constant;
import org.tinygroup.metadata.config.constants.Constants;
import org.tinygroup.metadata.constants.ConstantProcessor;

public class ConstantsProcessorImpl implements ConstantProcessor {
	// private Map<String, Map<String, String>> constantsMap = new
	// HashMap<String, Map<String, String>>();
	private Map<String, Constant> constantsMap = new HashMap<String, Constant>();

	public boolean getBooleanValue(String name) {
		String value = getValue(name);
		return Boolean.parseBoolean(value);
	}

	public double getDoubleValue(String name) {
		String value = getValue(name);
		return Double.parseDouble(value);
	}

	public float getFloatValue(String name) {
		String value = getValue(name);
		return Float.parseFloat(value);
	}

	public char getCharValue(String name) {
		String value = getValue(name);
		return value.charAt(0);
	}

	public short getShortValue(String name) {
		String value = getValue(name);
		return Short.parseShort(value);
	}

	/**
	 * value必须是数字
	 */
	public byte getByteValue(String name) {
		String value = getValue(name);
		return new Byte(value);
	}

	public long getLongValue(String id) {
		String value = getValue(id);
		return Long.parseLong(value);
	}

	public String getStringValue(String id) {
		return getValue(id);
	}

	public int getIntValue(String name) {
		String value = getValue(name);
		return Integer.parseInt(value);
	}

	public void addConstants(Constants constants) {
		if (constants != null && constants.getConstantList() != null) {
			for (Constant c : constants.getConstantList()) {
				constantsMap.put(c.getId(), c);
			}
		}
	}
	public void removeConstants(Constants constants) {
		if (constants != null && constants.getConstantList() != null) {
			for (Constant c : constants.getConstantList()) {
				constantsMap.remove(c.getId());
			}
		}
	}

	private String getValue(String id) {
		if (constantsMap.containsKey(id))
			return constantsMap.get(id).getValue();
		throw new RuntimeException(String.format("ID:[%s]的常量不存在。", id));

	}
	// public void addConstants(Constants constants) {
	// String packageName = MetadataUtil.passNull(constants.getPackageName());
	// Map<String, String> valueMap = new HashMap<String, String>();
	// constantsMap.put(packageName, valueMap);
	// if (constants.getConstantList() != null) {
	// for (Constant constant : constants.getConstantList()) {
	// valueMap.put(constant.getName(), constant.getValue());
	// }
	// }
	// }
	// public int getIntValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Integer.parseInt(value);
	// }
	//
	// /**
	// * 如果指定的package存在，则在自己里拿，如果指定的package中不存在，则从别的里面找
	// * @param packageName
	// * @param name
	// * @return
	// */
	// private String getValue(String packageName, String name) {
	// if (packageName != null) {
	// String value = constantsMap.get(packageName).get(name);
	// if (value != null) {
	// return value;
	// }
	// }
	// for (String pkgName : constantsMap.keySet()) {
	// String value = constantsMap.get(pkgName).get(name);
	// if (value != null) {
	// return value;
	// }
	// }
	// throw new RuntimeException(String.format(
	// "package [%s] name [%s]的常量不存在。", packageName, name));
	//
	// }
	//
	// public boolean getBooleanValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Boolean.parseBoolean(value);
	// }
	//
	// public double getDoubleValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Double.parseDouble(value);
	// }
	//
	// public float getFloatValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Float.parseFloat(value);
	// }
	//
	// public char getCharValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return value.charAt(0);
	// }
	//
	// public short getShortValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Short.parseShort(value);
	// }
	//
	// /**
	// * value必须是数字
	// */
	// public byte getByteValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return new Byte(value);
	// }
	//
	// public long getLongValue(String packageName, String name) {
	// String value = getValue(packageName, name);
	// return Long.parseLong(value);
	// }
	//
	// public String getStringValue(String packageName, String name) {
	// return getValue(packageName, name);
	// }
	//
	// public int getIntValue(String name) {
	// return getIntValue(null, name);
	// }
	//
	// public boolean getBooleanValue(String name) {
	// return getBooleanValue(null, name);
	// }
	//
	// public double getDoubleValue(String name) {
	// return getDoubleValue(null, name);
	// }
	//
	// public float getFloatValue(String name) {
	// return getFloatValue(null, name);
	// }
	//
	// public char getCharValue(String name) {
	// return getCharValue(null, name);
	// }
	//
	// public short getShortValue(String name) {
	// return getShortValue(null, name);
	// }
	//
	// public byte getByteValue(String name) {
	// return getByteValue(null, name);
	// }
	//
	// public long getLongValue(String name) {
	// return getLongValue(null, name);
	// }
	//
	// public String getStringValue(String name) {
	// return getValue(null, name);
	// }


}
