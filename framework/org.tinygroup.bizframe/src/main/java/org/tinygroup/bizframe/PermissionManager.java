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

import java.util.List;

/**
 * 权限管理器
 * 
 * @author luoguo
 * 
 */
@SuppressWarnings("rawtypes")
public interface PermissionManager<K extends Comparable<K>> extends TreeNodeService<K> {
	/**
	 * 设置权限主体检查器列表
	 * 
	 * @param permissionSubjectCheckerList
	 */
	void setPermissionSubjectCheckerList(
			List<PermissionSubjectChecker<K>> permissionSubjectCheckerList);

	/**
	 * 设置权限客体检查器列表
	 * 
	 * @param permissionSubjectCheckerList
	 */
	void setPermissionObjectCheckerList(
			List<PermissionObjectChecker<K>> permissionObjectCheckerList);

	/**
	 * 检查sourceObject是否属于destObject
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean belongTo(PermissionSubject<K, ?> sourceObject,
			PermissionSubject<K, ?> destObject);

	/**
	 * 返回sourceObject是否包含了destObject
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean contains(PermissionSubject<K, ?> sourceObject,
			PermissionSubject<K, ?> destObject);

	/**
	 * 检查sourceObject是否属于destObject
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean belongTo(PermissionObject<K, ?> sourceObject,
			PermissionObject<K, ?> destObject);

	/**
	 * 返回sourceObject是否包含了destObject
	 * 
	 * @param sourceObject
	 * @param destObject
	 * @return
	 */
	boolean contains(PermissionObject<K, ?> sourceObject,
			PermissionObject<K, ?> destObject);

	/**
	 * 是否支持缓冲
	 * 
	 * @return
	 */
	boolean isCacheSupport();

	/**
	 * 设置是否支持缓冲
	 * 
	 * @param support
	 */
	void setCacheSupport(boolean support);

	/**
	 * 权限客体是否支持继承
	 * 
	 * @return
	 */
	boolean isPermissionObjectInheritSupport();

	/**
	 * 设置权限客体是否支持继承
	 * 
	 * @param support
	 */
	void setPermissionObjectInheritSupport(boolean support);

	/**
	 * 权限主体是否支持继承
	 * 
	 * @return
	 */

	boolean isPermissionSubjectInheritSupport();

	/**
	 * 设置权限主体是否支持继承
	 * 
	 * @param support
	 */
	void setPermissionSubjectInheritSupport(boolean support);

	/**
	 * 设置权限策略列表
	 * 
	 * @param permissionStrategyList
	 */
	void setPermissionStrategy(PermissionCheckStrategy<K> permissionStrategy);

	/**
	 * 添加权限主体
	 * 
	 * @param permissionSubject
	 * @return 
	 */
	PermissionSubject addPermissionSubject(
			PermissionSubject<K, ?> permissionSubject);

	/**
	 * 删除权限客体
	 * 
	 * @param permissionObject
	 */
	void removePermissionObject(PermissionObject<K, ?> permissionObject);

	/**
	 * 删除权限主体
	 * 
	 * @param permissionSubject
	 */
	void removePermissionSubject(PermissionSubject<K, ?> permissionSubject);

	/**
	 * 添加权限客体
	 * 
	 * @param permissionObject
	 * @return 
	 */
	PermissionObject addPermissionObject(PermissionObject<K, ?> permissionObject);

	/**
	 * 是否禁止
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isBlock(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否允许
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isAllow(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否禁止
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isBlock(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId);

	/**
	 * 是否允许
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isAllow(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId);

	/**
	 * 是否直接禁止
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isBlockDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否直接允许
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isAllowDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否直接禁止
	 * 
	 * @return
	 */
	boolean isBlockDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 是否直接允许
	 * 
	 * @return
	 */
	boolean isAllowDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 给权限主体添加权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addAllowPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 给一组权限主体添加一组权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList);

	/**
	 * 
	 * 给权限主体删除权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeAllowPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 给一组权限主体删除一组权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList);

	/**
	 * 给权限主体添加权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 
	 * 给权限主体删除权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 给权限主体添加权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addBlockPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 给一组权限主体添加一组权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList);

	/**
	 * 
	 * 给权限主体删除权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeBlockPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 给一组权限主体删除一组权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList);

	/**
	 * 给权限主体添加权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void addBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 
	 * 给权限主体删除权限客体
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 */
	void removeBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId);

	/**
	 * 
	 * 根据主键值查询权限主体
	 * 
	 * @param subjectBeanType
	 *            权限主体bean类型
	 * @param keyValue
	 *            主键值
	 * @return
	 */
	PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue, Class<? extends PermissionSubject> subjectClassType);

	/**
	 * 
	 * 根据主键值查询权限客体
	 * 
	 * @param objectBeanType
	 *            权限客体bean类型
	 * @param keyValue
	 *            主键值
	 * @return
	 */
	PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue, Class<? extends PermissionObject> objectClassType);
	
	/**
	 * 
	 * 查询权限主体类型定义的所有权限主体列表
	 * @param subjectBeanType
	 * @param subjectClassType
	 * @return
	 */
	List<PermissionSubject<K,?>> getPermissionSubjects(String subjectBeanType,Class<? extends PermissionSubject> subjectClassType);
	
	/**
	 * 
	 * 查询权限客体类型定义的所有权限客体列表
	 * @param objectBeanType
	 * @param objectClassType
	 * @return
	 */
	List<PermissionObject<K,?>> getPermissionObjects(String objectBeanType,Class<? extends PermissionObject> objectClassType);
	
	/**
	 * 
	 * 查询某权限主体已分配的所有权限客体信息
	 * @param subjectBeanType
	 * @param objectBeanType
	 * @param permissionSubjectId
	 * @param objectClassType
	 * @return
	 */
	List<PermissionObject<K,?>> getAssignedPermission(String subjectBeanType,String objectBeanType,K permissionSubjectId,Class<? extends PermissionObject> objectClassType);
	/**
	 * 
	 * 获取某权限主体相关的所有下级权限主体
	 * @param subjectBeanType
	 * @param parentSubjectId
	 * @param subjectClassType
	 * @return
	 */
	List<PermissionSubject<K,?>> getChildPermissionSubjects(String subjectBeanType,K parentSubjectId,Class<? extends PermissionSubject> subjectClassType);
	/**
	 * 
	 * 获取某权限主客体相关的所有下级权限客体
	 * @param objectBeanType
	 * @param parentObjectId
	 * @param objectClassType
	 * @return
	 */
	List<PermissionObject<K, ?>> getChildPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType);

	/**
	 * 获取某权限主体相关的所有子孙级权限主体
	 * @param subjectBeanType
	 * @param parentSubjectId
	 * @param subjectClassType
	 * @return
	 */
	List<PermissionSubject<K,?>> getBegatsPermissionSubjects(String subjectBeanType,K parentSubjectId,Class<? extends PermissionSubject> subjectClassType);
	
	
	/**
	 * 
	 * 获取某权限主客体相关的所有子孙级权限客体
	 * @param objectBeanType
	 * @param parentObjectId
	 * @param objectClassType
	 * @return
	 */
	List<PermissionObject<K,?>> getBegatsPermissionObjects(String objectBeanType,K parentObjectId,Class<? extends PermissionObject> objectClassType);

}
