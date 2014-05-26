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
package org.tinygroup.weblayer.webcontext.buffered.exception;

import org.tinygroup.weblayer.webcontext.WebContextException;


/**
 * 代表一个buffer commit失败的异常。
 *
 * @author renhui
 */
public class BufferCommitFailedException extends WebContextException {
    private static final long serialVersionUID = 4884236978077840652L;

    /** 创建一个异常。 */
    public BufferCommitFailedException() {
        super();
    }

    /**
     * 创建一个异常。
     *
     * @param message 异常信息
     */
    public BufferCommitFailedException(String message) {
        super(message);
    }

    /**
     * 创建一个异常。
     *
     * @param message 异常信息
     * @param cause   异常原因
     */
    public BufferCommitFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建一个异常。
     *
     * @param cause 异常原因
     */
    public BufferCommitFailedException(Throwable cause) {
        super(cause);
    }
}
