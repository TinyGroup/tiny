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
package org.tinygroup.convert.common;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import java.io.UnsupportedEncodingException;

public class StringByByteArray implements Converter<String, byte[]> {
	private String charset = null;

	public StringByByteArray(String charset) {
		this.charset = charset;
	}

	public StringByByteArray() {

	}

	public byte[] convert(String inputData) throws ConvertException {
		if (charset == null) {
			return inputData.getBytes();
		} else {
			try {
				return inputData.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				throw new ConvertException(e);
			}
		}
	}

}
