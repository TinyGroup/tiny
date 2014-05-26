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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.bizframe.PermissionManager;
import org.tinygroup.bizframe.impl.PermissionManagerImpl;
import org.tinygroup.bizframe.impl.PermissionStrategyAllowOnly;
import org.tinygroup.container.Category;

public class Test {
	public static void main(String[] args) {
		User user = new UserImpl();
		user.setId("luog");
		User user1 = new UserImpl();
		user1.setId("renhui");
		List<Category<String, User>> subList = new ArrayList<Category<String, User>>();
		subList.add(user1);
		user.setSubList(subList);
		System.out.println(user.containsSub(user1));
		System.out.println(user1.belong(user));
		System.out.println(user.containsDirectly(user1));
		System.out.println(user1.belongDirectly(user));
		PermissionManager pm = new PermissionManagerImpl();
		pm.setPermissionStrategy(new PermissionStrategyAllowOnly());
		Function function = new FunctionImpl();
		function.setId("function1");
		pm.addAllowPermission(user, function);
		System.out.println("user:function-" + pm.isAllow(user, function));
		System.out.println("user1:function-" + pm.isAllow(user1, function));
	}
}
