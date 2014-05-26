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
 * 客户关系检查器接口
 * 
 * @author luoguo
 * 
 * @param <S>
 * @param <D>
 */
public interface PermissionObjectChecker<K extends Comparable<K>> {
	Class<PermissionObject<K,?>> getPermissionObjectType();

	Class<PermissionObject<K,?>> getPermissionObjectContainerType();
	/**
	 * 返回sourceObject是否在destObject当中
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean belongTo(PermissionObject<K,?> sourceObject, PermissionObject<K,?> destObject);

	/**
	 * 返回sourceObject是否包含了destObject
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean contains(PermissionObject<K,?> sourceObject, PermissionObject<K,?> destObject);
}
