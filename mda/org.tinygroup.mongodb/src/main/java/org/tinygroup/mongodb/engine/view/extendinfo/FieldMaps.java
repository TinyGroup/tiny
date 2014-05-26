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
package org.tinygroup.mongodb.engine.view.extendinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.BSONObject;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.context.Context;
import org.tinygroup.mongodb.engine.view.MongoViewContext;
import org.tinygroup.mongodb.util.ModelUtil;
import org.tinygroup.parser.Document;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlParser;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * 
 * 功能说明: 扩展信息中的<field-maps>节点描述对象
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-17 <br>
 * <br>
 */
public class FieldMaps {

	private static final String FIELD_MAPS = "field-maps";
	private static final String FIELD_MAP = "field-map";
	private List<FieldMap> fieldMaps = new ArrayList<FieldMap>();

	private Context context;
	
	private MongoViewContext viewContext;

	public FieldMaps(MongoViewContext viewContext, Context context) {
		this.context = context;
		this.viewContext=viewContext;
	}

	public void parserExtendInfos(String extendInfos) {
		XmlParser<String> parser = new XmlStringParser();
		Document<XmlNode> document = parser.parse(extendInfos);
		XmlNode node = document.getRoot().getSubNode(FIELD_MAPS);
		Assert.assertNotNull(node, "field-maps node must not null");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(node);
		List<XmlNode> subNodes = nameFilter.findNodeList(FIELD_MAP);
		for (XmlNode subNode : subNodes) {
			FieldMap fieldMap = new FieldMap(viewContext);
			fieldMap.setPropertyWithNode(subNode);
			if (fieldMap.getName() != null) {
				fieldMaps.add(fieldMap);
			}
		}
	}
	
	
	public <T> T object2TreeNodeWithRecursive(BSONObject object,BSONObject[] subRecords,Class<T> clazz){
		try {
			T treeNode;
			treeNode = clazz.newInstance();
			setCommonProperty(treeNode, object);
			setTreeNodesWithRecursive(treeNode, clazz,subRecords);
			return treeNode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T object2TreeNode(BSONObject object,Class<T> clazz){
		try {
			T treeNode;
			treeNode = clazz.newInstance();
			setCommonProperty(treeNode, object);
			return treeNode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
			
	
	/**
	 * 
	 * 递归设置节点属性
	 * 
	 * @param keyName
	 * @param treeNode
	 */
	private <T> void setTreeNodesWithRecursive(T treeNode,
			Class<T> clazz,BSONObject[] records) {
		List<T> subNodes = new ArrayList<T>();
		if (records != null) {
			for (BSONObject record : records) {
				BSONObject[] subRecords=viewContext.getSubRecords(record);
				T subTreeNode = object2TreeNodeWithRecursive(record,subRecords,clazz);
				subNodes.add(subTreeNode);
			}
		}
		try {
			if(subNodes.size()==0){
				subNodes=null;
			}
			BeanUtils.setProperty(treeNode, "nodes", subNodes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	

	/**
	 * 
	 * 设置普通属性
	 * 
	 * @param treeNode
	 * @param bean
	 */
	private <T> void setCommonProperty(T treeNode, BSONObject object) {
		Map<String, Object> properties = new HashMap<String, Object>();
		for (FieldMap fieldMap : fieldMaps) {
			properties.put(fieldMap.getName(),
					fieldMap.getValue(object, context));
		}
		ModelUtil.bean2Object(properties, treeNode);
	}


}
