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
package org.tinygroup.validate.test.scene.testcase;

import junit.framework.TestCase;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.ErrorDescription;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.XmlValidatorManager;
import org.tinygroup.validate.impl.ValidateResultImpl;
import org.tinygroup.validate.test.scene.SceneAddress;
import org.tinygroup.validate.test.scene.SceneUser;

public class TestScene extends TestCase {

	protected ValidatorManager validatorManager;

	private void init() {
		AbstractTestUtil.init(null, true);
	}

	protected void setUp() throws Exception {
		super.setUp();
		init();
		validatorManager = SpringUtil
				.getBean(XmlValidatorManager.VALIDATOR_MANAGER_BEAN_NAME);
	}

	private SceneUser getChinaSucessUser() {
		SceneUser user = new SceneUser();
		user.setName("user12345678"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(5); // china 0-6
		return user;
	}

	private SceneUser getChinaFailedUser() {
		SceneUser user = new SceneUser();
		user.setName("user12345678"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(5); // china 0-6
		return user;
	}

	private SceneAddress getChinaSucessAddress() {
		SceneAddress address = new SceneAddress();
		address.setNumber(1500); // china 0-2000 japan 0-1000
		address.setStreet("street123456"); // 10-20

		return address;
	}

	private SceneAddress getChinaFailedAddress() {
		SceneAddress address = new SceneAddress();
		address.setNumber(15000); // china 0-2000 japan 0-1000
		address.setStreet("street"); // 10-20
		return address;
	}

	public void testChinaSucessSceneUser() {
		SceneUser user = getChinaSucessUser();
		SceneUser user1 = getChinaSucessUser();
		SceneAddress address = getChinaSucessAddress();
		user1.setAddress(address);
		user.setAddress(address);

		// ValidateResult result = validatorSceneUser("",user);
		// ValidateResult result = validatorSceneUser("japan", user);
		ValidateResult result = validatorSceneUser("china", user);
		printResult("china", result);
		if (result.hasError()) {
			assertFalse(false);
		} else {
			assertTrue(true);
		}

	}

	public void testNull() {
		ValidateResult result = new ValidateResultImpl();
		validatorManager.validate(null, result);
		printResult("null test", result);
	}

	public void testBasic() {
		ValidateResult result = new ValidateResultImpl();
		validatorManager.validate("buyNum", 1, result);
		printResult("buyNum", result);

		ValidateResult result1 = new ValidateResultImpl();
		validatorManager.validate(1, result1);
		printResult("", result1);

		ValidateResult result2 = new ValidateResultImpl();
		validatorManager.validate("buyNum2", 1, result2);
		printResult("buyNum2", result2);
	}

	private ValidateResult validatorSceneUser(String scene, SceneUser user) {
		ValidateResult result = new ValidateResultImpl();
		validatorManager.validate(scene, user, result);
		return result;
	}

	private void printResult(String scene, ValidateResult result) {
		System.out.println("=============" + scene + "====================");
		for (ErrorDescription d : result.getErrorList()) {
			System.out.println("Field:" + d.getFieldName());
			System.out.println("Error:" + d.getDescription());
		}
		System.out.println("=============" + scene + "====================");
	}

}
