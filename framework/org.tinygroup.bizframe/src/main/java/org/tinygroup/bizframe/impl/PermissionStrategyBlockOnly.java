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
package org.tinygroup.bizframe.impl;

import org.tinygroup.bizframe.PermissionManager;
import org.tinygroup.bizframe.PermissionObject;
import org.tinygroup.bizframe.PermissionCheckStrategy;
import org.tinygroup.bizframe.PermissionSubject;

/**
 * 仅可以设置允许，凡是明确禁止的即表示没有权限，否则表示有权限，适用于宽松场景,相当于黑名单
 * 
 * @author luoguo
 * 
 */
public class PermissionStrategyBlockOnly<K extends Comparable<K>> implements
		PermissionCheckStrategy<K> {

	private PermissionManager<K> permissionManager;

	public void setPermissionManager(PermissionManager<K> permissionManager) {
		this.permissionManager = permissionManager;

	}

	public boolean isAllow(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return isBlock(permissionSubject, permissionObject);
	}

	public boolean isBlock(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return permissionManager.isBlockDirectly(permissionSubject,
				permissionObject);

	}

	public boolean isBlock(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		return permissionManager.isBlockDirectly(permissionSubjectType,
				permissionSubjectId, permissionObjectType, permissionObjectId);
	}

	public boolean isAllow(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		return !isBlock(permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
	}

}
