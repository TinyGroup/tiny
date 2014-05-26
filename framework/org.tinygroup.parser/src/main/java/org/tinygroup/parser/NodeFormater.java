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
package org.tinygroup.parser;

import java.io.IOException;
import java.io.OutputStream;

public interface NodeFormater<E extends Node<E>, T extends Document<E>> {
    /**
     * 格式化文档
     *
     * @param doc
     * @return String
     */
    String format(T doc);

    void setEncode(String encode);

    /**
     * 格式化文档、 并在指定的输出流中输出
     *
     * @param node
     * @return void
     */
    String format(E node);

    void format(T doc, OutputStream out) throws IOException;

    void format(E node, OutputStream out) throws IOException;
}
