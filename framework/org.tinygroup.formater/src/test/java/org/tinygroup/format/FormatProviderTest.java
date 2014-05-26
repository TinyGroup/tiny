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
package org.tinygroup.format;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.format.Formater;
import org.tinygroup.format.exception.FormatException;
import org.tinygroup.format.impl.DefaultPatternDefine;

;
public class FormatProviderTest extends TestCase {
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"springbeans.xml");
	Context context = new ContextImpl();
	Formater formater = (Formater) applicationContext.getBean("formater");

	protected void setUp() throws Exception {
		super.setUp();
		context.clear();
	}

	/**
	 * 测试不存在任何标记情况
	 * 
	 * @throws FormatException
	 */
	public void testFormatNotPlaceholder() throws FormatException {
		assertEquals("this is test", formater.format(context, "this is test"));
	}

	/**
	 * 测试存在标记，且有处理提供者处理的情况
	 * 
	 * @throws FormatException
	 */
	public void testFormatExistPlaceholderProvider() throws FormatException {
		Context context = new ContextImpl();
		assertEquals("this is v1 test",
				formater.format(context, "this is ${const:1} test"));
	}

	/**
	 * 测试存在标记，且没有处理提供者处理的情况
	 * 
	 * @throws FormatException
	 */

	public void testFormatExistPlaceholderNoProvider() throws FormatException {
		assertEquals("this is ${abc:2} test",
				formater.format(context, "this is ${abc:2} test"));
	}

	/**
	 * 测试存在标记，且是bean的情况
	 * 
	 * @throws FormatException
	 */

	public void testFormatBean() throws FormatException {
		User user = new User("aa", 123);
		context.put("user", user);
		assertEquals("this is aa test 123",
				formater.format(context, "this is ${context:user.name} test ${context:user.age}"));

	}
	
	public void testFormatPattern(){
		
		PatternDefine define=new  DefaultPatternDefine();
		define.setPrefixPatternString("%{");
		define.setPostfixPatternString("}");
		define.setPatternString("([%]+[{]+[a-zA-Z0-9[.[_[:[/[#]]]]]]+[}])");
		formater.setPatternHandle(define);
		assertEquals("this is v1 test v2",
				formater.format(context, "this is %{const:1} test %{const:2}"));
	}
}
