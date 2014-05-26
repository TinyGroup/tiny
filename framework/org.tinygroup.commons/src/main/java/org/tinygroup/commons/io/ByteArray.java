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
package org.tinygroup.commons.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 代表一个byte数组。
 *
 * @author Michael Zhou
 */
public class ByteArray {
    private final byte[] bytes;
    private final int    offset;
    private final int    length;

    public ByteArray(byte[] bytes) {
        this(bytes, 0, Integer.MIN_VALUE);
    }

    public ByteArray(byte[] bytes, int offset, int length) {
  
        if (length == Integer.MIN_VALUE) {
            length = bytes.length - offset;
        }

   
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
    }

    public byte[] getRawBytes() {
        return bytes;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public byte[] toByteArray() {
        byte[] copy = new byte[length];

        System.arraycopy(bytes, offset, copy, 0, length);

        return copy;
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(bytes, offset, length);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(bytes, offset, length);
    }

    
    public String toString() {
        return "byte[" + length + "]";
    }
}
