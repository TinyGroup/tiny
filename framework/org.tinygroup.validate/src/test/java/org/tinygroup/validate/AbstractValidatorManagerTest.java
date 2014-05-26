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
package org.tinygroup.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.impl.ValidateResultImpl;

public abstract class AbstractValidatorManagerTest extends TestCase {

	protected ValidatorManager validatorManager;

	void init() {
		AbstractTestUtil.init("application.xml",true);
	}

	
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	public void testValidator() {
		User user = new User();
		user.setAge(10);
		user.setEmail("renhui@hundsun.com");
		user.setName("renhui");
		Address address = new Address();
		address.setName("武林门新村");
		// address.setUrl("http://www.sina.com");
		user.setAddress(address);
		Address[] addressArray = new Address[5];
		addressArray[0] = address;
		user.setAddressArray(addressArray);
		List<String> strList = new ArrayList<String>();
		strList.add("str1");
		user.setStrList(strList);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		user.setAdds(addresses);
		Set<Address> addressSet = new HashSet<Address>();
		addressSet.add(address);
		user.setAddressSet(addressSet);
		Map<String, Address> addressMap = new HashMap<String, Address>();
		addressMap.put("hangzhou", address);
		user.setAddressMap(addressMap);
		ValidateResult result = new ValidateResultImpl();
		validatorManager.validate(user, result);
//		for (ErrorDescription description : result.getErrorList()) {
//			System.out.println(description.getDescription());
//			System.out.println(description.getFieldName());
//		}
	}
}
