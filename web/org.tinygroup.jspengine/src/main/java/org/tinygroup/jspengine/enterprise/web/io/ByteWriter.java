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
package org.tinygroup.jspengine.enterprise.web.io;

/**
 * This interface defines additional functionalities a web container can
 * provide for the response writer.  If implementated, perfermance will
 * likely to be improved.
 *
 * @author Kin-man Chung
 */
 
public interface ByteWriter {

    /**
     * Write a portion of a byte array to the output.
     *
     * @param  buff  A byte array
     * @param  off   Offset from which to start reading byte
     * @param  len   Number of bytes to write
     * @param  strlen If non-zero, the length of the string from which
     *               the bytes was converted.  If zero, then the string
     *               length is unknown.
     */
    void write(byte buff[], int off, int len, int strlen)
        throws java.io.IOException;
}
