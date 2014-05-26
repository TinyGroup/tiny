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
package org.tinygroup.context2object.impl;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.tinygroup.context2object.TypeConverter;

public class DateTypeConverter implements TypeConverter<String, Date> {
	String format = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat dateFormat = null;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
		dateFormat = new SimpleDateFormat(format);
	}

	public Class<String> getSourceType() {
		return String.class;
	}

	public Class<Date> getDestinationType() {
		return Date.class;
	}

	public Date getObject(String value) {
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(format);
		}
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
