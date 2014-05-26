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
package org.tinygroup.convert.objectxml.xstream;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.util.ArrayList;
import java.util.List;

public class XmlToObject<T> implements Converter<String, List<T>> {
    private XStream xstream;
	private String rootNodeName;

	public XmlToObject(Class<T> rootClass, String rootNodeName) {
		xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.processAnnotations(rootClass);
		this.rootNodeName = rootNodeName;
	}

	@SuppressWarnings("unchecked")
	public List<T> convert(String inputData) throws ConvertException {
		XmlNode root = new XmlStringParser().parse(inputData).getRoot();
		checkRootNodeName(root);
		List<T> list = new ArrayList<T>();
		List<XmlNode> nodeList = root.getSubNodes();
		for (XmlNode node : nodeList) {
			list.add((T) xstream.fromXML(node.toString()));
		}
		return list;
	}

	private void checkRootNodeName(XmlNode root) throws ConvertException {
		if (root.getNodeName() == null
				|| !root.getNodeName().equals(rootNodeName)) {
			throw new ConvertException("根节点名称[" + root.getRoot().getNodeName()
					+ "]与期望的根节点名称[" + rootNodeName + "]不一致！");
		}
	}
}
