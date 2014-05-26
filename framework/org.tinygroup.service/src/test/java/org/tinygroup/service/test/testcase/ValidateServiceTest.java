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
package org.tinygroup.service.test.testcase;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.exception.ServiceParamValidateException;
import org.tinygroup.service.test.validate.ValidateAddress;
import org.tinygroup.service.test.validate.ValidateUser;
import org.tinygroup.service.util.ServiceTestUtil;
import org.tinygroup.validate.ErrorDescription;
import org.tinygroup.validate.ValidateResult;

public class ValidateServiceTest extends TestCase {
	public void testValidateFailed1() {
		ValidateUser user = getSuccessUser();
		ValidateUser user1 = getFailedUser();
		user.setMate(user1);
		user1.setMate(user);
		ValidateAddress address = getSuccessAddress();
		user.setAddress(address);
		user1.setAddress(address);
		int a = getSuccessInt();
		Context context = new ContextImpl();
		context.put("validateUser", user);
		context.put("string", "validate test");
		context.put("a", a); // 10-2000
		try {
			ServiceTestUtil.executeForValidate("validateServiceAdd", context);
//			assertTrue(false);
		} catch (Exception e) {
			if (e instanceof ServiceParamValidateException) {
				ValidateResult result = ((ServiceParamValidateException) e)
						.getResult();
				if (result == null) {
					System.out
							.println("Validate Failed,But Result IS NULL !!!!!");
					assertTrue(false);
				} else {
					assertTrue(true);
					print(result);
				}
			}
		}

	}

	public void testValidateFailed2() {

		ValidateUser user = getSuccessUser();
		ValidateUser user1 = getSuccessUser();
		user.setMate(user1);
		user1.setMate(user);
		ValidateAddress address = getFailedAddress();
		user.setAddress(address);
		user1.setAddress(address);
		int a = getSuccessInt();

		Context context = new ContextImpl();
		context.put("validateUser", user);
		context.put("string", "validate test");
		context.put("a", a); // 10-2000
		try {
			ServiceTestUtil.executeForValidate("validateServiceAdd", context);
//			assertTrue(false);
		} catch (Exception e) {
			if (e instanceof ServiceParamValidateException) {
				ValidateResult result = ((ServiceParamValidateException) e)
						.getResult();
				if (result == null) {
					System.out
							.println("Validate Failed,But Result IS NULL !!!!!");
					assertTrue(false);
				} else {
					assertTrue(true);
					print(result);
				}
			}
		}

	}

	public void testValidateFailed3() {

		ValidateUser user = getSuccessUser();
		ValidateUser user1 = getSuccessUser();
		user.setMate(user1);
		user1.setMate(user);
		ValidateAddress address = getSuccessAddress();
		user.setAddress(address);
		user1.setAddress(address);
		int a = getFailedInt();

		Context context = new ContextImpl();
		context.put("validateUser", user);
		context.put("string", "validate test");
		context.put("a", a); // 10-2000
		try {
			ServiceTestUtil.executeForValidate("validateServiceAdd", context);
//			assertTrue(false);
		} catch (Exception e) {
			if (e instanceof ServiceParamValidateException) {
				ValidateResult result = ((ServiceParamValidateException) e)
						.getResult();
				if (result == null) {
					System.out
							.println("Validate Failed,But Result IS NULL !!!!!");
					assertTrue(false);
				} else {
					assertTrue(true);
					print(result);
				}
			}
		}

	}
	
	public void testValidateSucess() {

		ValidateUser user = getSuccessUser();
		ValidateUser user1 = getSuccessUser();
		user.setMate(user1);
		user1.setMate(user);
		ValidateAddress address = getSuccessAddress();
		user.setAddress(address);
		user1.setAddress(address);
		int a = getSuccessInt();

		Context context = new ContextImpl();
		context.put("validateUser", user);
		context.put("string", "validate test");
		context.put("a", a); // 10-2000
		try {
			ServiceTestUtil.executeForValidate("validateServiceAdd", context);
//			assertTrue(true);
		} catch (Exception e) {
			if (e instanceof ServiceParamValidateException) {
				ValidateResult result = ((ServiceParamValidateException) e)
						.getResult();
				if (result == null) {
					System.out
							.println("Validate Failed,But Result IS NULL !!!!!");
					assertTrue(false);
				} else {
					assertTrue(false);
					print(result);
				}
			}
		}

	}


	private void print(ValidateResult result) {
		System.out.println("==========begin==============");
		for (ErrorDescription e : result.getErrorList()) {
			System.out.println(e.toString());
		}
		System.out.println("==========end==============");
	}

	private ValidateUser getFailedUser() {
		ValidateUser user = new ValidateUser();
		user.setName("user"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(10); // china 0-6
		return user;
	}

	private ValidateUser getSuccessUser() {
		ValidateUser user = new ValidateUser();
		user.setName("user12345678"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(5); // china 0-6
		return user;
	}

	private ValidateAddress getFailedAddress() {
		ValidateAddress address = new ValidateAddress();
		address.setNumber(2500); // china 0-2000 japan 0-1000
		address.setStreet("street"); // 10-20
		return address;
	}

	private ValidateAddress getSuccessAddress() {
		ValidateAddress address = new ValidateAddress();
		address.setNumber(1500); // china 0-2000 japan 0-1000
		address.setStreet("street123456"); // 10-20
		return address;
	}

	private int getFailedInt() {
		return 5;
	}

	private int getSuccessInt() {
		return 20;
	}
}
