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

import java.io.IOException;
import java.io.InputStream;

/**
 * 非同步的<code>ByteArrayInputStream</code>替换方案。本代码移植自IBM developer works文章：
 * <ul>
 * <li><a
 * href="http://www.ibm.com/developerworks/cn/java/j-io1/index.shtml">彻底转变流，第 1
 * 部分：从输出流中读取</a>
 * <li><a
 * href="http://www.ibm.com/developerworks/cn/java/j-io2/index.shtml">彻底转变流，第 2
 * 部分：优化 Java 内部 I/O</a>
 * </ul>
 *
 * @author Michael Zhou
 */
public class ByteArrayInputStream extends InputStream {
    // buffer from which to read
    private byte[] buffer;
    private int    index;
    private int    limit;
    private int    mark;

    // is the stream closed?
    private boolean closed;

    public ByteArrayInputStream(byte[] data) {
        this(data, 0, data.length);
    }

    public ByteArrayInputStream(byte[] data, int offset, int length) {
        if (data == null) {
            throw new NullPointerException();
        } else if (offset < 0 || offset + length > data.length || length < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            buffer = data;
            index = offset;
            limit = offset + length;
            mark = offset;
        }
    }

    
    public int read() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else if (index >= limit) {
            return -1; // EOF
        } else {
            return buffer[index++] & 0xff;
        }
    }

    
    public int read(byte[] data, int offset, int length) throws IOException {
        if (data == null) {
            throw new NullPointerException();
        } else if (offset < 0 || offset + length > data.length || length < 0) {
            throw new IndexOutOfBoundsException();
        } else if (closed) {
            throw new IOException("Stream closed");
        } else if (index >= limit) {
            return -1; // EOF
        } else {
            // restrict length to available data
            if (length > limit - index) {
                length = limit - index;
            }

            // copy out the subarray
            System.arraycopy(buffer, index, data, offset, length);
            index += length;
            return length;
        }
    }

    
    public long skip(long amount) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else if (amount <= 0) {
            return 0;
        } else {
            // restrict amount to available data
            if (amount > limit - index) {
                amount = limit - index;
            }

            index += (int) amount;
            return amount;
        }
    }

    
    public int available() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            return limit - index;
        }
    }

    
    public void close() {
        closed = true;
    }

    
    public void mark(int readLimit) {
        mark = index;
    }

    
    public void reset() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            // reset index
            index = mark;
        }
    }

    
    public boolean markSupported() {
        return true;
    }
}
