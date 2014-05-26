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
package org.tinygroup.metadata.bizdatatype;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.metadata.config.PlaceholderValue;
import org.tinygroup.metadata.config.bizdatatype.BusinessType;
import org.tinygroup.metadata.config.bizdatatype.BusinessTypes;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class BizDataTypeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream stream = XStreamFactory.getXStream();
		stream.autodetectAnnotations(true);
		stream.processAnnotations(BusinessTypes.class);
		BusinessTypes businessTypes = new BusinessTypes();

		
		businessTypes.setName("base");
		businessTypes.setTitle("基础模块");
		businessTypes.setPackageName("aa.bb");
		List<BusinessType> businessTypeList=new ArrayList<BusinessType>();
		businessTypes.setBusinessTypeList(businessTypeList);
		BusinessType businessType=new BusinessType();;
		businessTypeList.add(businessType);
		
		businessType.setName("aa");
		businessType.setTitle("AA");
		businessType.setTypeId("hsvarcharid");
		List<PlaceholderValue> placeholderValueList=new ArrayList<PlaceholderValue>();
		businessType.setPlaceholderValueList(placeholderValueList);
		PlaceholderValue placeholderVaue=new PlaceholderValue();
		placeholderValueList.add(placeholderVaue);
		placeholderVaue.setName("l");
		placeholderVaue.setValue("112");
		
		System.out.println(stream.toXML(businessTypes));

	}

}
