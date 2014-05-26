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
package test;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		User1 user1 = new User1();
		user1.setName("abc");
		User user = new User();
		user.setName("abc");
		
//		
//		JSonEvent event =new JSonEvent();
//		event.parameterMap.put("user",user);
//		event.parameterMap.put("user1",user1);
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
//		xstream.alias("jsonEvent", event.getClass());
		xstream.alias("user", User.class);
		xstream.alias("user1", User1.class);

		System.out.println(xstream.toXML(user));
		User user2 = (User)xstream.fromXML("{\"user\":{\"name\":\"abc\"}}");
		System.out.println(user2.getName());
		
	}
	

}
