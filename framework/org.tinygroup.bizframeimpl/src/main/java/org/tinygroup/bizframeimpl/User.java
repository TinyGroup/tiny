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
package org.tinygroup.bizframeimpl;

import org.tinygroup.bizframe.PermissionSubject;

/**
 * 用户列表，之所以实现Category接口，是为了让其具有上下级关系
 * 
 * @author luoguo
 * 
 */
public interface User extends PermissionSubject<String,User> {
	// List<Role> getRoleList();
	//
	// List<Position> getPositionList();
	//
	// List<Organization> getOrgazitionList();
	//
	// void setRoleList(List<Role> roleList);
	//
	// void setPositionList(List<Position> positionList);
	//
	// void setOrgazitionList(List<Organization> organizationList);
	
	String getUserId();
	
	void setUserId(String userId);
}
