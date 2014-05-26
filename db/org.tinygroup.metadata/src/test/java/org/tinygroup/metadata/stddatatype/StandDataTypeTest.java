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
package org.tinygroup.metadata.stddatatype;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.metadata.config.Placeholder;
import org.tinygroup.metadata.config.PlaceholderValue;
import org.tinygroup.metadata.config.stddatatype.DialectType;
import org.tinygroup.metadata.config.stddatatype.StandardType;
import org.tinygroup.metadata.config.stddatatype.StandardTypes;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class StandDataTypeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream stream = XStreamFactory.getXStream();
		stream.autodetectAnnotations(true);
		stream.processAnnotations(StandardTypes.class);

		StandardTypes standardTypes = new StandardTypes();
		standardTypes.setPackageName("aa.bb");
		List<StandardType> standardTypeList = new ArrayList<StandardType>();
		standardTypes.setStandardTypeList(standardTypeList);
		StandardType standardType = new StandardType();
		standardTypeList.add(standardType);
		standardType.setId("hsvarcharid");
		List<Placeholder> placeholderList = new ArrayList<Placeholder>();
		standardType.setPlaceholderList(placeholderList);
		Placeholder placeholder = new Placeholder();
		placeholder.setName("aa");
		placeholder.setTitle("长度");
		placeholder.setDescription("aa");
		placeholderList.add(placeholder);
		List<DialectType> dialectTypeList = new ArrayList<DialectType>();
		standardType.setDialectTypeList(dialectTypeList);

		DialectType dialectType = new DialectType();
		dialectType.setLanguage("oracle");
		dialectTypeList.add(dialectType);
		dialectType.setType("aa");
		dialectType.setDefaultValue("aa");
		List<PlaceholderValue> placeholderValueList = new ArrayList<PlaceholderValue>();
		dialectType.setPlaceholderValueList(placeholderValueList);
		PlaceholderValue placeholderValue = new PlaceholderValue();
		placeholderValueList.add(placeholderValue);
		placeholderValue.setName("aa");
		placeholderValue.setValue("12");

		System.out.println(stream.toXML(standardTypes));
	}

}
