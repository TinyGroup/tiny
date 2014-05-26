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

import java.util.List;

import org.tinygroup.bizframe.PermissionCheckStrategy;
import org.tinygroup.bizframe.PermissionManager;
import org.tinygroup.bizframe.PermissionObject;
import org.tinygroup.bizframe.PermissionObjectChecker;
import org.tinygroup.bizframe.PermissionSubject;
import org.tinygroup.bizframe.PermissionSubjectChecker;

/**
 * 
 * 功能说明: 权限管理的抽象实现
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-31 <br>
 * <br>
 */
public abstract class AbstractPermissionManager<K extends Comparable<K>>
		implements PermissionManager<K> {
	protected PermissionCheckStrategy<K> permissionStrategy = null;
	protected boolean permissionSubjectInheritSupport;// 权限主体是否支持继承
	protected boolean permissionObjectInheritSupport;// 权限客体是否支持继承
	protected boolean cacheSupport;// 是否允许缓冲
	protected List<PermissionSubjectChecker<K>> permissionSubjectCheckerList;
	protected List<PermissionObjectChecker<K>> permissionObjectCheckerList;

	public void setPermissionStrategy(
			PermissionCheckStrategy<K> permissionStrategy) {
		permissionStrategy.setPermissionManager(this);
		this.permissionStrategy = permissionStrategy;
	}

	public boolean isPermissionObjectInheritSupport() {
		return permissionObjectInheritSupport;
	}

	public void setPermissionObjectInheritSupport(boolean support) {
		this.permissionObjectInheritSupport = support;

	}

	public boolean isPermissionSubjectInheritSupport() {
		return permissionSubjectInheritSupport;
	}

	public void setPermissionSubjectInheritSupport(boolean support) {
		this.permissionSubjectInheritSupport = support;
	}

	public boolean isCacheSupport() {
		return cacheSupport;
	}

	public void setCacheSupport(boolean support) {
		this.cacheSupport = support;
	}

	public void setPermissionSubjectCheckerList(
			List<PermissionSubjectChecker<K>> permissionSubjectCheckerList) {
		this.permissionSubjectCheckerList = permissionSubjectCheckerList;
	}

	public void setPermissionObjectCheckerList(
			List<PermissionObjectChecker<K>> permissionObjectCheckerList) {
		this.permissionObjectCheckerList = permissionObjectCheckerList;
	}

	public boolean belongTo(PermissionSubject<K, ?> sourceObject,
			PermissionSubject<K, ?> destObject) {
		for (PermissionSubjectChecker<K> checker : permissionSubjectCheckerList) {

			boolean sourceTypeMatch = checker.getPermissionSubjectType()
					.isInstance(sourceObject);
			boolean destTypeMatch = checker.getPermissionSubjectContainerType()
					.isInstance(destObject);
			if (sourceTypeMatch && destTypeMatch) {
				boolean result = checker.belongTo(sourceObject, destObject);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains(PermissionSubject<K, ?> sourceObject,
			PermissionSubject<K, ?> destObject) {
		for (PermissionSubjectChecker<K> checker : permissionSubjectCheckerList) {

			boolean sourceTypeMatch = checker.getPermissionSubjectType()
					.isInstance(sourceObject);
			boolean destTypeMatch = checker.getPermissionSubjectContainerType()
					.isInstance(destObject);
			if (sourceTypeMatch && destTypeMatch) {
				boolean result = checker.contains(sourceObject, destObject);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean belongTo(PermissionObject<K, ?> sourceObject,
			PermissionObject<K, ?> destObject) {
		for (PermissionObjectChecker<K> checker : permissionObjectCheckerList) {

			boolean sourceTypeMatch = checker.getPermissionObjectType()
					.isInstance(sourceObject);
			boolean destTypeMatch = checker.getPermissionObjectContainerType()
					.isInstance(destObject);
			if (sourceTypeMatch && destTypeMatch) {
				boolean result = checker.belongTo(sourceObject, destObject);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains(PermissionObject<K, ?> sourceObject,
			PermissionObject<K, ?> destObject) {
		for (PermissionObjectChecker<K> checker : permissionObjectCheckerList) {

			boolean sourceTypeMatch = checker.getPermissionObjectType()
					.isInstance(sourceObject);
			boolean destTypeMatch = checker.getPermissionObjectContainerType()
					.isInstance(destObject);
			if (sourceTypeMatch && destTypeMatch) {
				boolean result = checker.contains(sourceObject, destObject);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isBlock(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		if (permissionStrategy != null) {
			return permissionStrategy.isBlock(permissionSubject,
					permissionObject);
		} else {
			return isBlockDirectly(permissionSubject, permissionObject);
		}
	}

	public boolean isAllow(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		if (permissionStrategy != null) {
			return permissionStrategy.isAllow(permissionSubject,
					permissionObject);
		} else {
			return isAllowDirectly(permissionSubject, permissionObject);
		}
	}

	public boolean isBlock(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		if (permissionStrategy != null) {
			return permissionStrategy.isBlock(permissionSubjectType,
					permissionSubjectId, permissionObjectType,
					permissionObjectId);
		} else {
			return isBlockDirectly(permissionSubjectType, permissionSubjectId,
					permissionObjectType, permissionObjectId);
		}
	}

	public boolean isAllow(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		if (permissionStrategy != null) {
			return permissionStrategy.isAllow(permissionSubjectType,
					permissionSubjectId, permissionObjectType,
					permissionObjectId);
		} else {
			return isAllowDirectly(permissionSubjectType, permissionSubjectId,
					permissionObjectType, permissionObjectId);
		}
	}

	
}
