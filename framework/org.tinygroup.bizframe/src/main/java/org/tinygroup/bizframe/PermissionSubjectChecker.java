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
package org.tinygroup.bizframe;

/**
 * 主体关系检查器接口
 * 
 * @author luoguo
 * 
 * @param <S>
 * @param <D>
 */
public interface PermissionSubjectChecker<K extends Comparable<K>> {

	Class<PermissionSubject<K, ?>> getPermissionSubjectType();

	Class<PermissionSubject<K, ?>> getPermissionSubjectContainerType();

	/**
	 * 返回sourceSubject是否在destSubject当中
	 * 
	 * @param sourceSubject
	 * @param destSubject
	 * @return
	 */
	boolean belongTo(PermissionSubject<K, ?> sourceSubject,
			PermissionSubject<K, ?> destSubject);

	/**
	 * 返回sourceSubject是否包含了destSubject
	 * 
	 * @param sourceSubject
	 * @param destSubject
	 * @return
	 */
	boolean contains(PermissionSubject<K, ?> sourceSubject,
			PermissionSubject<K, ?> destSubject);
}
