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
package org.tinygroup.commons.exceptions;

/**
 * Created by IntelliJ IDEA. User: luoguo Date: 11-3-31 Time: 上午11:45 To change
 * this template use File | Settings | File Templates.
 */
public class ComparableException extends RuntimeException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7581644376850121494L;

	public ComparableException() {
		super("The object must implements interface <Comparable>");
	}

	public ComparableException(String string) {
		super(string);
	}
}
