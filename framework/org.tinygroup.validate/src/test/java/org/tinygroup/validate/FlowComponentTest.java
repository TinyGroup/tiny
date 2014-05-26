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

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.springutil.SpringUtil;

/**
 * 流程组件测试
 * @author renhui
 *
 */
public class FlowComponentTest extends AbstractValidatorManagerTest {
	
	private FlowExecutor flowExecutor;
	
	protected void setUp() throws Exception {
		super.setUp();
		validatorManager=SpringUtil.getBean(ValidatorManager.VALIDATOR_MANAGER_BEAN_NAME);
		flowExecutor=SpringUtil.getBean(FlowExecutor.FLOW_BEAN);
	}
	
	
	
	public void testFlowComponent(){
		User user=createUser();
		Context context=new ContextImpl();
		context.putSubContext("validate", new ContextImpl());
		context.put("userdemo", user);
		flowExecutor.execute("validateFlow", "validateNode", context);
	    System.out.println(context.get("validate","userdemo.validateMessage"));
	}



	private User createUser() {
		User user=new User();
		user.setAge(10);
		user.setEmail("renhui@hundsun.com");
		user.setName("renhui");
		Address address=new Address();
		address.setName("武林门新村");
//		address.setUrl("http://www.sina.com");
		user.setAddress(address);
		Address[] addressArray=new Address[5];
		addressArray[0]=address;
		user.setAddressArray(addressArray);
		List<String> strList=new ArrayList<String>();
		strList.add("str1");
		user.setStrList(strList);
		List<Address> addresses=new ArrayList<Address>();
		addresses.add(address);
		user.setAdds(addresses);
		Set<Address> addressSet=new HashSet<Address>();
		addressSet.add(address);
	    user.setAddressSet(addressSet);
	    Map<String,Address> addressMap=new HashMap<String, Address>();
	    addressMap.put("hangzhou", address);
	    user.setAddressMap(addressMap);
	    return user;
	}

}
