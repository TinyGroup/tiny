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
 * 查询权限信息，以树节点的结构数据返回
 * 
 * @author renhui
 * 
 */
public interface TreeNodeService<K extends Comparable<K>> {

	/**
	 * 查询子权限主体信息，返回的是parentSubjectId对应的权限主体，并且设置相关的子权限信息列表
	 * 
	 * @param subjectBeanType
	 * @param parentSubjectId
	 * @param subjectClassType
	 * @return
	 */
	PermissionSubject<K, ?> getChildPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType);

	/**
	 * 查询子孙权限主体信息，返回的是parentSubjectId对应的权限主体，并且递归设置相关的子权限信息列表
	 * 
	 * @param subjectBeanType
	 * @param parentSubjectId
	 * @param subjectClassType
	 * @return
	 */
	PermissionSubject<K, ?> getBegatsPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType);

	/**
	 * 查询子权限客体信息，返回的是parentSubjectId对应的权限客体，并且设置相关的子权限信息列表
	 * 
	 * @param objectBeanType
	 * @param parentObjectId
	 * @param objectClassType
	 * @return
	 */
	PermissionObject<K, ?> getChildPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType);

	/**
	 * 查询子孙权限客体信息，返回的是parentSubjectId对应的权限客体，并且设置相关的子权限信息列表
	 * 
	 * @param objectBeanType
	 * @param parentObjectId
	 * @param objectClassType
	 * @return
	 */
	PermissionObject<K, ?> getBegatsPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType);

}
