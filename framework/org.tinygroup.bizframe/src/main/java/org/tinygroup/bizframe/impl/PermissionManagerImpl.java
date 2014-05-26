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

import org.tinygroup.bizframe.PermissionObject;
import org.tinygroup.bizframe.PermissionSubject;

//TODO，缓冲支持，权限主体、客体继承支持，需要在策略代码中加以实现
/**
 * 如果没有配置策略，那么就是允许即返回是否配置了允许，禁止即是否配置了禁止
 * 
 * @author luoguo
 * 
 */
public class PermissionManagerImpl<K extends Comparable<K>> extends
		AbstractPermissionManager<K> {

	private PermissionStorage<K> storage = new PermissionStorage<K>();
	
	

	public void setPermissionObjectInheritSupport(boolean support) {
		super.setPermissionObjectInheritSupport(support);
		storage.setInheritSupport(support);
	}

	public PermissionSubject<K, ?> addPermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		storage.addPermissionSubject(permissionSubject);
		return permissionSubject;
	}

	public PermissionObject<K, ?> addPermissionObject(
			PermissionObject<K, ?> permissionObject) {
		storage.addPermissionObject(permissionObject);
		return permissionObject;
	}

	public void removePermissionObject(PermissionObject<K, ?> permissionObject) {
		storage.removePermissionObject(permissionObject);
	}

	public void removePermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		storage.removePermissionSubject(permissionSubject);
	}

	public PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue, Class<? extends PermissionSubject> subjectClassType) {
		return storage.getPermissionSubject(subjectBeanType, keyValue,
				subjectClassType);
	}

	public PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue, Class<? extends PermissionObject> objectClassType) {
		return storage.getPermissionObject(objectBeanType, keyValue,
				objectClassType);
	}

	public void addAllowPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		storage.addAllowPermission(permissionSubject, permissionObject);
	}

	public void addAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
			for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
				addAllowPermission(permissionSubject, permissionObject);
			}
		}
	}

	public void removeAllowPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		storage.removeAllowPermission(permissionSubject, permissionObject);
	}

	public void removeAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
			for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
				removeAllowPermission(permissionSubject, permissionObject);
			}
		}
	}

	public void addAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			addAllowPermission(permissionSubject, permissionObject);
		}
	}

	public void removeAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			removeAllowPermission(permissionSubject, permissionObject);
		}
	}

	public void addBlockPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		storage.addBlockPermission(permissionSubject, permissionObject);
	}

	public void addBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
			for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
				addBlockPermission(permissionSubject, permissionObject);
			}
		}
	}

	public void removeBlockPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		storage.removeBlockPermission(permissionSubject, permissionObject);
	}

	public void removeBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
			for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
				removeBlockPermission(permissionSubject, permissionObject);
			}
		}
	}

	public void addBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			addBlockPermission(permissionSubject, permissionObject);
		}

	}

	public void removeBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			removeBlockPermission(permissionSubject, permissionObject);
		}

	}

	public boolean isBlockDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return storage.isBlockDirectly(permissionSubject, permissionObject);
	}

	public boolean isAllowDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return storage.isAllowDirectly(permissionSubject, permissionObject);
	}

	public boolean isBlockDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			return isBlockDirectly(permissionSubject, permissionObject);
		}
		return false;
	}

	public boolean isAllowDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		PermissionSubject<K, ?> permissionSubject = storage
				.getPermissionSubject(permissionSubjectType,
						permissionSubjectId);
		PermissionObject<K, ?> permissionObject = storage.getPermissionObject(
				permissionObjectType, permissionObjectId);
		if (permissionSubject != null && permissionObject != null) {
			return isAllowDirectly(permissionSubject, permissionObject);
		}
		return false;
	}

	public List<PermissionSubject<K, ?>> getPermissionSubjects(
			String subjectBeanType,
			Class<? extends PermissionSubject> subjectClassType) {
		return storage.getPermissionSubjects(subjectBeanType, subjectClassType);
	}

	public List<PermissionObject<K, ?>> getPermissionObjects(
			String objectBeanType,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getPermissionObjects(objectBeanType, objectClassType);
	}

	public List<PermissionObject<K, ?>> getAssignedPermission(
			String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getAssignedPermission(subjectBeanType, objectBeanType,
				permissionSubjectId, objectClassType);
	}

	public List<PermissionSubject<K, ?>> getChildPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return storage.getChildPermissionSubjects(subjectBeanType, parentSubjectId, subjectClassType);
	}

	public List<PermissionObject<K, ?>> getChildPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getChildPermissionObjects(objectBeanType, parentObjectId, objectClassType);
	}

	public List<PermissionSubject<K, ?>> getBegatsPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return storage.getBegatsPermissionSubjects(subjectBeanType, parentSubjectId, subjectClassType);
	}

	public List<PermissionObject<K, ?>> getBegatsPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getBegatsPermissionObjects(objectBeanType, parentObjectId, objectClassType);
	}

	public PermissionSubject<K, ?> getChildPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return storage.getChildPermissionSubjectsWithTree(subjectBeanType, parentSubjectId, subjectClassType);
	}

	public PermissionSubject<K, ?> getBegatsPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return storage.getBegatsPermissionSubjectsWithTree(subjectBeanType, parentSubjectId, subjectClassType);
	}

	public PermissionObject<K, ?> getChildPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getChildPermissionObjectsWithTree(objectBeanType, parentObjectId, objectClassType);
	}

	public PermissionObject<K, ?> getBegatsPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return storage.getBegatsPermissionObjectsWithTree(objectBeanType, parentObjectId, objectClassType);
	}

}
