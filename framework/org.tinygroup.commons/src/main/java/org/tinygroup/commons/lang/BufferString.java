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
package org.tinygroup.commons.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

;
/**
 * 利用Buffer构建的字符串,Buffer一定不能被清掉的
 * 
 * @author luoguo
 * 
 */
public final class BufferString implements Comparable<BufferString> {
	int start, length = 0;
	byte[] buf;
	int hash;

	/**
	 * 附加到Buffer上
	 * 
	 * @param buffer
	 * @param start
	 */
	public void appendToBuffer(byte[] buffer, int start) {
		for (int i = 0; i < length; i++) {
			buffer[start + i] = buf[this.start + i];
		}
	}

	public BufferString(byte[] buf, int start, int length) {
		this.start = start;
		this.buf = buf;
		if (start + length > buf.length - 1) {
			this.length = buf.length - start;
		} else {
			this.length = length;
		}
	}

	public byte byteAt(int pos) {
		return buf[start + pos];
	}

	private boolean equalsBufferString(BufferString myString) {
		if (hashCode() != myString.hashCode()) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (buf[start + i] != myString.buf[myString.start + i]) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof BufferString) {
			BufferString string = (BufferString) object;
			return equalsBufferString(string);
		}
		return false;
	}

	public int length() {
		return length;
	}

	public int hashCode() {
		int h = hash;
		if (h == 0 && length > 0) {
			int off = start;
			int len = length;

			for (int i = 0; i < len; i++) {
				h = 31 * h + buf[off++];
			}
			hash = h;
		}
		return h;
	}

	public String toString() {
		return toString("GBK");
	}

	public String toString(String... encoding) {
		try {
			return new String(buf, start, length, encoding[0]);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public int write(OutputStream out) throws IOException {
		out.write(buf, start, length);
		return length;
	}

	public int compareTo(BufferString string) {
		for (int i = 0; i < length && i < string.length; i++) {
			if (buf[start + i] > string.buf[string.start + i]) {
				return 1;
			}
			if (buf[start + i] < string.buf[string.start + i]) {
				return -1;
			}
		}
		if (length == string.length) {
			return 0;
		}
		if (length > string.length) {
			return 1;
		}
		return -1;
	}
}
