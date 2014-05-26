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
import org.tinygroup.tinydb.Bean;

/**
 * 
 * 功能说明:权限关联表记录缓存
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-26 <br>
 * <br>
 */
public class AuthRelationCache<K extends Comparable<K>> {

	/**
	 * 权限主体缓存 key:sub_type+"-"+sub_id
	 */
	private Map<String, PermissionSubject<K, ?>> permissionSubjectMap = new HashMap<String, PermissionSubject<K, ?>>();
	/**
	 * 权限客体缓存 key:ob_type+"-"+ob_id
	 */
	private Map<String, PermissionObject<K, ?>> permissionObjectMap = new HashMap<String, PermissionObject<K, ?>>();
	/**
	 * 允许关联关系表 key:sub_type+"-"+sub_id+"-"+ob_type+"-"+ob_id
	 */
	private Map<String, Bean> allowRelationMap = new HashMap<String, Bean>();

	/**
	 * 权限主体对应的所有权限客体信息 key:sub_type+"-"+sub_id 
	 */
	private Map<String, List<PermissionObject<K, ?>>> subject2Objects=new HashMap<String, List<PermissionObject<K,?>>>();
	/**
	 * 阻止关联关系表 key:sub_type+"-"+sub_id+"-"+ob_type+"-"+ob_id
	 */
	private Map<String, Bean> blockRelationMap = new HashMap<String, Bean>();

	public void addPermissionSubject(PermissionSubject<K, ?> permissionSubject) {
		String key = getKey(permissionSubject.getType(),
				permissionSubject.getId());
		permissionSubjectMap.put(key, permissionSubject);
	}

	public PermissionSubject<K, ?> getPermissionSubject(String sub_type,
			K sub_id) {
		String key = getKey(sub_type, sub_id);
		return permissionSubjectMap.get(key);
	}

	public void removePermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		permissionSubjectMap.remove(getKey(permissionSubject.getType(),
				permissionSubject.getId()));
	}

	public void addPermissionObject(PermissionObject<K, ?> permissionObject) {
		String key = getKey(permissionObject.getType(),
				permissionObject.getId());
		permissionObjectMap.put(key, permissionObject);
	}

	public PermissionObject<K, ?> getPermissionObject(String ob_type, K ob_id) {
		String key = getKey(ob_type, ob_id);
		return permissionObjectMap.get(key);
	}

	public void removePermissionObject(PermissionObject<K, ?> permissionObject) {
		permissionObjectMap.remove(getKey(permissionObject.getType(),
				permissionObject.getId()));
	}

	public void addAuthRelation(String status, String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId, Bean bean) {
		String key = getKey(permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (status.equals(DbModelPermissionManager.ALLOW)) {
			allowRelationMap.put(key, bean);
		} else {
			blockRelationMap.put(key, bean);
		}
		String subjectKey=getKey(permissionSubjectType, permissionSubjectId);
		List<PermissionObject<K, ?>> permissionObjects=subject2Objects.get(subjectKey);
		if(permissionObjects==null){
			permissionObjects=new ArrayList<PermissionObject<K,?>>();
			subject2Objects.put(subjectKey, permissionObjects);
		}
		PermissionObject<K, ?> permissionObject=permissionObjectMap.get(getKey(permissionObjectType, permissionObjectId));
		if(permissionObject!=null&&!permissionObjects.contains(permissionObject)){
			permissionObjects.add(permissionObject);
		}
	}

	public void removeAuthRelation(String status, String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		String key = getKey(permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (status.equals(DbModelPermissionManager.ALLOW)) {
			allowRelationMap.remove(key);
		} else {
			blockRelationMap.remove(key);
		}
	}

	public Bean getAuthRelation(String status, String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		String key = getKey(permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (status.equals(DbModelPermissionManager.ALLOW)) {
			return allowRelationMap.get(key);
		} else {
			return blockRelationMap.get(key);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<PermissionObject<K, ?>> getPermissionObjectsWithSujectKey(String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType){
	 	String subjectKey=getKey(subjectBeanType, permissionSubjectId);
		List<PermissionObject<K, ?>> permissionObjects=subject2Objects.get(subjectKey);
		List<PermissionObject<K, ?>> classPermissionObjects=null;
		if(permissionObjects!=null&&permissionObjects.size()>0){
			 classPermissionObjects=new ArrayList<PermissionObject<K,?>>();
			for (PermissionObject<K, ?> permissionObject : permissionObjects) {
			     if(permissionObject.getType().equals(objectBeanType)&&objectClassType.isInstance(permissionObject)){
			    	 classPermissionObjects.add(permissionObject);
			     }
			}
			
		}
		return permissionObjects;
		
	}
	

	private String getKey(String type, K id) {
		return type + "-" + id;
	}

	private String getKey(String sub_type, K sub_id, String ob_type, K ob_id) {
		return sub_type + "-" + sub_id + "-" + ob_type + "-" + ob_id;
	}

}
