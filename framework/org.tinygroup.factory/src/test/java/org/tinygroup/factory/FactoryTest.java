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
package org.tinygroup.factory;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.tinygroup.factory.config.Bbb;
import org.tinygroup.factory.config.Beans;
import org.tinygroup.factory.config.Cat;
import org.tinygroup.factory.config.Ccc;
import org.tinygroup.factory.config.User;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class FactoryTest extends TestCase {
	static Factory factory = BeanFactory.getFactory();
	static {
		XStream xStream = XStreamFactory.getXStream();
		Beans beans=(Beans) xStream.fromXML(FactoryTest.class.getResourceAsStream("/Test.beans.xml"));
		factory.addBeans(beans);
		factory.init();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetBeanByName(){
		
	}
    public void testGetBeanByClass(){
		
	}
    public void testGetBeanByNameAndClass(){
		
   	}
	
    public void testContainsBean(){
    	
    }
    
    public void testGetType(){
    	
    }
    
    public void testCreateBean(){
    	
    }
    
    public void testAddBeans(){
    	
    }

    public void testRemoveBeans(){
    	
    }
	public void testGetParameterType() {
		try {
			Method[] methods = FactoryTest.class.getDeclaredMethods();
			for (Method method : methods) {
				System.out.println("method is: " + method.getName());
				String[] paraType = BeanFactory.getMethodParameterName(
						this.getClass(), method);
				if (paraType != null && paraType.length > 0) {
					for (String type : paraType) {
						System.out.println(type);
					}
				}
			}

		} catch (Exception e) {
				e.printStackTrace();
		}

	}

	public void testAutoAssemble() {
		assertNotNull(factory.getBean("aaa"));
		Bbb bbb = factory.getBean("bbb");
		assertEquals("aaa", bbb.getAaa().getName());
		bbb = factory.getBean("bbb");
		assertEquals("aaa", bbb.getAaa().getName());
		Ccc ccc = factory.getBean("ccc");
		assertEquals("aaa1", ccc.getAaa1().getName());
		ccc = factory.getBean("ccc");
		assertEquals("aaa1", ccc.getAaa1().getName());
	}

	public void testInitComparePrototype() {
		Cat cat = factory.getBean("cat1");
		Cat cat1 = factory.getBean("cat1");
		assertEquals(false, cat == cat1);
	}

	public void testInitComparePrototype1() {
		User user = factory.getBean("user2");
		User user2 = factory.getBean("user2");
		assertEquals(false, user == user2);
		assertEquals(false, user.getCatMap() == user2.getCat());
	}

	public void testInit() {
		User user = factory.getBean(User.class);
		if (user != null)
			System.out.println(XStreamFactory.getXStream().toXML(user));
		else {
			fail();
		}
		user = factory.getBean("user");
		if (user != null)
			System.out.println(XStreamFactory.getXStream().toXML(user));
		else {
			fail();
		}
		user = factory.getBean("user1");
		if (user != null)
			System.out.println(XStreamFactory.getXStream().toXML(user));
		else {
			fail();
		}
		user = factory.getBean("user2");
		if (user != null)
			System.out.println(XStreamFactory.getXStream().toXML(user));
		else {
			fail();
		}
	}

}
