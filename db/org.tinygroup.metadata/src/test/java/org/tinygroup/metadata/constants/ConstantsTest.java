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
package org.tinygroup.metadata.constants;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.metadata.config.constants.Constant;
import org.tinygroup.metadata.config.constants.Constants;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ConstantsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Constants constants = new Constants();
		constants.setPackageName("aa.bb");
		List<Constant> constantList = new ArrayList<Constant>();
		constants.setConstantList(constantList);
		Constant constant = new Constant();
		constant.setName("aa");
		constant.setTitle("AA");
		constant.setDescription("aa");
		constant.setValue("aa");
		constantList.add(constant);
		XStream stream = XStreamFactory.getXStream();
		stream.autodetectAnnotations(true);
		stream.processAnnotations(Constants.class);
		System.out.println(stream.toXML(constants));
	}

}
