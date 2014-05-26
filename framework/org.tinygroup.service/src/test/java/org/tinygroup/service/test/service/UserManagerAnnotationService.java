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
package org.tinygroup.service.test.service;

import java.util.List;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.annotation.ServiceComponent;
import org.tinygroup.service.annotation.ServiceMethod;
import org.tinygroup.service.annotation.ServiceParameter;
import org.tinygroup.service.annotation.ServiceResult;
import org.tinygroup.service.test.base.ServiceUser;

@ServiceComponent()
public class UserManagerAnnotationService {

	private static Logger logger = LoggerFactory
			.getLogger(UserManagerAnnotationService.class);

	@ServiceMethod(serviceId = "serviceAddServiceUserAnnotation")
	@ServiceResult(name = "user2")
	public ServiceUser addServiceUser(
			@ServiceParameter(name = "user",required=false) ServiceUser user) {
		ServiceUser user2 = user;
		if (user2 == null) {
			user2 = new ServiceUser();
			user2.setName("user2");
			String info1 = String.format("参数未传递,自行创建新用户%s", user2.getName());
			String info2 = String.format("添加了一名新用户%s(对象添加)", user2.getName());
			logger.logMessage(LogLevel.INFO, info1);
			logger.logMessage(LogLevel.INFO, info2);
		} else {
			String info = String.format("添加了一名新用户%s(对象添加)", user2.getName());
			logger.logMessage(LogLevel.INFO, info);
		}
		return user2;
	}
	@ServiceMethod( serviceId = "serviceAddServiceUserListAnnotation")
	@ServiceResult(name = "userList")
	public List<ServiceUser> addServiceUserList(@ServiceParameter(name="users") List<ServiceUser> users){
		return users;
	}
	

	@ServiceMethod( serviceId = "serviceAddServiceUserByNameAnnotation")
	@ServiceResult(name = "user2")
	public ServiceUser addServiceUser(
			@ServiceParameter(name = "name") String name) {
		String info = String.format("添加了一名新用户%s(名称添加)", name);
		logger.logMessage(LogLevel.INFO, "", info);
		ServiceUser user2 = new ServiceUser();
		user2.setName(name);
		return user2;
	}
	
	@ServiceMethod()
	@ServiceResult()
	public ServiceUser addServiceUserNoResultName(
			@ServiceParameter(name = "name") String name) {
		String info = String.format("添加了一名新用户%s(名称添加)", name);
		logger.logMessage(LogLevel.INFO, "", info);
		ServiceUser user2 = new ServiceUser();
		user2.setName(name);
		return user2;
	}
	
	
	public int getServiceUserAge(String name) {
		String info = String.format("查询用户%s的年龄", name);
		logger.logMessage(LogLevel.INFO, info);
		return 5;
	}
	
	@ServiceMethod( serviceId = "serviceSetServiceUserAgeAnnotation")
	@ServiceResult(name = "i")
	public int setServiceUserAge(
			@ServiceParameter(name = "name")String name, 
			@ServiceParameter(name = "age")Integer i) {
		String info = String.format("设置用户%s的年龄为%d(Integer调用)", name, i);
		logger.logMessage(LogLevel.INFO, info);
		return i;
	}
	@ServiceMethod( serviceId = "serviceSetServiceUserAgeIntAnnotation")
	public void setServiceUserAge(
			@ServiceParameter(name = "name")String name, 
			@ServiceParameter(name = "age")int i) {
		String info = String.format("设置用户%s的年龄为%d(Integer调用)", name, i);
		logger.logMessage(LogLevel.INFO, info);
	}
	@ServiceMethod( serviceId = "serviceSetServiceUserAgeArrayAnnotation")
	@ServiceResult(name = "length")
	public int setServiceUserAge(
			@ServiceParameter(name = "names")String[] name, 
			@ServiceParameter(name = "ages")int[] i) {

		String info = String.format("设置用户%s的年龄为%d(Integer调用)", name[0], i[0]);
		logger.logMessage(LogLevel.INFO, info);
		return name.length;
	}
}
