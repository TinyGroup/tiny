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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.bizframe.PermissionObject;
import org.tinygroup.bizframe.PermissionSubject;

/**
 * 权限信息存储对象
 * 
 * @author renhui
 * 
 */
public class PermissionStorage<K extends Comparable<K>> {

	private Map<String, PermissionSubject<K, ?>> permissionSubjectMap = new HashMap<String, PermissionSubject<K, ?>>();
	private Map<String, PermissionObject<K, ?>> permissionObjectMap = new HashMap<String, PermissionObject<K, ?>>();
	private Map<String, List<PermissionSubject<K, ?>>> permissionSubjectTypeMap = new HashMap<String, List<PermissionSubject<K, ?>>>();
	private Map<String, List<PermissionObject<K, ?>>> permissionObjectTypeMap = new HashMap<String, List<PermissionObject<K, ?>>>();
	private Map<String, List<PermissionObject<K, ?>>> allowPermissionMap = new HashMap<String, List<PermissionObject<K, ?>>>();
	private Map<String, List<PermissionObject<K, ?>>> blockPermissionMap = new HashMap<String, List<PermissionObject<K, ?>>>();

	private boolean inheritSupport;

	public boolean isInheritSupport() {
		return inheritSupport;
	}

	public void setInheritSupport(boolean inheritSupport) {
		this.inheritSupport = inheritSupport;
	}

	public void addPermissionSubject(PermissionSubject<K, ?> permissionSubject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		if (!permissionSubjectMap.containsKey(key)) {
			permissionSubjectMap.put(key, permissionSubject);
		}
		List<PermissionSubject<K, ?>> subjects = permissionSubjectTypeMap
				.get(permissionSubject.getType());
		if (subjects == null) {
			subjects = new ArrayList<PermissionSubject<K, ?>>();
			permissionSubjectTypeMap.put(permissionSubject.getType(), subjects);
		}
		subjects.add(permissionSubject);
	}

	public void addPermissionObject(PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionObject.getType(),
				permissionObject.getId());
		if (!permissionObjectMap.containsKey(key)) {
			permissionObjectMap.put(key, permissionObject);
		}
		List<PermissionObject<K, ?>> objects = permissionObjectTypeMap
				.get(permissionObject.getType());
		if (objects == null) {
			objects = new ArrayList<PermissionObject<K, ?>>();
			permissionObjectTypeMap.put(permissionObject.getType(), objects);
		}
		objects.add(permissionObject);
	}

	public void removePermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		permissionSubjectMap.remove(key);
	}

	public void removePermissionObject(PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionObject.getType(),
				permissionObject.getId());
		permissionObjectMap.remove(key);
	}

	public PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue) {
		String key = getKey(objectBeanType, keyValue);
		return permissionObjectMap.get(key);
	}

	public PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue, Class<? extends PermissionObject> objectClassType) {
		String key = getKey(objectBeanType, keyValue);
		return permissionObjectMap.get(key);
	}

	public PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue) {
		String key = getKey(subjectBeanType, keyValue);
		return permissionSubjectMap.get(key);
	}

	public PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue, Class<? extends PermissionSubject> subjectClassType) {
		String key = getKey(subjectBeanType, keyValue);
		return permissionSubjectMap.get(key);
	}

	public List<PermissionSubject<K, ?>> getPermissionSubjects(
			String subjectBeanType,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> permissionSubjects = permissionSubjectTypeMap
				.get(subjectBeanType);
		List<PermissionSubject<K, ?>> subjects = new ArrayList<PermissionSubject<K, ?>>();
		if (permissionSubjects != null) {
			for (PermissionSubject<K, ?> permissionSubject : permissionSubjects) {
				if (subjectClassType.isAssignableFrom(permissionSubject
						.getClass())) {
					subjects.add(permissionSubject);
				}
			}
		}
		return subjects;
	}

	public List<PermissionObject<K, ?>> getPermissionObjects(
			String objectBeanType,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> permissionObjects = permissionObjectTypeMap
				.get(objectBeanType);
		List<PermissionObject<K, ?>> objects = new ArrayList<PermissionObject<K, ?>>();
		if (permissionObjects != null) {
			for (PermissionObject<K, ?> permissionObject : permissionObjects) {
				if (objectClassType.isAssignableFrom(permissionObject
						.getClass())) {
					objects.add(permissionObject);
				}
			}
		}
		return objects;
	}

	public void addAllowPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = allowPermissionMap
				.get(key);
		if (permissionObjects == null) {
			permissionObjects = new ArrayList<PermissionObject<K, ?>>();
			allowPermissionMap.put(key, permissionObjects);
		}
		PermissionObject<K, ?> object = getPermissionObject(
				permissionObject.getType(), permissionObject.getId());
		permissionObjects.add(object);
	}

	public void removeAllowPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = allowPermissionMap
				.get(key);
		if (permissionObjects != null) {
			PermissionObject<K, ?> object = getPermissionObject(
					permissionObject.getType(), permissionObject.getId());
			permissionObjects.remove(object);
		}
	}

	public void addBlockPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = blockPermissionMap
				.get(key);
		if (permissionObjects == null) {
			permissionObjects = new ArrayList<PermissionObject<K, ?>>();
			blockPermissionMap.put(key, permissionObjects);
		}
		PermissionObject<K, ?> object = getPermissionObject(
				permissionObject.getType(), permissionObject.getId());
		permissionObjects.add(object);
	}

	public void removeBlockPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = blockPermissionMap
				.get(key);
		if (permissionObjects != null) {
			PermissionObject<K, ?> object = getPermissionObject(
					permissionObject.getType(), permissionObject.getId());
			permissionObjects.remove(object);
		}
	}

	public boolean isAllowDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = allowPermissionMap
				.get(key);
		if (permissionObjects != null) {
			PermissionObject<K, ?> object = getPermissionObject(
					permissionObject.getType(), permissionObject.getId());
			return permissionObjects.contains(object);
		}
		return false;
	}

	public boolean isBlockDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		List<PermissionObject<K, ?>> permissionObjects = blockPermissionMap
				.get(key);
		if (permissionObjects != null) {
			PermissionObject<K, ?> object = getPermissionObject(
					permissionObject.getType(), permissionObject.getId());
			return permissionObjects.contains(object);
		}
		return false;
	}

	public List<PermissionObject<K, ?>> getAssignedPermission(
			String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> objects = getSelfAssignedPermission(
				subjectBeanType, objectBeanType, permissionSubjectId,
				objectClassType);
		PermissionSubject<K, ?> permissionSubject = getPermissionSubject(
				subjectBeanType, permissionSubjectId);
		if (inheritSupport) {
			List<PermissionSubject<K, ?>> subjects = getBegatsPermissionSubjects(
					subjectBeanType, permissionSubjectId,
					permissionSubject.getClass());
			for (PermissionSubject<K, ?> subject : subjects) {
				objects.addAll(getSelfAssignedPermission(subjectBeanType,
						objectBeanType, subject.getId(), objectClassType));
			}
		}
		return objects;
	}

	private List<PermissionObject<K, ?>> getSelfAssignedPermission(
			String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType) {
		String key = getKey(subjectBeanType, permissionSubjectId);
		List<PermissionObject<K, ?>> permissionObjects = allowPermissionMap
				.get(key);
		List<PermissionObject<K, ?>> objects = new ArrayList<PermissionObject<K, ?>>();
		if (permissionObjects != null) {
			for (PermissionObject<K, ?> permissionObject : permissionObjects) {
				if (permissionObject.getType().equals(objectBeanType)
						&& objectClassType.isAssignableFrom(permissionObject
								.getClass())) {
					objects.add(permissionObject);
				}
			}
		}
		return objects;
	}

	public List<PermissionSubject<K, ?>> getChildPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> permissionSubjects = permissionSubjectTypeMap
				.get(subjectBeanType);
		List<PermissionSubject<K, ?>> subjects = new ArrayList<PermissionSubject<K, ?>>();
		if (permissionSubjects != null) {
			for (PermissionSubject<K, ?> permissionSubject : permissionSubjects) {
				if (permissionSubject.getParentId().equals(parentSubjectId)
						&& subjectClassType.isAssignableFrom(permissionSubject
								.getClass())) {
					subjects.add(permissionSubject);
				}
			}
		}
		return subjects;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PermissionSubject<K, ?> getChildPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List childrens = getChildPermissionSubjects(subjectBeanType,
				parentSubjectId, subjectClassType);
		PermissionSubject<K, ?> permissionSubject = getPermissionSubject(
				subjectBeanType, parentSubjectId, subjectClassType);
		permissionSubject.setSubList(childrens);
		return permissionSubject;
	}

	public List<PermissionObject<K, ?>> getChildPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> permissionObjects = permissionObjectTypeMap
				.get(objectBeanType);
		List<PermissionObject<K, ?>> objects = new ArrayList<PermissionObject<K, ?>>();
		if (permissionObjects != null) {
			for (PermissionObject<K, ?> permissionObject : permissionObjects) {
				if (permissionObject.getParentId().equals(parentObjectId)
						&& objectClassType.isAssignableFrom(permissionObject
								.getClass())) {
					objects.add(permissionObject);
				}
			}
		}
		return objects;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PermissionObject<K, ?> getChildPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List childrens = getChildPermissionObjects(objectBeanType,
				parentObjectId, objectClassType);
		PermissionObject<K, ?> permissionObject = getPermissionObject(
				objectBeanType, parentObjectId, objectClassType);
		permissionObject.setSubList(childrens);
		return permissionObject;
	}

	public List<PermissionSubject<K, ?>> getBegatsPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> permissionSubjects = permissionSubjectTypeMap
				.get(subjectBeanType);
		List<PermissionSubject<K, ?>> subjects = new ArrayList<PermissionSubject<K, ?>>();
		if (permissionSubjects != null) {
			for (PermissionSubject<K, ?> permissionSubject : permissionSubjects) {
				if (permissionSubject.getParentId().equals(parentSubjectId)
						&& subjectClassType.isAssignableFrom(permissionSubject
								.getClass())) {
					subjects.add(permissionSubject);
					List<PermissionSubject<K, ?>> childs = getBegatsPermissionSubjects(
							subjectBeanType, permissionSubject.getId(),
							subjectClassType);
					if (childs.size() > 0) {
						subjects.addAll(childs);
					}
				}
			}
		}
		return subjects;
	}

	public List<PermissionObject<K, ?>> getBegatsPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> permissionObjects = permissionObjectTypeMap
				.get(objectBeanType);
		List<PermissionObject<K, ?>> objects = new ArrayList<PermissionObject<K, ?>>();
		if (permissionObjects != null) {
			for (PermissionObject<K, ?> permissionSubject : permissionObjects) {
				if (permissionSubject.getParentId().equals(parentObjectId)
						&& objectClassType.isAssignableFrom(permissionSubject
								.getClass())) {
					objects.add(permissionSubject);
					List<PermissionObject<K, ?>> childs = getBegatsPermissionObjects(
							objectBeanType, permissionSubject.getId(),
							objectClassType);
					if (childs.size() > 0) {
						objects.addAll(childs);
					}
				}
			}
		}
		return objects;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PermissionSubject<K, ?> getBegatsPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List childrens = getChildPermissionSubjects(subjectBeanType,
				parentSubjectId, subjectClassType);
		PermissionSubject<K, ?> permissionSubject = getPermissionSubject(
				subjectBeanType, parentSubjectId, subjectClassType);
		if (childrens.size() > 0) {
			for (int i = 0; i < childrens.size(); i++) {
				PermissionSubject<K, ?> subject = (PermissionSubject<K, ?>) childrens
						.get(i);
				List subs = getChildPermissionSubjects(subjectBeanType,
						subject.getId(), subjectClassType);
				if (subs.size() > 0) {
					getBegatsPermissionSubjectsWithTree(subjectBeanType,
							subject.getId(), subjectClassType);
				}
			}
			permissionSubject.setSubList(childrens);
		}
		return permissionSubject;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PermissionObject<K, ?> getBegatsPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List childrens = getChildPermissionObjects(objectBeanType,
				parentObjectId, objectClassType);
		PermissionObject<K, ?> permissionObject = getPermissionObject(
				objectBeanType, parentObjectId, objectClassType);
		if (childrens.size() > 0) {
			for (int i = 0; i < childrens.size(); i++) {
				PermissionObject<K, ?> subject = (PermissionObject<K, ?>) childrens
						.get(i);
				List subs = getChildPermissionObjects(objectBeanType,
						subject.getId(), objectClassType);
				if (subs.size() > 0) {
					getBegatsPermissionObjectsWithTree(objectBeanType,
							subject.getId(), objectClassType);
				}
			}
			permissionObject.setSubList(childrens);
		}
		return permissionObject;
	}

	private String getKey(String type, K id) {
		return type + "-" + id;
	}

}
