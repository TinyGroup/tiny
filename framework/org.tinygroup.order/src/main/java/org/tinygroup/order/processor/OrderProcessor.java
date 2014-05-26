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
package org.tinygroup.order.processor;

import static org.tinygroup.logger.LogLevel.INFO;

import java.io.BufferedInputStream;
import java.util.List;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * 
 * 功能说明: 解析对象顺序文件，并对对象列表进行排序 

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-17 <br>
 * <br>
 */
public class OrderProcessor<T> {
	
	public static final String ORDER_NAME="orderProcessor";

	private static Logger logger = LoggerFactory
			.getLogger(OrderProcessor.class);
	
	private List<OrderGroups<T>> groupList=CollectionUtil.createArrayList();
	
	private List<String> fileNameCaches=CollectionUtil.createArrayList();

	public List<T> orderList(String name, List<T> list) {
		for (OrderGroups<T> groups : groupList) {
			OrderGroup<T> group = groups.get(name);
			if (group != null) {
				return group.initProcessorOrder(list);
			}
		}
		throw new RuntimeException(String.format(
				"can not found ordergroup with name of %s", name));

	}

	public void loadOrderFile(FileObject fileObject){
		 Assert.assertNotNull(fileObject, "fileObject must not null");
		 String filePath=fileObject.getAbsolutePath();
		 if(!fileNameCaches.contains(filePath)){
		 logger.logMessage(INFO, "读取对象加载顺序配置文件:<{}>开始",filePath);
		 BufferedInputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(fileObject.getInputStream());
				byte[] bytes = new byte[inputStream.available()];
				inputStream.read(bytes);
				inputStream.close();
				String content = new String(bytes, "UTF-8");
				logger.logMessage(INFO, "读取对象加载顺序配置文件：<{}>",
						filePath);
				XmlNode root = new XmlStringParser().parse(content)
						.getRoot();
				OrderGroups<T> groups=new OrderGroups<T>(root);
				groups.load();
				groupList.add(groups);
			} catch (Exception e) {
				logger.errorMessage("读取对象加载顺序配置文件：<{}>", e,
						filePath);
				throw new RuntimeException(e);
			}
			fileNameCaches.add(filePath);
		 }
	}
}
