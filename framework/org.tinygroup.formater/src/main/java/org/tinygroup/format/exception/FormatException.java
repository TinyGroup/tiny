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
package org.tinygroup.format.exception;

/**
 * 格式化异常
 * 
 * @author luoguo
 * 
 */
public class FormatException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3335058564386168996L;

	public FormatException(String message) {
		super(message);
	}

	public FormatException(Throwable throwable) {
		super(throwable);
	}

	public FormatException(String message, Throwable throwable) {
		super("格式化错误", throwable);
	}
}
