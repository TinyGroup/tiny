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
package org.tinygroup.cepcore.test.aop.testcase;

import junit.framework.TestCase;

import org.tinygroup.cepcore.exception.RequestParamValidateException;
import org.tinygroup.cepcore.test.aop.bean.AopValidateAddress;
import org.tinygroup.cepcore.test.aop.bean.AopValidateUser;
import org.tinygroup.cepcore.test.aop.util.AopTestUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.validate.ErrorDescription;

public class TestValidateParam extends TestCase{
	private static Logger logger = LoggerFactory.getLogger(TestValidateParam.class);
	public void testValidateParam(){
		Event event = new Event();
		ServiceRequest request = new ServiceRequest();
		
		AopValidateUser user = getSuccessUser();
		AopValidateUser user1 = getFailedUser();
		user.setMate(user1);
		user1.setMate(user);
		AopValidateAddress address = getSuccessAddress();
		user.setAddress(address);
		user1.setAddress(address);
		int a = getSuccessInt();
		Context context = new ContextImpl();
		context.put("validateUser", user);
		context.put("p2", "validate test");
		context.put("p3", a); // 10-2000
		
		event.setServiceRequest(request);
		request.setContext(context);
		request.setServiceId("aoptest");
		try {
			AopTestUtil.execute(event);
		} catch (Exception e) {
			System.out.println(e);
			if(e instanceof RequestParamValidateException){
				RequestParamValidateException rpe = (RequestParamValidateException)e;
				logger.logMessage(LogLevel.INFO,rpe.getMessage());
				for(ErrorDescription ed:rpe.getResult().getErrorList()){
					logger.logMessage(LogLevel.INFO,ed.toString());
				}
				assertTrue(true);
			}
		}
		
	}
	
	
	private AopValidateUser getFailedUser() {
		AopValidateUser user = new AopValidateUser();
		user.setName("user"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(10); // china 0-6
		return user;
	}

	private AopValidateUser getSuccessUser() {
		AopValidateUser user = new AopValidateUser();
		user.setName("user12345678"); // 10-20,非空
		user.setAge(60);// china 22-200 japan 18-200
		user.setGrade(5); // china 0-6
		return user;
	}

	private AopValidateAddress getFailedAddress() {
		AopValidateAddress address = new AopValidateAddress();
		address.setNumber(2500); // china 0-2000 japan 0-1000
		address.setStreet("street"); // 10-20
		return address;
	}

	private AopValidateAddress getSuccessAddress() {
		AopValidateAddress address = new AopValidateAddress();
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
