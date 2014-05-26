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
package org.tinygroup.weblayer.webcontext.session.exception;


/**
 * 代表编码、解码时发生的错误。
 *
 * @author renhui
 */
public class SessionEncoderException extends SessionFrameworkException {
    private static final long serialVersionUID = -4586631007196706066L;

    public SessionEncoderException() {
        super();
    }

    public SessionEncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionEncoderException(String message) {
        super(message);
    }

    public SessionEncoderException(Throwable cause) {
        super(cause);
    }
}
