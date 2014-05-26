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
package org.tinygroup.factory.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class BeansConfig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Beans beans = new Beans();
		List<Bean> beanList = new ArrayList<Bean>();
		Bean bean = new Bean();
		List<Property> properties = new ArrayList<Property>();
		Property property = new Property();
		property.setName("aa");
		property.setValue("aa");
		Ref ref = new Ref();
		ref.setId("aa");
		property.setRef(ref);
		properties.add(property);
		bean.setProperties(properties);
		bean.setId("124");
		bean.setScope("singleton");
		bean.setName("user");
		bean.setClassName(User.class.getName());
		beanList.add(bean);
		beans.setBeanList(beanList);
		// new JettisonMappedXmlDriver()
		XStream xStream = new XStream();
		xStream.autodetectAnnotations(true);
		xStream.processAnnotations(Beans.class);
		System.out.println(xStream.toXML(beans));
	}

}
