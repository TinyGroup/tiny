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
 * 代表session框架的异常。
 *
 * @author renhui
 */
public class SessionFrameworkException extends RuntimeException {
    private static final long serialVersionUID = 7308344005371211443L;

    public SessionFrameworkException() {
        super();
    }

    public SessionFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionFrameworkException(String message) {
        super(message);
    }

    public SessionFrameworkException(Throwable cause) {
        super(cause);
    }
}
