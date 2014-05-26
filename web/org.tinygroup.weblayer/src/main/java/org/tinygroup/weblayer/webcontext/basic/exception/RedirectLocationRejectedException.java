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
package org.tinygroup.weblayer.webcontext.basic.exception;

/**
 * 代表一个重定向location被拒绝的异常。
 *
 * @author renhui
 */
public class RedirectLocationRejectedException extends ResponseHeaderRejectedException {
    private static final long serialVersionUID = -2667477249289081304L;

    public RedirectLocationRejectedException() {
    }

    public RedirectLocationRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedirectLocationRejectedException(String message) {
        super(message);
    }

    public RedirectLocationRejectedException(Throwable cause) {
        super(cause);
    }
}
