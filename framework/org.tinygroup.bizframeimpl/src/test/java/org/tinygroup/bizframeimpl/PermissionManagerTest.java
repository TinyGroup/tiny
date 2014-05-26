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

import java.nio.charset.Charset;
import java.sql.Connection;

import junit.framework.TestCase;

import org.tinygroup.bizframe.PermissionManager;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.util.DataSourceFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.tinytestutil.script.Resources;
import org.tinygroup.tinytestutil.script.ScriptRunner;

public class PermissionManagerTest extends TestCase {

	private static boolean hasExcuted = false;

	private PermissionManager manager;

	private User user;

	private Function function;

	
	protected void setUp() throws Exception {
		super.setUp();
		if (!hasExcuted) {
			Connection conn = null;
			try {
				AbstractTestUtil.init(null, true);
				conn = DataSourceFactory.getConnection("dynamicDataSource");
				ScriptRunner runner = new ScriptRunner(conn, false, false);
				Resources.setCharset(Charset.forName("utf-8"));
				// 加载sql脚本并执行
				try {
					runner.runScript(Resources
							.getResourceAsReader("table_derby.sql"));
				} catch (Exception e) {
					// e.printStackTrace();
				}
				hasExcuted = true;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		}
		manager = SpringUtil.getBean("dbPermissionManager");
		user = getUser();
		function = getFunction();

	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
		manager.removePermissionSubject(user);
		manager.removePermissionObject(function);
	}

	private User getUser() {
		User user = new UserImpl();
		user.setName("luog");
		user.setOrder(0);
		user.setTitle("aff");
		user.setDescription("users");
		user.setUserId("user1");
		return user;
	}

	private Function getFunction() {
		Function function = new FunctionImpl();
		function.setName("权限管理");
		function.setOrder(0);
		function.setTitle("aff");
		function.setDescription("权限管理菜单");
		function.setFunId("funId1");
		return function;
	}

	public void testAddPermissionSubject() {
		User addUser = (User) manager.addPermissionSubject(user);
		addUser = (User) manager.getPermissionSubject("TUser", addUser.getId(),
				UserImpl.class);
		assertEquals(user.getName(), addUser.getName());
		assertEquals(user.getTitle(), addUser.getTitle());

	}

	public void testAddPermissionObject() {
		Function addFunction = (Function) manager.addPermissionObject(function);
		addFunction = (Function) manager.getPermissionObject("TFunction",
				addFunction.getId(), FunctionImpl.class);
		assertEquals(function.getName(), addFunction.getName());
		assertEquals(function.getTitle(), addFunction.getTitle());
	}

	public void testAllowPermission() {
		User user = (User) manager.addPermissionSubject(this.user);
		Function function = (Function) manager
				.addPermissionObject(this.function);
		manager.addAllowPermission(user, function);
		assertEquals(true, manager.isAllow(user, function));
		manager.removeAllowPermission(user, function);
		assertEquals(false, manager.isAllow(user, function));
	}

	public void testBlockPermission() {
		User user = (User) manager.addPermissionSubject(this.user);
		Function function = (Function) manager
				.addPermissionObject(this.function);
		manager.addBlockPermission(user, function);
		assertEquals(true, manager.isBlock(user, function));
		manager.removeBlockPermission(user, function);
		assertEquals(false, manager.isBlock(user, function));
	}

}
